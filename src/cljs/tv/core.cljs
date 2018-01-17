(ns tv.core
  (:require [rum.core :as rum]
            [tv.components :as components]
            [tv.impl :as impl]))

(enable-console-print!)

(defn ^:export mount [dom-element]
  (impl/start-router!)
  (rum/mount (components/Root) dom-element))
