(ns tv.tests
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [cljs.test :refer-macros [are async deftest is run-tests testing]]
            [bidi.bidi :as bidi]
            [tv.core :refer [routes]]
            [tv.impl :refer [fetch! map->Show show-description show-time]]
            [tv.impl.date :refer [date-string?]]))

(enable-console-print!)

(def show
  (map->Show
    {:description [:string "Show description. e."]
     :startTime   "2017-01-01 09:00:00"
     :stationId   "ruv"
     :title       "Show title"}))

(deftest forward-routing-test
  (testing "Routing works as expected"
    (let [match-route (partial bidi/match-route routes)]
      (are [a b] (= a b)
        (match-route "/")      {:handler :home}
        (match-route "/ruv")   {:handler :schedule :route-params {:id :ruv}}
        (match-route "/stod2") {:handler :schedule :route-params {:id :stod2}}))))

(deftest reverse-routing-test
  (testing "Reverse routing works as expected"
    (let [path-for (partial bidi/path-for routes)]
      (are [a b] (= a b)
        (path-for :home)                "/"
        (path-for :schedule :id :ruv)   "/ruv"
        (path-for :schedule :id :stod2) "/stod2"))))

(deftest api-test
  (testing "Connection to API works"
    (async done
      (go
        (is (= (:status (fetch! :ruv))) 200)
        (done)))))

(deftest show-description-test
  (testing "Trailing 'e.' is trimmed from show description"
    (is (= (show-description show) "Show description."))))

(deftest show-time-test
  (testing "Date formats correctly"
    (is (= (show-time show) "09:00"))))

(deftest date-string?-test
  (testing "Date is valid"
    (let [start-time (:startTime show)]
      (is (= (date-string? start-time) true)))))

(defn run
  []
  (run-tests))
