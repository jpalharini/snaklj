(ns snaklj.game.controller
  (:require [clojure.core.matrix :as matrix]
            [re-frame.core :as rf]
            [snaklj.db :as db]
            [snaklj.game.logic :as logic]
            [snaklj.config :as config]
            [snaklj.events]))

(defn draw-elements
  "Receives a matrix, an array of positions and a value to set in each position of the matrix.
   Returns a new matrix the same size as the one passed to it."
  [matrix positions value]
  (loop [new-matrix    matrix
         rem-positions positions]
    (if-let [[x y] (first rem-positions)]
      (recur (matrix/mset new-matrix x y value)
             (rest rem-positions))
      new-matrix)))

;; When drawing:
;; - Snake is 1
;; - Food is 2

(defn draw-snake
  [matrix snake*]
  (draw-elements matrix (:positions @snake*) 1))

(defn draw-food
  [matrix food-positions*]
  (draw-elements matrix @food-positions* 2))

(defn get-new-positions
  [snake* matrix*]
  (let [{:keys [direction positions]} @snake*
        curr-head-position (first positions)
        next-head-position (logic/get-next-position direction curr-head-position)
        new-positions      (conj positions next-head-position)]
    (if (logic/ate? curr-head-position matrix*)
      ;; When the snake eats, we preserve the last position to increase its size
      ;; Otherwise, we discard it
      (do (rf/dispatch-sync [:snake/ate curr-head-position])
          new-positions)
      (butlast new-positions))))

(defn move-snake!
  [snake* matrix*]
  (let [new-positions (get-new-positions snake* matrix*)]
    (if (logic/collided? new-positions)
      (rf/dispatch-sync [::db/kill-snake])
      (rf/dispatch-sync [::db/update-snake-positions new-positions]))))

(defn maybe-produce-food!
  "Produce food at the configured probablity"
  ([snake* food-positions*]
   (let [r (rand-int 100)]
     (when (< r config/food-probability-perc)
       (maybe-produce-food! snake* food-positions* (logic/new-food-position)))))
  ([snake* food-positions* new-position]
   ;; If we randomically place food in the same place as existing food or where the snake is,
   ;; come up with a new position. Else, append the new food position
   (if (logic/food-position-clash? snake* food-positions* new-position)
     (maybe-produce-food! snake* food-positions* (logic/new-food-position))
     (rf/dispatch [::db/update-food-positions (conj @food-positions* new-position)]))))