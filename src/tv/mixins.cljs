(ns tv.mixins
  (:require [tv.schedule :refer [fetch-schedule!]]))

(defn schedule-loader [db]
  {:will-mount (fn [state]
                 (fetch-schedule! db)
                 state)})
