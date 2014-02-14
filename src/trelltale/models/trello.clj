(ns trelltale.models.trello
  (:require [trello.core :as trello]
            [environ.core :refer :all]
            ))

(def auth {:key (env :trello-key), :token (env :trello-token)})

(defn boards [] (trello/active-boards auth))
