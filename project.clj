(defproject tv "0.1.0-SNAPSHOT"
  :description "SPA showing what's on Icelandic TV today"
  :author "Paul Burt <peeb@protonmail.com>"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.335"]
                 [bidi "2.1.3"]
                 [cljs-http "0.1.45"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [funcool/cuerdas "2.0.5"]
                 [figwheel-sidecar "0.5.16"]
                 [rum "0.11.2"]]
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.16"]]
  :source-paths ["dev" "src/clj"]
  :aliases {"release" ["do" "clean," "cljsbuild" "once" "prod"]}
  :clean-targets ^{:protect false} [:target-path "figwheel_server.log" "resources/public/js"]
  :cljsbuild {:builds
              {:dev {:source-paths ["src/cljs" "test/cljs"]
                     :compiler {:asset-path "js/out"
                                :compiler-stats true
                                :main "tv.core"
                                :optimizations :none
                                :output-dir "resources/public/js/out"
                                :output-to "resources/public/js/main.js"
                                :parallel-build true
                                :source-map true
                                :source-map-timestamp true}
                     :figwheel {:on-jsload "tv.test.core/run"
                                :open-urls ["http://localhost:3449/#/ruv"]}}
               :prod {:source-paths ["src/cljs"]
                      :compiler {:closure-defines {"goog.DEBUG" false}
                                 :compiler-stats true
                                 :elide-asserts true
                                 :optimizations :advanced
                                 :output-to "resources/public/js/main.js"
                                 :parallel-build true
                                 :pretty-print false}}}})
