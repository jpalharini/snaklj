(ns snaklj.db
  (:require [re-frame.core :as rf]
            [snaklj.config :as config]))

(def default-db
  {:name        "snaklj"
   :game-state  :stopped
   :matrix      (config/new-matrix)
   :snake       (merge {:state :alive
                        :size  config/snake-initial-size}
                       (config/get-starting-position))})

(rf/reg-event-db
 ::update-game-state
 (fn [db [_ ns]]
   (assoc db :game-state ns)))

(rf/reg-event-db
 ::update-matrix
 (fn [db [_ nm]]
   (assoc db :matrix nm)))

(rf/reg-event-db
 ::update-snake-state
 (fn [db [_ ns]]
   (assoc db [:snake :state] ns)))

(rf/reg-event-db
 ::increase-snake-size
 (fn [db [_]]
   (update-in db [:snake :size] inc)))