(ns snaklj.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::name
 (fn [db]
   (:name db)))

(rf/reg-sub
 ::matrix
 (fn [{m :matrix}] m))

(rf/reg-sub
 ::snake-size
 (fn [{s :snake}]
   (:size s)))
