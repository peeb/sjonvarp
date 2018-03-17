(defproject tv "0.1.0-SNAPSHOT"
  :aliases {"release" ["with-profile" "prod" "do" "clean," "cljsbuild" "once"]}
  :author "Paul Burt <paul.burt@gmail.com>"
  :cljsbuild {:builds
              {:client
               {:compiler {:compiler-stats true
                           :output-to "resources/public/js/main.js"
                           :parallel-build true}
                :source-paths ["src/cljs"]}}}
  :description "SPA showing what's on Icelandic TV today"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.191"]
                 [bidi "2.1.3"]
                 [cljs-http "0.1.44"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [funcool/cuerdas "2.0.5"]
                 [rum "0.11.2"]]
  :plugins [[lein-cljsbuild "1.1.7"]]
  :profiles {:dev {:clean-targets ^{:protect false} ["figwheel_server.log"
                                                     "resources/public/js"
                                                     :target-path]
                   :cljsbuild {:builds
                               {:client
                                {:compiler {:asset-path "js/out"
                                            :main "tv.core"
                                            :optimizations :none
                                            :output-dir "resources/public/js/out"
                                            :source-map true
                                            :source-map-timestamp true}
                                 :figwheel {:on-jsload "tv.tests/run"
                                            :open-urls ["http://localhost:3449/#/ruv"]}
                                 :source-paths ["test"]}}}
                   :dependencies [[org.clojure/tools.nrepl "0.2.13"]
                                  [figwheel-sidecar "0.5.15"]
                                  [com.cemerick/piggieback "0.2.2"]]
                   :plugins [[lein-figwheel "0.5.15"]]
                   :source-paths ["dev" "src/cljs"]}
             :prod {:cljsbuild {:builds
                                {:client
                                 {:compiler {:closure-defines {"goog.DEBUG" false}
                                             :elide-asserts true
                                             :optimizations :advanced
                                             :pretty-print false}}}}}})
