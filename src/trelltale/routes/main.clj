(ns trelltale.routes.main
  (:require [compojure.core :refer :all]
            [trelltale.request-handlers :as handlers]
            [trelltale.routes.definitions :refer :all]))

(defroutes main-routes
  (GET root-template     request (do #_(println 123) (println request) (handlers/home request)) )
  (GET boards-template   [] (handlers/boards))
  (GET hooks-template    [] (handlers/hooks))
  (GET add-hook-template [board-id]
    (handlers/add-hook board-id))

  (GET remove-hook-template [hook-id] (handlers/remove-hook hook-id))

  (HEAD trello-callback-template [] (handlers/trello-check))
  (POST trello-callback-template {params :params} (handlers/trello-callback params)))
