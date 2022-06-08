(ns snaklj.game.state
  (:require [re-frame.core :as rf]
            [snaklj.events :as events]
            [snaklj.game.logic :as logic]))

(rf/reg-event-db
 :snake/ate
 (fn [db _]
   (assoc db :food-position (logic/new-food-position (-> db :snake :positions)))))

(rf/reg-event-db
 :snake/kill
 (fn [db _]
   (-> db
       (assoc-in [:snake :state] :dead)
       (assoc :game-state :lost))))

(rf/reg-event-db
 :game/change-state
 (fn [db _]
   (let [current-state (:game-state db)]
     (when (= current-state :lost)
       (rf/dispatch [::events/initialize-game]))
     (assoc db :game-state (case current-state
                             :running :paused
                             :running)))))