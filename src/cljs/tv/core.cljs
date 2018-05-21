(ns tv.core
  (:require [bidi.router :refer [start-router!]]
            [rum.core :as rum]
            [tv.components :as components]
            [tv.impl :refer [db]]))

(enable-console-print!)

(def ^{:doc "Client-side routes"} routes
  ["/" {""              :home
        [[keyword :id]] :schedule}])

(defn- router! []
  (start-router!
    routes
    {:on-navigate (fn [{:keys [route-params]}]
                    (let [{:keys [id] :or {id :ruv}} route-params]
                      (swap! db assoc :active-id id)))}))

(defn ^:export mount [dom-element]
  (router!)
  (rum/mount (components/Root) dom-element))
