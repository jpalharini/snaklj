(ns snaklj.game.setup
  (:require [snaklj.config :as config]
            [snaklj.game.logic :as logic]))

(defn- safe-random-position []
  "Gets a position that is, at least, 5 blocks away from an edge"
  (+ 5 (rand-int (- config/matrix-size 10))))

(defn get-starting-position
  "Builds a map of head represented by [x y] tuple and direction"
  []
  (let [head-x (safe-random-position)
        head-y (safe-random-position)]
    {:head      [head-x head-y]
     :direction (rand-nth [:up :down :left :right])}))

(defn- reverse-direction
  "When initially drawing, we need to reverse direction"
  [direction]
  (case direction :up :down
                  :down :up
                  :left :right
                  :right :left))

(defn get-initial-snake-positions
  "Snake is drawn from head to tail, so we look at previous positions as 'next'"
  [{:keys [head direction]}]
  (loop [positions [head]]
    (if (< (count positions) config/snake-initial-size)
      (let [current-pos (last positions)
            next-pos    (logic/get-next-position (reverse-direction direction) current-pos)]
        (recur (conj positions next-pos)))
      (apply list positions))))