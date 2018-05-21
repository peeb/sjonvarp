(ns tv.impl.date
  (:require [cljs-time.core :refer [date?]]
            [cljs-time.format :refer [formatter parse unparse]]))

(def date-format (formatter "YYYY-MM-DD HH:mm:ss"))

(def time-format (formatter "HH:mm"))

(defn date-string?
  "Predicate to determine if the given string represents a valid date"
  [s]
  (->> s
       (parse date-format)
       (date?)))

(defn time-string
  "Generate a time string from the given date string"
  [date-string]
  (->> date-string
       (parse date-format)
       (unparse time-format)))
