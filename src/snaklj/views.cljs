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
  (let [matrix*     (rf/subscribe [::subs/matrix])
        game-state* (rf/subscribe [::subs/game-state])
        snake-size* (rf/subscribe [::subs/snake-size])]
    [:div
     [:h1 "SNAKLJ"]
     [:p "The famous Snake game, in ClojureScript + Re-Frame"]
     (draw-board @matrix*)
     [:a [:i.action-button {:class    (case @game-state*
                                        :running "bi bi-pause-circle-fill"
                                        "bi bi-play-circle-fill")
                            :on-click #(rf/dispatch-sync [:game/change-state])}]]
     [:p [:b "Snake size: "] @snake-size*]
     (when (= @game-state* :lost)
       [:p "You crashed! Press " [:i {:class "bi bi-play-circle-fill"}] " to restart."])]))