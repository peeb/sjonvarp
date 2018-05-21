(ns tv.mixins
  (:require [tv.impl :refer [fetch-schedule! stations]]))

(def ^{:doc "Fetch schedule data for all stations"} schedule-loader
  {:will-mount (fn [state]
                 (doseq [[id _] stations]
                   (fetch-schedule! id))
                 state)})
