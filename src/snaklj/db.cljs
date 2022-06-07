(ns snaklj.db
  (:require [re-frame.core :as rf]
            [snaklj.config :as config]))

(def default-db
  {:name       "snaklj"
   :game-state :stopped
   :matrix     (config/new-matrix)})

(rf/reg-event-db
 ::update-game-state
 (fn [db [_ new-state]]
   (assoc db :game-state new-state)))

(rf/reg-event-db
 ::update-matrix
 (fn [db [_ new-matrix]]
   (assoc db :matrix new-matrix)))

(rf/reg-event-db
 ::update-snake-direction
 (fn [db [_ new-dir]]
   (assoc-in db [:snake :direction] new-dir)))

(rf/reg-event-db
 ::update-snake-positions
 (fn [db [_ positions]]
   (assoc-in db [:snake :positions] positions)))

(rf/reg-event-db
 ::update-food-positions
 (fn [db [_ positions]]
   (assoc db :food-positions positions)))

(rf/reg-event-db
 ::kill-snake
 (fn [db _]
   (-> db
       (assoc-in [:snake :state] :dead)
       (assoc :game-state :lost))))