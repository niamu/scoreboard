(defproject scoreboard "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.7.1"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.clojure/core.async  "0.3.442"
                  :exclusions [org.clojure/tools.reader]]
                 [cljsjs/react "15.4.2-1"]
                 [cljsjs/react-dom "15.4.2-1"]
                 [sablono "0.7.7"]
                 [hiccup "1.0.5"]
                 [garden "1.3.2"]
                 [org.omcljs/om "1.0.0-alpha46"]
                 [me.raynes/conch "0.8.0"]]
  :source-paths ["src"]
  :main scoreboard.core
  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]
                :figwheel {:open-urls ["http://localhost:3449/index.html"]}
                :compiler {:main scoreboard.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/scoreboard.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :preloads [devtools.preload]}}
               {:id "min"
                :source-paths ["src"]
                :compiler {:output-to "resources/public/js/compiled/scoreboard.js"
                           :main scoreboard.core
                           :optimizations :advanced
                           :pretty-print false}}]}
  :figwheel {:css-dirs ["resources/public/css"]}
  :profiles {:uberjar {:aot :all
                       :uberjar-name "scoreboard.jar"}
             :dev {:dependencies [[binaryage/devtools "0.9.2"]
                                  [figwheel-sidecar "0.5.10"]
                                  [com.cemerick/piggieback "0.2.1"]]
                   :plugins [[lein-figwheel "0.5.10"]
                             [lein-cljsbuild "1.1.5"
                              :exclusions [[org.clojure/clojure]]]]
                   :source-paths ["src" "dev"]
                   :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                                     :target-path]}})
