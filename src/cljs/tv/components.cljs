(ns tv.components
  (:require [cuerdas.core :as string]
            [rum.core :as rum]
            [tv.impl :refer [db show-description show-time show-title]]
            [tv.mixins :refer [schedule-loader]]))

(rum/defc Stations < rum/static
  "Render station navigation links"
  [stations active-id color]
  [:nav.navbar.navbar-light.bg-light.sticky-top
   [:ul.nav.nav-pills.container
    (mapv (fn [[id {:keys [station-name]}]]
            (let [active? (= active-id id)
                  url (string/format "#/%s" (name id))]
              [:li.nav-item {:key (name id)}
               [:a.nav-link.text-uppercase {:class (if active? "active")
                                            :href url}
                [:strong station-name]]]))
         stations)]])

(defn- gradient
  [from-color & [to-color]]
  (string/format "linear-gradient(to bottom right, %s, %s)"
                 from-color (or to-color "white")))

(rum/defc Header < rum/static
  "Render the page header"
  [station-name color]
  (let [background (gradient color)]
    [:header.jumbotron {:style {:background background}}
     [:div.container
      [:h1 {:style {:color "white"
                    :font-size "3em"
                    :font-weight 900}}
       (string/format "%s í dag" station-name)]]]))

(rum/defc Show < rum/static
  "Render an individual TV show"
  [show color]
  [:div.row
   [:div.col-sm-2
    [:h3.font-weight-light {:style {:color color}}
     [:em (show-time show)]]]
   [:div.col-sm-10.show-title
    [:h3 (show-title show)]
    [:p.text-muted.show-description
     (show-description show)]]])

(rum/defc Schedule < rum/static
  "Render a schedule of TV shows"
  [schedule color]
  [:div.container
   (if (seq schedule)
     (mapv (fn [{:keys [reactKey] :as show}]
             (-> (Show show color)
                 (rum/with-key reactKey)))
           schedule)
     [:p [:em "Hleð..."]])])

(rum/defc Root < rum/reactive schedule-loader
  "One component to rule them all"
  []
  (let [{:keys [active-id stations]} (rum/react db)
        {:keys [color schedule station-name]} (get stations active-id)]
    (conj [:div#components]
          (Stations stations active-id color)
          (Header station-name color)
          (Schedule schedule color))))
