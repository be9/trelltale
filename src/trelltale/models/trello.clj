(ns trelltale.models.trello
  (:require [org.httpkit.client :as http]
            [environ.core :refer :all]
            [cheshire.core :refer :all]))

(def auth {:key (env :trello-key), :token (env :trello-token)})

(defn ->>request [subpath & [opts]]
  (let [url (str "https://api.trello.com/1/" subpath)
        opts (merge {:url url, :query-params auth} opts)
        {:keys [body error] :as resp} @(http/request opts nil)]
    (if error
      {:error "Bad Request"}
      (parse-string body true))))

;;

(defn all-boards []
  (->>request "members/me/boards"))

(defn boards [] (filter #(not (:closed %)) (all-boards)))

;;

(defn webhooks []
  (->>request (str "tokens/" (:token auth) "/webhooks")))

(defn add-webhook [board-id url]
  (let [form-params {:description "Trelltale hook",
                     :callbackURL url,
                     :idModel board-id}]
    (->>request "webhooks" {:form-params form-params, :method :post})))
