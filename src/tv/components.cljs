(ns tv.components
  (:require [rum.core :as rum]
            [tv.schedule :refer [db schedule-loader show-description show-key show-time show-title]]))

(rum/defc header-component []
   [:header.hero.is-primary.is-bold
    [:div.hero-body
     [:div.container
      [:h1.hero-title.has-text-weight-bold.is-size-1
       "Dagskrá RÚV"]]]])

(rum/defc show-component [show]
  [:div.columns
   [:div.column.is-one-fifth
    [:h3.has-text-weight-bold.has-text-primary.is-size-4
     (show-time show)]]
   [:div.column
    [:h3.has-text-weight-bold.has-text-primary.is-size-4
     (show-title show)]
    [:p.has-text-weight-light.is-size-7
     (show-description show)]]])

(rum/defc schedule-component [schedule]
  [:section
   [:div.container
    (if (seq schedule)
      (mapv #(-> (show-component %)
                 (rum/with-key (show-key %)))
            schedule)
      [:p "Hleð..."])]])

(rum/defc container-component < rum/reactive schedule-loader []
  (let [{:keys [schedule]} (rum/react db)]
    (conj [:div#rum-components]
          (header-component)
          (schedule-component schedule))))
