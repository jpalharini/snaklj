(ns snaklj.core
  (:require [clojure.core.matrix :as matrix]
            [cljs.core.async :refer [go]]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [re-pressed.core :as rp]
            [snaklj.db :as db]
            [snaklj.events :as events]
            [snaklj.config :as config]
            [snaklj.game.controller :as game]
            [snaklj.subs :as subs]
            [snaklj.views :as views]))

(defn- update-matrix! [matrix]
  (rf/dispatch [::db/update-matrix matrix]))

(defn cycle-game []
  (let [game-state*    (rf/subscribe [::subs/game-state])
        matrix*        (rf/subscribe [::subs/matrix])
        snake*         (rf/subscribe [::subs/snake])
        food-position* (rf/subscribe [::subs/food-position])
        msize          config/matrix-size]
    (when (= @game-state* :running)
      (game/move-snake! snake* matrix*)
      (-> (matrix/zero-matrix msize msize)
          (game/draw-snake snake*)
          (game/draw-food food-position*)
          (update-matrix!)))))

(defonce run-game (js/setInterval cycle-game config/game-speed))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (rf/dispatch-sync [::events/initialize-db])
  (rf/dispatch-sync [::events/initialize-game])
  ; Using re-pressed to monitor keyboard input
  (rf/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])
  (rf/dispatch-sync [::rp/set-keydown-rules {:event-keys           [[[:key/up] [{:keyCode 38}]]
                                                                    [[:key/down] [{:keyCode 40}]]
                                                                    [[:key/left] [{:keyCode 37}]]
                                                                    [[:key/right] [{:keyCode 39}]]]
                                             :prevent-default-keys [{:keyCode 37}
                                                                    {:keyCode 38}
                                                                    {:keyCode 39}
                                                                    {:keyCode 40}]}])
  ; (go) runs game in parallel
  (go (run-game))
  (dev-setup)
  (mount-root))
