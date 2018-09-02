(ns tv.test.impl
  (:require [cljs.test :refer-macros [are async deftest is testing]]
            [bidi.bidi :as bidi]
            [tv.core :refer [routes]]
            [tv.impl :refer [fetch! map->Show show-description show-time]]
            [tv.impl.date :refer [date-string?]]))

(def ^:private show
  (map->Show
    {:description [:string "Show description. e. "]
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
      (is (= 200 (:status (fetch! :ruv))))
      (done))))

(deftest show-description-test
  (testing "Trailing 'e.' is trimmed from show description"
    (is (= "Show description." (show-description show)))))

(deftest show-time-test
  (testing "Date formats correctly"
    (is (= "09:00" (show-time show)))))

(deftest date-string?-test
  (testing "Date is valid"
    (let [start-time (:startTime show)]
      (is (= true (date-string? start-time))))))
