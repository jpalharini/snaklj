(ns snaklj.views
  (:require [re-frame.core :as rf]
            [snaklj.config :as config]
            [snaklj.subs :as subs]
            [clojure.core.matrix :as matrix]))

(defn draw-board []
  (let [size    (* config/matrix-size 20)
        matrix* (rf/subscribe [::subs/matrix])]
    [:div {:style {:width  size
                   :height size
                   :padding "3px"
                   :border "1px solid #000"}}
     (for [line (range config/matrix-size)]
       [:p (interpose " " (matrix/mget @matrix* line))])]))

(defn main-panel []
  (let [name (rf/subscribe [::subs/name])]
    [:div
     [:h1
      "Hello from game " @name]
     (draw-board)]))