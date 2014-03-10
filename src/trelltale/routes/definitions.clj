(ns trelltale.routes.definitions
  (:require [clojurewerkz.route-one.core :refer :all]))

(defroute root "/")

(defroute boards "/boards")

(defroute hooks "/hooks")

(defroute add-hook "/hooks/add/:board-id")

(defroute trello-callback "/trello/callback")
