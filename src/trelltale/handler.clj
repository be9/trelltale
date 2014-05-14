(ns trelltale.handler
  (:require [compojure.core            :refer [defroutes routes]]
            [ring.middleware.resource  :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            ;[ring.middleware.params    :refer [wrap-params]]
            [ring.middleware.json      :refer [wrap-json-params]]
            [hiccup.middleware         :refer [wrap-base-url]]
            [compojure.handler         :as handler]
            [compojure.route           :as route]
            [noir.util.middleware :as nm]
            [trelltale.routes.main :refer [main-routes]]))

(defn init []
  (println "trelltale is starting"))

(defn destroy []
  (println "trelltale is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> [(handler/site (routes main-routes app-routes))]
      (nm/app-handler)
      (wrap-json-params)
      (wrap-base-url)))
