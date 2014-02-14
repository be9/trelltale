(ns trelltale.routes.home
  (:require [compojure.core :refer :all]
            [hiccup.element :refer :all]
            [trelltale.models.trello :as trello]
            [trelltale.views.layout :as layout]))

(defn home []
  (layout/common "Home" [:h1 "Hello World!"]))

(defn boards []
  (let [boards (trello/boards)]
    (println boards)
    (layout/common "Boards"
      [:h3 "Accessible Trello boards"]
      [:table.table.table-striped
        [:tr [:th "#"] [:th "Name"] [:th ""]]
        (for [[idx board] (map-indexed vector boards)]
          [:tr [:td (inc idx)]
               [:td (link-to (:url board) (:name board))]
               [:td (link-to {:class "btn btn-default"} (str "/") "Link")]])])))

(defn hooks []
  (layout/common "Hooks" [:h1 "Hello hooks!"]))

(defroutes home-routes
  (GET "/"       [] (home))
  (GET "/boards" [] (boards))
  (GET "/hooks"  [] (hooks)))
