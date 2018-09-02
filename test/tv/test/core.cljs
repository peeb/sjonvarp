(ns tv.test.core
  (:require [cljs.test :refer-macros [run-tests]]
            [tv.test.impl]))

(enable-console-print!)

(defn run []
  (run-tests 'tv.test.impl))
