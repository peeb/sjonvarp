(ns tv.impl
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [cljs.spec.alpha :as s]
            [cljs-http.client :as http]
            [cuerdas.core :as string]
            [tv.impl.date :refer [date-string? time-string]]))

(def ^:private stations
  {:ruv      {:station-name "RÚV"        :color "hotpink"}
   :stod2    {:station-name "Stöð 2"     :color "teal"}
   :stod2bio {:station-name "Stöð 2 Bíó" :color "firebrick"}
   :stod3    {:station-name "Stöð 3"     :color "darkorchid"}})

(defonce ^{:doc "Application state container"} db
  (atom {:active-id :ruv
         :stations  stations}))

(defn ^:private tidy-description
  "Remove trailing 'e.' from end of description strings"
  [description]
  (-> description
      (string/trim)
      (string/clean)
      (string/replace #".\s?e.$" ".")))

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

(s/def ::maybe-string (s/or :blank string/blank? :string string?))

(s/def ::description   ::maybe-string)
(s/def ::originalTitle ::maybe-string)
(s/def ::reactKey      string?)
(s/def ::startTime     date-string?)
(s/def ::title         string?)

(s/def ::show
  (s/keys :req-un [::reactKey ::startTime ::title]
          :opt-un [::description ::originalTitle]))

(s/def ::schedule (s/coll-of ::show :min-count 1))

(def ^:private base-url "https://apis.is/tv")

(defn ^:private id->url
  "Build an API URL from the given station ID"
  [id]
  (string/format "%s/%s" base-url (name id)))

(defn fetch!
  ""
  [id]
  (let [url (id->url id)]
    (http/get url {:with-credentials? false})))

(defn ^:private update-schedule
  "Update shows in the given schedule with additional information"
  [schedule id]
  (map-indexed (fn [idx show]
                 (assoc (map->Show show)
                        :reactKey (string/format "%s/%s" (name id) (inc idx))))
               schedule))

(defn fetch-schedule!
  "Fetch schedule data for the given station ID.
  Update application state db if the data conform to spec."
  [id]
  (go
    (let [{{:keys [results]} :body} (<! (fetch! id))]
      (when (seq results)
        (let [updated  (update-schedule results id)
              schedule (s/conform ::schedule updated)]
          (when-not (= schedule ::s/invalid)
            (swap! db assoc-in [:stations id :schedule] schedule)))))))
