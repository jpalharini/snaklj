(ns snaklj.config
  (:require [clojure.core.matrix :as matrix]))

(def debug? true)

(def matrix-size 30)

(def snake-initial-size 3)

(def game-speed 1000)

(def food-probability-perc 10)

(defn new-matrix []
  (matrix/zero-matrix matrix-size matrix-size))