(ns snaklj.config
  (:require [clojure.core.matrix :as matrix]))

(def debug? true)

(def matrix-size 30)

(def snake-initial-size 3)

(defn- safe-random-position []
  "Gets a position that is, at least, 5 blocks away from an edge"
  (+ 5 (rand-int (- matrix-size 10))))

(defn get-starting-position []
  "Builds a map of head and tail position represented by [x y] tuple"
  ; TODO: Use random values whenever possible
  (let [head-x (safe-random-position)
        head-y (- matrix-size (* 2 snake-initial-size))
        tail-x head-x
        tail-y (+ head-y (dec snake-initial-size))]
    {:head      [head-x head-y]
     :tail      [tail-x tail-y]
     :direction :left}))

(defn new-matrix []
  (matrix/zero-matrix matrix-size matrix-size))