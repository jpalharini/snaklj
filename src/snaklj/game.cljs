(ns snaklj.game
  (:require [re-frame.core :as rf]
            [snaklj.subs :as subs]
            [clojure.core.matrix :as matrix]))

(defn get-next-position [current-position direction]
  (let [[x y] current-position]
    (case direction
      :up [(dec x) y]
      :down [(inc x) y]
      :left [x (inc y)]
      :right [x (dec y)])))

(defn draw-snake
  [matrix {:keys [head direction curves]}]
  (let [size* (rf/subscribe [::subs/snake-size])]
    (loop [positions        0
           current-matrix   matrix
           current-position head]
      (if (< positions @size*)
        (let [[x y :as next] (get-next-position current-position direction)]
          (recur (inc positions)
                 (matrix/mset current-matrix x y 1)
                 next))
        current-matrix))))