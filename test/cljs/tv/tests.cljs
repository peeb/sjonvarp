(ns tv.tests
  (:require [clojure.string :as string]
            [clojure.test :refer [are deftest is run-tests testing]]
            [bidi.bidi :as bidi]
            [tv.impl :as impl]))

(enable-console-print!)

(def show
  (impl/map->Show
    {:description [:string "Show description. e."]
     :startTime   "2017-01-01 09:00:00"
     :stationId   "ruv"
     :title       "Show title"}))

(deftest test-match-route
  (testing "Routing works as expected"
    (let [match-route (partial bidi/match-route impl/routes)]
      (are [a b] (= a b)
        (match-route "/")      {:handler :home}
        (match-route "/ruv")   {:handler :schedule :route-params {:id :ruv}}
        (match-route "/stod2") {:handler :schedule :route-params {:id :stod2}}))))

(deftest test-path-for
  (testing "Reverse routing works as expected"
    (let [path-for (partial bidi/path-for impl/routes)]
      (are [a b] (= a b)
        (path-for :home)                "/"
        (path-for :schedule :id :ruv)   "/ruv"
        (path-for :schedule :id :stod2) "/stod2"))))

(deftest test-show-description
  (testing "Trailing 'e.' is trimmed from show description"
    (is (= (impl/show-description show) "Show description."))))

(deftest test-show-time
  (testing "Date formats correctly"
    (is (= (impl/show-time show) "09:00"))))

(deftest test-date-string
  (testing "Date is valid"
    (is (= (impl/date-string? (:startTime show)) true))))

(defn run []
  (run-tests))
