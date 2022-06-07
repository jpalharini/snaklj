(ns snaklj.views
  (:require [re-frame.core :as rf]
            [snaklj.config :as config]
            [snaklj.subs :as subs]
            [snaklj.db :as db]
            [clojure.core.matrix :as matrix]))

(defn draw-board [matrix]
  (let [block-size 20
        board-size (* config/matrix-size block-size)]
    [:div {:style {:width    board-size
                   :height   board-size
                   :border   "1px solid #000"
                   :position "relative"}}
     (for [x (range config/matrix-size)
           y (range config/matrix-size)]
       (if (> (matrix/mget matrix x y) 0)
         ^{:key (str x ":" y)} [:div {:style {:width            block-size
                                              :height           block-size
                                              :background-color "#555"
                                              :position         "absolute"
                                              :top              (* x block-size)
                                              :left             (* y block-size)}}]))]))

(defn main-panel []
  (let [name    (rf/subscribe [::subs/name])
        matrix* (rf/subscribe [::subs/matrix])]
    (rf/dispatch [::db/update-game-state :running])
    [:div
     [:h1
      "Hello from game " @name]
     (draw-board @matrix*)]))