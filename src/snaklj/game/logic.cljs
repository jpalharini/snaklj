(ns snaklj.game.logic
  (:require [snaklj.config :as config]
            [clojure.core.matrix :as matrix]))

(defn get-next-position [direction [x y]]
  (case direction
    :up [(dec x) y]
    :down [(inc x) y]
    :left [x (dec y)]
    :right [x (inc y)]))

(defn collided-with-body?
  [positions]
  (boolean
   (let [[head & rest] positions]
     (some #{head} rest))))

(defn collided-with-edge?
  [positions]
  (let [[head-x head-y] (first positions)]
    (or (= head-x config/matrix-size)
        (= head-y config/matrix-size)
        (< head-x 0)
        (< head-y 0))))

(defn collided?
  [new-positions]
  (or (collided-with-body? new-positions)
      (collided-with-edge? new-positions)))

(defn ate?
  "Checks if head position in matrix contains food (matrix[x][y] == 2)"
  [[x y] matrix*]
  (let [pos-content (matrix/mget @matrix* x y)]
    (= pos-content 2)))

(defn new-food-position []
  [(rand-int 30) (rand-int 30)])

(defn food-position-clash?
  [snake* food-positions* new-position]
  (not (or (@food-positions* new-position)
           (some #{new-position} (:positions @snake*)))))