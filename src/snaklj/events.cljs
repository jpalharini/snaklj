(ns snaklj.events
  (:require [re-frame.core :as rf]
            [snaklj.db :as db]
            [snaklj.game.setup :as game.setup]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::initialize-game
 (fn [db _]
   (let [{:keys [direction] :as starting-position} (game.setup/get-starting-position)
         initial-snake-positions (game.setup/get-initial-snake-positions starting-position)]
     (-> db
         (assoc :snake {:state     :alive
                        :direction direction
                        :positions initial-snake-positions})
         (assoc :food-positions #{})))))

(rf/reg-event-db
 :snake/ate
 (fn [db [_ position]]
   (update db :food-positions disj position)))

(rf/reg-event-db
 :game/change-state
 (fn [db _]
   (let [current-state (:game-state db)]
     (when (= current-state :lost)
       (rf/dispatch [::initialize-game]))
     (assoc db :game-state (case current-state
                             :running :paused
                             :running)))))

(defn update-valid-direction
  [db new-direction]
  (let [current-direction (-> db :snake :direction)
        invalid-direction (current-direction {:up    :down
                                              :down  :up
                                              :left  :right
                                              :right :left})]
    (when-not (= new-direction invalid-direction)
      (assoc-in db [:snake :direction] new-direction))))

(rf/reg-event-db :key/up (fn [db _] (update-valid-direction db :up)))
(rf/reg-event-db :key/down (fn [db _] (update-valid-direction db :down)))
(rf/reg-event-db :key/left (fn [db _] (update-valid-direction db :left)))
(rf/reg-event-db :key/right (fn [db _] (update-valid-direction db :right)))
