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

(defn random-food-position []
  [(rand-int 30) (rand-int 30)])

(defn food-position-clash?
  [snake-positions new-position]
  (some #{new-position} snake-positions))

(defn new-food-position
  ([snake-positions]
   (new-food-position snake-positions (random-food-position)))
  ([snake-positions new-position]
   ;; If we randomically place food where the snake is, come up with a new position.
   ;; Else, return the new food position.
   (if (food-position-clash? snake-positions new-position)
     (new-food-position snake-positions (random-food-position))
     new-position)))