(defproject f8 "0.1.0-SNAPSHOT"
            :description "FIXME: write description"
            :url "http://example.com/FIXME"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.9.0-alpha10"]
                           [org.clojure/clojurescript "1.9.198"]
                           [org.clojure/core.async "0.2.395"
                            :exclusions [org.clojure/tools.reader]]
                           [org.omcljs/om "1.0.0-alpha41" :exclusions [cljsjs/react cljsjs/react-dom]]
                           [org.routom/routom "0.1.0-alpha9"]
                           [datascript "0.15.5"]]

            :plugins [[lein-cljsbuild "1.1.4"]
                      [lein-figwheel "0.5.8"]
                      [lein-kibit "0.1.3"]]
            :clean-targets ["target/" "index.ios.js" "index.android.js"]
            :aliases {"prod-build" ^{:doc "Recompile code with prod profile."}
                                   ["do" "clean"
                                    ["with-profile" "prod" "cljsbuild" "once" ]]}
            :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.8"]
                                            [com.cemerick/piggieback "0.2.1"]]
                             :source-paths ["src" "env/dev"]
                             :cljsbuild    {:builds [{:id           "ios"
                                                      :source-paths ["src" "env/dev"]
                                                      :figwheel     true
                                                      :compiler     {:output-to     "target/ios/not-used.js"
                                                                     :main          "env.ios.main"
                                                                     :output-dir    "target/ios"
                                                                     :optimizations :none}}
                                                     {:id           "android"
                                                      :source-paths ["src" "env/dev"]
                                                      :figwheel     true
                                                      :compiler     {:output-to     "target/android/not-used.js"
                                                                     :main          "env.android.main"
                                                                     :output-dir    "target/android"
                                                                     :optimizations :none}}
                                                     {:id           "windows"
                                                      :source-paths ["src" "env/dev" "checkouts/routom/src"]
                                                      :figwheel     true
                                                      :compiler     {:output-to     "target/windows/not-used.js"
                                                                     :main          "env.windows.main"
                                                                     :output-dir    "target/windows"
                                                                     :optimizations :none}}]}
                             :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}
                       :prod {:cljsbuild {:builds [{:id           "ios"
                                                    :source-paths ["src" "env/prod"]
                                                    :compiler     {:output-to     "index.ios.js"
                                                                   :main          "env.ios.main"
                                                                   :output-dir    "target/ios"
                                                                   :static-fns    true
                                                                   :optimize-constants true
                                                                   :optimizations :simple
                                                                   :closure-defines {"goog.DEBUG" false}}}
                                                   {:id            "android"
                                                    :source-paths ["src" "env/prod"]
                                                    :compiler     {:output-to     "index.android.js"
                                                                   :main          "env.android.main"
                                                                   :output-dir    "target/android"
                                                                   :static-fns    true
                                                                   :optimize-constants true
                                                                   :optimizations :simple
                                                                   :closure-defines {"goog.DEBUG" false}}}
                                                   {:id            "windows"
                                                    :source-paths ["src" "env/prod"]
                                                    :compiler     {:output-to     "index.windows.js"
                                                                   :main          "env.windows.main"
                                                                   :output-dir    "target/windows"
                                                                   :static-fns    true
                                                                   :optimize-constants true
                                                                   :optimizations :simple
                                                                   :closure-defines {"goog.DEBUG" false}}}]}}})
