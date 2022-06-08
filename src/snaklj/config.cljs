(ns snaklj.config
  (:require [clojure.core.matrix :as matrix]))

(def debug? true)

(def matrix-size 30)

(def snake-initial-size 3)

(def game-speed 500)

(def food-probability-perc 5)

(defn new-matrix []
  (matrix/zero-matrix matrix-size matrix-size))