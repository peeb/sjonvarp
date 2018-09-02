(ns tv.schedule
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [cljs.spec.alpha :as s]
            [cljs-http.client :as http]
            [cuerdas.core :as string]
            [tv.date :as date]
            tv.spec))

(defonce db (atom {:schedule {}}))

(def ^:private repeat-regex #".[ ]?e.$")

(defn ^:private repeat? [s]
  (boolean (re-matches repeat-regex s)))

(def ^:private clean
  (comp string/trim
        string/clean
        #(string/replace % repeat-regex ".")))

(s/def ::not-repeat (s/and string? #(not (repeat? %))))

(s/fdef clean
  :args (s/cat :s string?)
  :ret ::not-repeat)

(defprotocol ShowProtocol
  (show-description [show])
  (show-key [show])
  (show-time [show])
  (show-title [show]))

(defrecord Show
  [description originalTitle startTime title]
  ShowProtocol
  (show-description [_]
    (let [[tag value] description]
      (if (= tag :string)
        (clean value)
        "Engin lýsing tiltæk")))
  (show-key [_]
    (date/date->timestamp startTime))
  (show-time [_]
    (date/date->time startTime))
  (show-title [_]
    (str title
      (let [[tag value] originalTitle
            same? (or (= tag :blank)
                      (string/caseless= value title))]
        (when-not same? (str " / " value))))))

(defn ^:private fetch!
  []
  (let [url "https://apis.is/tv/ruv"]
    (http/get url {:with-credentials? false})))

(defn fetch-schedule!
  []
  (go
    (let [{{:keys [results]} :body} (<! (fetch!))]
      (when (seq results)
        (let [schedule (s/conform :tv.spec/schedule (map map->Show results))]
          (when-not (= schedule ::s/invalid)
            (swap! db assoc :schedule schedule)))))))

(def ^{:doc "Fetch schedule data"} schedule-loader
  {:will-mount (fn [state]
                 (fetch-schedule!)
                 state)})
