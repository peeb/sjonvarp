(ns tv.core
  (:require [rum.core :as rum]
            [tv.components :as components]))

(enable-console-print!)

(defn ^:export mount [dom-element]
  (rum/mount (components/container-component) dom-element))
