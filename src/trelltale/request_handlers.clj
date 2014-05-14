(ns trelltale.request-handlers
  (:require [hiccup.element :refer :all]
            [trelltale.models.trello :as trello]
            [trelltale.models.format-message :as fm]
            [ring.util.response :as ring]
            [trelltale.views.layout :as layout]
            [trelltale.routes.definitions :refer :all]
            [hiccup.util]
            [noir.session :as session]))

(defn home [req]
  #_(print req)
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
  (let [hooks         (trello/webhooks)
        boards        (trello/boards)
        boards-by-id  (zipmap (map #(:id %) boards) boards)]

    (layout/common "Hooks"
      [:h3 "Enabled webhooks"]
      [:table.table.table-striped
        [:tr [:th "#"]
             [:th "Board"]
             [:th "Description"]
             [:th "Callback"]
             [:th "State"]
             [:th "&nbsp;"]]

        (for [[idx hook] (map-indexed vector hooks)]
          (let [board (boards-by-id (:idModel hook))]
            [:tr [:td (inc idx)]
                 [:td (link-to (:url board) (:name board))]
                 [:td (:description hook)]
                 [:td (:callbackURL hook)]
                 [:td (if (:active hook) "Active" "Disabled")]
                 [:td (link-to {:class "btn btn-danger"} (remove-hook-path :hook-id (:id hook)) "Remove")]]))])))

(defn add-hook [board-id]
  (session/flash-put! :success (str "Hello there! " board-id))
  (ring/redirect (root-path)))

(defn remove-hook [hook-id]
  (session/flash-put! :success (str "Hello there! " hook-id))
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
