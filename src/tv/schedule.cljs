(ns tv.schedule
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [cljs.spec.alpha :as s]
            [cljs-http.client :as http]
            [cuerdas.core :as string]
            [tv.date :refer [date->time date->timestamp]]
            [tv.spec :as spec :refer [clean]]))

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
    (date->timestamp startTime))
  (show-time [_]
    (date->time startTime))
  (show-title [_]
    (str title
      (let [[tag value] originalTitle
            same? (or (= tag :blank)
                      (string/caseless= value title))]
        (when-not same? (str " / " value))))))

(def ^:private api-url "https://apis.is/tv/ruv")

(defn ^:private fetch! [url]
  (http/get url {:with-credentials? false}))

(defn fetch-schedule! [container]
  (go
    (let [{{:keys [results]} :body} (<! (fetch! api-url))]
      (when (seq results)
        (let [schedule (s/conform ::spec/schedule (map map->Show results))]
          (when-not (= schedule ::s/invalid)
            (reset! container schedule)))))))
