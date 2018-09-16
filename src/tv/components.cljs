(ns tv.components
  (:require [rum.core :refer [react reactive with-key] :refer-macros [defc]]
            [tv.mixins :refer [schedule-loader]]
            [tv.schedule :refer [show-description show-key show-time show-title]]))

(defonce ^:private db (atom {}))

(defc header-component []
  [:header.hero.is-primary.is-bold
   [:div.hero-body
    [:div.container
     [:h1.hero-title.has-text-weight-bold.is-size-1
      "Dagskrá RÚV"]]]])

(defc show-component [show]
  [:div.columns
   [:div.column.is-one-fifth
    [:h3.has-text-weight-bold.has-text-primary.is-italic.is-size-4
     (show-time show)]]
   [:div.column
    [:h3.has-text-weight-bold.has-text-primary.is-size-4
     (show-title show)]
    [:p.has-text-weight-light.is-size-7
     (show-description show)]]])

(defc schedule-component < reactive (schedule-loader db) []
  (let [schedule (react db)]
    [:section
     [:div.container
      (if (seq schedule)
        (mapv #(-> (show-component %)
                   (with-key (show-key %)))
              schedule)
        [:p.has-text-weight-light "Hleð..."])]]))

(defc container-component []
  (conj [:div#components]
        (header-component)
        (schedule-component)))
