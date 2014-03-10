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

(defn all-boards []
  (->>request "members/me/boards"))

(defn boards [] (filter #(not (:closed %)) (all-boards)))

(defn add-webhook [board-id url]
  (let [form-params {:description "Trelltale hook",
                     :callbackURL url,
                     :idModel board-id}]
    (->>request "webhooks" {:form-params form-params, :method :post})))


"
Создание карточки:

{:model {:desc , :name notification-test-board, :labelNames {:red , :orange , :yellow , :green , :blue , :purple }, :descData nil, :prefs {:selfJoin false, :background blue, :invitations members, :canInvite true, :canBeOrg true, :backgroundColor #23719F, :backgroundImageScaled nil, :canBePrivate true, :backgroundBrightness unknown, :cardCovers true, :cardAging regular, :backgroundImage nil, :comments members, :permissionLevel private, :canBePublic true, :backgroundTile false, :voting disabled}, :pinned true, :closed false, :idOrganization nil, :url https://trello.com/b/bifRIv9V/notification-test-board, :shortUrl https://trello.com/b/bifRIv9V, :id 5303050df893a6f97ccc36c1}, :action {:id 53030f92bc1a56277dbbbc5a, :idMemberCreator 4f07e60ce23cb6fe6d037c53, :data {:board {:shortLink bifRIv9V, :name notification-test-board, :id 5303050df893a6f97ccc36c1}, :list {:name To Do, :id 5303050df893a6f97ccc36c2}, :card {:shortLink 4QdPeYcR, :idShort 3, :name test3, :id 53030f92bc1a56277dbbbc59}}, :type createCard, :date 2014-02-18T07:45:22.326Z, :memberCreator {:id 4f07e60ce23cb6fe6d037c53, :avatarHash f72508e4e4ca6095df0e92839f117903, :fullName oleg dashevskii, :initials OD, :username be9}}}

Перемещение карточки:

{:model {:desc , :name notification-test-board, :labelNames {:red , :orange , :yellow , :green , :blue , :purple }, :descData nil, :prefs {:selfJoin false, :background blue, :invitations members, :canInvite true, :canBeOrg true, :backgroundColor #23719F, :backgroundImageScaled nil, :canBePrivate true, :backgroundBrightness unknown, :cardCovers true, :cardAging regular, :backgroundImage nil, :comments members, :permissionLevel private, :canBePublic true, :backgroundTile false, :voting disabled}, :pinned true, :closed false, :idOrganization nil, :url https://trello.com/b/bifRIv9V/notification-test-board, :shortUrl https://trello.com/b/bifRIv9V, :id 5303050df893a6f97ccc36c1}, :action {:id 53030fd0ed97fbb97c8ebf0f, :idMemberCreator 4f07e60ce23cb6fe6d037c53, :data {:listAfter {:name Doing, :id 5303050df893a6f97ccc36c3}, :listBefore {:name To Do, :id 5303050df893a6f97ccc36c2}, :board {:shortLink bifRIv9V, :name notification-test-board, :id 5303050df893a6f97ccc36c1}, :card {:shortLink 4QdPeYcR, :idShort 3, :name test3, :id 53030f92bc1a56277dbbbc59, :idList 5303050df893a6f97ccc36c3}, :old {:idList 5303050df893a6f97ccc36c2}}, :type updateCard, :date 2014-02-18T07:46:24.681Z, :memberCreator {:id 4f07e60ce23cb6fe6d037c53, :avatarHash f72508e4e4ca6095df0e92839f117903, :fullName oleg dashevskii, :initials OD, :username be9}}}

Join:

{:model {:desc , :name notification-test-board, :labelNames {:red , :orange , :yellow , :green , :blue , :purple }, :descData nil, :prefs {:selfJoin false, :background blue, :invitations members, :canInvite true, :canBeOrg true, :backgroundColor #23719F, :backgroundImageScaled nil, :canBePrivate true, :backgroundBrightness unknown, :cardCovers true, :cardAging regular, :backgroundImage nil, :comments members, :permissionLevel private, :canBePublic true, :backgroundTile false, :voting disabled}, :pinned true, :closed false, :idOrganization nil, :url https://trello.com/b/bifRIv9V/notification-test-board, :shortUrl https://trello.com/b/bifRIv9V, :id 5303050df893a6f97ccc36c1}, :action {:id 53030ffa30caf3be2ecca328, :idMemberCreator 4f07e60ce23cb6fe6d037c53, :data {:board {:shortLink bifRIv9V, :name notification-test-board, :id 5303050df893a6f97ccc36c1}, :card {:shortLink tggfwmYM, :idShort 2, :name test2, :id 53030dd3a72a23467da131ca}, :idMember 4f07e60ce23cb6fe6d037c53}, :type addMemberToCard, :date 2014-02-18T07:47:06.510Z, :member {:id 4f07e60ce23cb6fe6d037c53, :avatarHash f72508e4e4ca6095df0e92839f117903, :fullName oleg dashevskii, :initials OD, :username be9}, :memberCreator {:id 4f07e60ce23cb6fe6d037c53, :avatarHash f72508e4e4ca6095df0e92839f117903, :fullName oleg dashevskii, :initials OD, :username be9}}}

Unjoin:

{:model {:desc , :name notification-test-board, :labelNames {:red , :orange , :yellow , :green , :blue , :purple }, :descData nil, :prefs {:selfJoin false, :background blue, :invitations members, :canInvite true, :canBeOrg true, :backgroundColor #23719F, :backgroundImageScaled nil, :canBePrivate true, :backgroundBrightness unknown, :cardCovers true, :cardAging regular, :backgroundImage nil, :comments members, :permissionLevel private, :canBePublic true, :backgroundTile false, :voting disabled}, :pinned true, :closed false, :idOrganization nil, :url https://trello.com/b/bifRIv9V/notification-test-board, :shortUrl https://trello.com/b/bifRIv9V, :id 5303050df893a6f97ccc36c1}, :action {:id 530310266ef854b41eff0033, :idMemberCreator 4f07e60ce23cb6fe6d037c53, :data {:board {:shortLink bifRIv9V, :name notification-test-board, :id 5303050df893a6f97ccc36c1}, :card {:shortLink tggfwmYM, :idShort 2, :name test2, :id 53030dd3a72a23467da131ca}, :deactivated false, :idMember 4f07e60ce23cb6fe6d037c53}, :type removeMemberFromCard, :date 2014-02-18T07:47:50.924Z, :member {:id 4f07e60ce23cb6fe6d037c53, :avatarHash f72508e4e4ca6095df0e92839f117903, :fullName oleg dashevskii, :initials OD, :username be9}, :memberCreator {:id 4f07e60ce23cb6fe6d037c53, :avatarHash f72508e4e4ca6095df0e92839f117903, :fullName oleg dashevskii, :initials OD, :username be9}}}


"


