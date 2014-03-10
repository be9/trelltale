(ns trelltale.models.format-message
  (:require [hiccup.core :refer [h]]
            [clojure.string :as s]
            [clj-time.format :as tf]))

(defn truncate
  ([s] (truncate s 200))
  ([s maxlen] 
    (let [size (count s)]
      (if (>= size maxlen)
        (str (subs s 0 maxlen) "…")
        s))))

(defn board-id [action]
  (((action :data) :board) :id))

(defn card-url [action]
  (let [card     ((action :data) :card)
        id       (card :id)
        id-short (card :idShort)]
;    (str "https://trello.com/card/" id "/" (board-id action) "/" id-short)))
    (str "https://trello.com/c/" (card :shortLink) "/" id-short)))

(defn card-name [action]
  (((action :data) :card) :name))

(defn list-name [action]
  (((action :data) :list) :name))

(defn actor [action]
  ((action :memberCreator) :username))

(defn card-link [action]
  (str "<a href=\"" (card-url action) "\">" (h (card-name action)) "</a>"))

(def formatter (tf/formatter "MMM dd HH:mm"))

(defn formatted-time [sdatetime]
  (tf/unparse formatter (tf/parse sdatetime)))

(defn comment-card-message [action]
  (let [who  (h (actor action))
        text (h (truncate (s/join " " (s/split-lines ((action :data) :text)))))]
    (str who " commented on card " (card-link action) ": " text)))

(defn create-card-message [action]
  (let [who  (h (actor action))
        lst  (h (list-name action))]
    (str who " created card " (card-link action) " in <b>" lst "</b>")))

(defn delete-card-message [action]
  (let [who  (h (actor action))
        lst  (h (list-name action))]
    (str who " deleted a card from <b>" lst "</b>")))

(defn update-card-message [action]
  (let [data (action :data)
        old  (data :old)
        card (data :card)
        who  (h (actor action))
        ]
    (condp #(contains? %2 %1) old
      :name   (str who " renamed card “" (h (old :name)) "” to " (card-link action))
      :desc   (str who " updated description for card " (card-link action))
      :idList (str who " moved card " (card-link action) " from <b>"
                   (h ((data :listBefore) :name)) "</b> to <b>" (h ((data :listAfter) :name)) "</b>")

      :due    (if-let [due (card :due)]
                (str who " changed due date on card " (card-link action) " to <b>" (formatted-time due) "</b>")
                (str who " removed due date from card " (card-link action)))

      :closed (str who " " (if (old :closed) "unarchived" "archived") " card " (card-link action))
      nil)))

(defn member-related-message [action [self-action-word other-action-word preposition]]
  (let [who (h (actor action))]
    (if (= ((action :memberCreator) :id)
           ((action :member) :id))
      (str who " " self-action-word  " " (card-link action))
      (str who " " other-action-word " " (h ((action :member) :username)) " " preposition " " (card-link action)))))

(defn add-member-message [action]
  (member-related-message action ["joined" "added" "to"]))

(defn remove-member-message [action]
  (member-related-message action ["left" "removed" "from"]))

(defn add-attachment-message [action]
  (let [who       (h (actor action))
        att       ((action :data) :attachment)
        att-url   (att :url)
        att-name  (att :name)
        att-link  (str "<a href=\"" att-url "\">" (h att-name) "</a>")]

    (str who " added an attachment to card " (card-link action) ": " att-link)))

(defn check-item-message [action]
  ;elsif node["type"] == "updateCheckItemStateOnCard" && data["checkItem"]["state"] == "complete"
        ;return "[#{prefix}] #{creator_fullname} completed #{data["checkItem"]["name"]} on #{card_name} #{card_url}"

  (let [chk ((action :data) :checkItem)]
    (when (= (chk :state) "complete")
      (str (h (actor action)) " completed " (h (chk :name)) " on " (card-link action)))))


(def message-map
  {"commentCard"          comment-card-message
   "createCard"           create-card-message
   "updateCard"           update-card-message
   "deleteCard"           delete-card-message
   "addMemberToCard"      add-member-message
   "removeMemberFromCard" remove-member-message
   "addAttachmentToCard"  add-attachment-message
   "updateCheckItemStateOnCard" check-item-message})

(defn format-message [cdata]
  (let [action  (:action cdata)
        atype   (and action (:type action))
        fun     (message-map atype)]
    (when (fn? fun)
      (fun action))))

