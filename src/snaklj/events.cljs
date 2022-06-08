(ns snaklj.events
  (:require [re-frame.core :as rf]
            [snaklj.db :as db]
            [snaklj.game.setup :as game.setup]
            [snaklj.game.logic :as game.logic]))

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
                        :positions initial-snake-positions}
                :food-position (game.logic/new-food-position initial-snake-positions))))))

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
