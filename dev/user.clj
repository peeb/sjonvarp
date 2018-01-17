(ns user
  (:require [figwheel-sidecar.repl-api :as figwheel]))

(defn reset [] (figwheel/reset-autobuild))
(defn start [] (figwheel/start-figwheel!))
(defn status [] (figwheel/fig-status))
(defn stop [] (figwheel/stop-figwheel!))
