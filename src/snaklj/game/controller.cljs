(ns snaklj.game.controller
  (:require [clojure.core.matrix :as matrix]
            [re-frame.core :as rf]
            [snaklj.db :as db]
            [snaklj.events]
            [snaklj.game.logic :as logic]))

(defn draw-snake
  [matrix snake*]
  (loop [new-matrix    matrix
         rem-positions (:positions @snake*)]
    (if-let [[x y] (first rem-positions)]
      (recur (matrix/mset new-matrix x y 1)
             (rest rem-positions))
      new-matrix)))

(defn draw-food
  [matrix food-position*]
  (let [[x y] @food-position*]
    (matrix/mset matrix x y 2)))

(defn get-new-positions
  [snake* matrix*]
  (let [{:keys [direction positions]} @snake*
        curr-head-position (first positions)
        next-head-position (logic/get-next-position direction curr-head-position)
        new-positions      (conj positions next-head-position)]
    (if (logic/ate? curr-head-position matrix*)
      ;; When the snake eats, we preserve the last position to increase its size
      ;; Otherwise, we discard it
      (do (rf/dispatch-sync [:snake/ate])
          new-positions)
      (butlast new-positions))))

(defn move-snake!
  [snake* matrix*]
  (let [new-positions (get-new-positions snake* matrix*)]
    (if (logic/collided? new-positions)
      (rf/dispatch-sync [:snake/kill])
      (rf/dispatch-sync [::db/update-snake-positions new-positions]))))