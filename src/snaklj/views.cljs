(ns snaklj.views
  (:require [clojure.core.matrix :as matrix]
            [re-frame.core :as rf]
            [snaklj.config :as config]
            [snaklj.game.state]
            [snaklj.subs :as subs]))

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
                                              :top              (* x block-size)
                                              :left             (* y block-size)
                                              :background-color "#555"
                                              :position         "absolute"}}]))]))

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