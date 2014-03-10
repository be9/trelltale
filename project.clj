(defproject trelltale "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.4"]
                 [environ "0.4.0"]
                 [clojurewerkz/route-one "1.0.0"]
                 [lib-noir "0.8.0"]
                 [http-kit "2.1.16"]
                 [cheshire "5.3.1"]
                 [com.taoensso/timbre "3.0.1"]
                 [ring/ring-json "0.2.0"]
                 [clj-time "0.6.0"]
                 [ring-server "0.3.0"]]
  :plugins [[lein-ring "0.8.7"]]
  :ring {:handler trelltale.handler/app
         :init trelltale.handler/init
         :destroy trelltale.handler/destroy}
  :aot :all
  :main trelltale.repl
  :profiles
  {:production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [org.clojure/tools.namespace "0.2.4"] [ring/ring-devel "1.2.0"]]}})
