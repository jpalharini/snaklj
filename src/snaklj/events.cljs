(ns snaklj.events
  (:require [re-frame.core :as rf]
            [snaklj.db :as db]
            [snaklj.game :as game]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::initialize-game
 (fn [{:keys [snake] :as db} _]
   (update db :matrix game/draw-snake snake)))
