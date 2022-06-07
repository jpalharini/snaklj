(ns snaklj.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [snaklj.events :as events]
            [snaklj.views :as views]
            [snaklj.config :as config]
            [snaklj.game.controller :as game]
            [snaklj.game.logic :as game.logic]
            [snaklj.subs :as subs]
            [snaklj.db :as db]
            [cljs.core.async :refer [go]]
            [clojure.core.matrix :as matrix]
            [re-pressed.core :as rp]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn- update-matrix! [matrix]
  (rf/dispatch [::db/update-matrix matrix]))

(defn cycle-game []
  (let [game-state*     (rf/subscribe [::subs/game-state])
        matrix*         (rf/subscribe [::subs/matrix])
        snake*          (rf/subscribe [::subs/snake])
        food-positions* (rf/subscribe [::subs/food-positions])
        msize           config/matrix-size]
    (when (= @game-state* :running)
      (game/move-snake! snake* matrix*)
      (go (game/maybe-produce-food! snake* food-positions*))
      (-> (matrix/zero-matrix msize msize)
          (game/draw-snake snake*)
          (game/draw-food food-positions*)
          (update-matrix!)))))

(defonce run-game (js/setInterval cycle-game config/game-speed))

(defn init []
  (rf/dispatch-sync [::events/initialize-db])
  (rf/dispatch-sync [::events/initialize-game])
  (rf/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])
  (rf/dispatch-sync [::rp/set-keydown-rules {:event-keys [[[:key/up] [{:keyCode 38}]]
                                                          [[:key/down] [{:keyCode 40}]]
                                                          [[:key/left] [{:keyCode 37}]]
                                                          [[:key/right] [{:keyCode 39}]]]
                                             :prevent-default-keys [{:keyCode 37}
                                                                    {:keyCode 38}
                                                                    {:keyCode 39}
                                                                    {:keyCode 40}]}])
  (go (run-game))
  (dev-setup)
  (mount-root))
