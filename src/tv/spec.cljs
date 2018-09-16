(ns tv.spec
  (:require [cljs.spec.alpha :as s]
            [cuerdas.core :as string]
            [tv.date :as date]))

(def ^:private repeat-pattern #".[ ]?e.$")

(defn ^:private repeat? [s]
  (boolean (re-matches repeat-pattern s)))

(def ^:private not-repeat? (complement repeat?))

(s/def ::not-repeat (s/and string? not-repeat?))

(def clean
  (comp string/trim
        string/clean
        #(string/replace % repeat-pattern ".")))

(s/fdef clean
  :args (s/cat :s string?)
  :ret ::not-repeat)

(s/def ::maybe-string (s/or :blank string/blank? :string string?))

(s/def ::description ::maybe-string)
(s/def ::originalTitle ::maybe-string)
(s/def ::startTime ::date/date-string)
(s/def ::title string?)

(s/def ::show
  (s/keys :req-un [::startTime ::title]
          :opt-un [::description ::originalTitle]))

(s/def ::schedule (s/coll-of ::show :min-count 1))
