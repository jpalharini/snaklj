(ns snaklj.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::name
 (fn [db]
   (:name db)))

(rf/reg-sub
 ::game-state
 (fn [db]
   (:game-state db)))

(rf/reg-sub
 ::matrix
 (fn [{m :matrix}] m))

(rf/reg-sub
 ::snake
 (fn [{s :snake}] s))

(rf/reg-sub
 ::snake-size
 (fn [{s :snake}]
   (count (:positions s))))

(rf/reg-sub
 ::snake-positions
 (fn [{s :snake}]
   (:positions s)))

(rf/reg-sub
 ::food-positions
 (fn [db]
   (:food-positions db)))
