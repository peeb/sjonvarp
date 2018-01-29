(ns tv.impl
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [cljs.spec.alpha :as spec]
            [bidi.router :as bidi]
            [cljs-http.client :as http]
            [cljs-time.core :as t]
            [cljs-time.format :as tf]
            [cuerdas.core :as string]))

(def ^:private stations
  {:ruv      {:station-name "RÚV"        :color "hotpink"}
   :stod2    {:station-name "Stöð 2"     :color "teal"}
   :stod2bio {:station-name "Stöð 2 Bíó" :color "firebrick"}
   :stod3    {:station-name "Stöð 3"     :color "darkorchid"}})

(defonce db (atom {:active-id :ruv :stations stations}))

(defn css-gradient
  [from-color & [to-color]]
  (string/format "linear-gradient(to bottom right, %s, %s)"
                 from-color (or to-color "white")))

(defn- tidy-description
  "Remove trailing 'e.' from end of description strings"
  [description]
  (-> description
      string/trim
      string/clean
      (string/replace #".\s?e.$" ".")))

(def ^:private date-format (tf/formatter "YYYY-MM-DD HH:mm:ss"))

(def ^:private time-format (tf/formatter "HH:mm"))

(defn date-string?
  "Predicate to determine if the given string represents a valid date"
  [date-string]
  (->> date-string
       (tf/parse date-format)
       t/date?))

(defn time-string
  "Generate a time string from the given date string"
  [date-string]
  (->> date-string
       (tf/parse date-format)
       (tf/unparse time-format)))

(defprotocol ShowProtocol
  (show-description [show])
  (show-time [show])
  (show-title [show]))

(defrecord Show
  [description originalTitle startTime title]
  ShowProtocol
  (show-description [_]
    (let [[tag value] description]
      (if (= tag :string)
        (tidy-description value)
        "Engin lýsing tiltæk")))
  (show-time [_]
    (time-string startTime))
  (show-title [_]
    (let [[tag value] originalTitle]
      (str title
           (when-not (= tag :blank)
             (string/format " / %s" value))))))

(spec/def ::maybe-string (spec/or :blank string/blank? :string string?))

(spec/def ::description ::maybe-string)
(spec/def ::originalTitle ::maybe-string)
(spec/def ::reactKey string?)
(spec/def ::startTime date-string?)
(spec/def ::title string?)

(spec/def ::show
  (spec/keys :req-un [::reactKey ::startTime ::title]
             :opt-un [::description ::originalTitle]))

(spec/def ::schedule (spec/coll-of ::show :min-count 1))

(defn- id->url
  "Build an API URL from the given station ID"
  [id]
  (string/format "https://apis.is/tv/%s" (name id)))

(defn- fetch!
  [id]
  (let [url (id->url id)]
    (http/get url {:with-credentials? false})))

(defn- update-schedule
  "Update shows in the given schedule with additional information"
  [schedule id]
  (map-indexed (fn [idx show]
                 (assoc (map->Show show)
                        :reactKey (string/format "%s/%s" (name id) (inc idx))))
               schedule))

(defn- fetch-schedule!
  "Fetch schedule data for the given station ID.
  Update application state db if the data conform."
  [id]
  (go
    (let [{{:keys [results]} :body} (<! (fetch! id))]
      (when (seq results)
        (let [updated  (update-schedule results id)
              schedule (spec/conform ::schedule updated)]
          (when-not (= schedule ::spec/invalid)
            (swap! db assoc-in [:stations id :schedule] schedule)))))))

(def ^{:doc "Fetch schedule data for all stations"} schedule-loader
  {:will-mount (fn [state]
                 (doseq [[id _] stations]
                   (fetch-schedule! id))
                 state)})

(def ^{:doc "Client-side routes" :private true} routes
  ["/" {"" :home
        [[keyword :id]] :schedule}])

(defn start-router!
  "Start client-side routing"
  []
  (bidi/start-router!
    routes
    {:on-navigate (fn [{:keys [route-params]}]
                    (let [{:keys [id] :or {id :ruv}} route-params]
                      (swap! db assoc :active-id id)))}))
