(ns trelltale.main
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [trelltale.handler]))

(defn -main [port]
  (jetty/run-jetty trelltale.handler/app {:port (Integer. port) :join? false}))
