(ns tv.date
  (:require [cljs.spec.alpha :as s]
            [cljsjs.moment]))

(defn date?
  [date]
  (.isValid (js/moment date)))

(s/def ::date-string (s/and string? date?))

(defn date->time
  [date]
  (.format (js/moment date) "HH:mm"))

(defn date->timestamp
  [date]
  (.unix (js/moment date)))
