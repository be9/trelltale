(ns trelltale.request-handlers
  (:require [hiccup.element :refer :all]
            [trelltale.models.trello :as trello]
            [trelltale.models.format-message :as fm]
            [ring.util.response :as ring]
            [trelltale.views.layout :as layout]
            [trelltale.routes.definitions :refer :all]
            [noir.session :as session]))

(defn home []
  (layout/common "Home" [:h1 "Hello World!"]))

(defn boards []
  (let [boards (trello/boards)]
    (layout/common "Boards"
      [:h3 "Accessible Trello boards"]
      [:table.table.table-striped
        [:tr [:th "#"] [:th "Name"] [:th ""]]
        (for [[idx board] (map-indexed vector boards)]
          [:tr [:td (inc idx)]
               [:td (link-to (:url board) (:name board))]
               [:td (link-to {:class "btn btn-default"} (add-hook-path :board-id (:id board)) "Link")]])])))

(defn hooks []
  (layout/common "Hooks" [:h1 "Hello hooks!"]))

(defn add-hook [board-id]
  (session/flash-put! :success (str "Hello there! " board-id))
  (ring/redirect (root-path)))

(defn trello-check
  []
  (println "Trello is checking us!")
  "Gotcha")

(defn trello-callback
  [params]
  (println (fm/format-message params))
  (prn params)
  "Gotcha")
