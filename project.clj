(defproject hackbench "0.4.1-SNAPSHOT"
  :description "Rapid prototyping using clojure"
  :url "https://cljsnode.herokuapp.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/core.async "0.4.490"]
                 [cljsjs/react "15.6.2-4"]
                 [cljsjs/react-dom "15.6.2-4"]
                 [cljsjs/react-dom-server "15.6.2-4"]
                 [cljsjs/create-react-class "15.6.2-0"]
                 [cljsjs/material-ui "0.19.2-0"]
                 [cljs-react-material-ui "0.2.48"
                  :exclusions [cljsjs/material-ui
                               org.clojure/clojure
                               org.clojure/clojurescript]]
                 [cljsjs/pubnub "4.1.1-0"]
                 [camel-snake-kebab "0.4.0"]
                 [mount "0.1.12"]
                 [bidi "2.1.4"]
                 [reagent "0.7.0"]
                 [secretary "1.2.3"]
                 [re-frame "0.10.5"]
                 [cljs-http "0.1.46"]
                 [com.taoensso/timbre "4.10.0"
                  :exclusions [com.taoensso/encore]]
                 [com.taoensso/sente "1.14.0-RC2"]
                 [kioo "0.5.0"
                  :exclusions [org.clojure/clojure cljsjs/react cljsjs/react-dom]]
                 [macchiato/core "0.2.14"]]


  :npm {:dependencies [[express "4.16.4"]
                       [cors "2.8.5"]
                       [xhr2 "0.1.4"]
                       [xmldom "0.1.27"]
                       [react "16.2.0"]
                       [react-dom "16.2.0"]
                       [create-react-class "15.6.3"]
                       [pubnub "4.1.1"]
                       [~(symbol "@okta/okta-sdk-nodejs") "1.2.0"]
                       [twilio "3.30.0"]
                       [source-map-support "0.5.3"]]
        :root :root}

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-npm "0.6.2"]]

  :min-lein-version "2.5.3"

  :hooks [leiningen.cljsbuild]

  :aliases {"start" ["npm" "start"]
            "test" ["with-profile" "test" "doo" "node" "server" "once"]}

  :main "main.js"

  :source-paths ["src/universal"]

  :clean-targets ^{:protect false} [[:cljsbuild :builds :server :compiler :output-to]
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    "node_modules"
                                    :target-path :compile-path]

  :figwheel {:http-server-root "public"
             :css-dirs ["resources/public/css"]
             :server-logfile "logs/figwheel.log"
             :load-all-builds false
             :builds-to-start [:app :server]}

  :cljsbuild {:builds
              {:app
               {:source-paths ["src/browser" "src/universal"]
                :compiler {:output-to "resources/public/js/out/app.js"
                           :output-dir "resources/public/js/out"
                           :asset-path "js/out"
                           :main app.start
                           :optimizations :none}}

               :server
               {:source-paths ["src/node" "src/universal"]
                :compiler {:target :nodejs
                           :output-to "main.js"
                           :output-dir "target"
                           :main server.core
                           :foreign-libs [{:file "src/node/polyfill/simple.js"
                                           :provides ["polyfill.simple"]}]
                           :optimizations :none}}}}


  :profiles {:dev
             {:plugins
              [[lein-figwheel "0.5.16"]
               [lein-doo "0.1.8"]]
              :cljsbuild
              {:builds
               {:app
                {:compiler {:pretty-print true
                            :source-map false}
                 :figwheel {:on-jsload "reagent.core/force-update-all"}}
                :server
                {:compiler {:pretty-print true
                            :source-map false}
                 :figwheel {:heads-up-display false}}}}
              :npm {:dependencies [[ws "3.3.3"]]}}

             :test {:cljsbuild
                    {:builds
                     {:server
                      {:source-paths ["test"]
                       :compiler {:main runners.doo
                                  :optimizations :none
                                  :output-to "target/test/server.js"
                                  :output-dir "target/test"}}}}}

             :production
             {; :env {:production true}
              :cljsbuild
              {:builds
               {:server
                {:compiler {;:optimizations :simple
                            :pretty-print false}}
                :app
                {:compiler {:output-dir "target/app/out"
                            :optimizations :advanced
                            :infer-externs true
                            :pretty-print false}}}}}})
