(ns snaklj.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [snaklj.events :as events]
            [snaklj.views :as views]
            [snaklj.config :as config]))

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
  (dev-setup)
  (mount-root))
