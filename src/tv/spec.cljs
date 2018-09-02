(ns tv.spec
  (:require [cljs.spec.alpha :as s]
            [cuerdas.core :as string]
            [tv.date :as date]))

(s/def ::possible-string (s/or :blank string/blank? :string string?))

(s/def ::description ::possible-string)
(s/def ::originalTitle ::possible-string)
(s/def ::startTime ::date/date-string)
(s/def ::title string?)

(s/def ::show
  (s/keys :req-un [::startTime ::title]
          :opt-un [::description ::originalTitle]))

(s/def ::schedule (s/coll-of ::show :min-count 1))
