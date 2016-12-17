(ns ^:figwheel-no-load env.windows.main
  (:require [om.next :as om]
            [f8.windows.core :as core]
            [f8.state :as state]
            [re-natal.support :as support]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :heads-up-display false
  :jsload-callback (fn []
                     (swap! state/app-state update :reload-count inc)
                     (om/add-root! state/reconciler core/AppRoot 1)
                     ;(.forceUpdate (om/class->any state/reconciler core/AppRoot))
                     ))

(core/init)

(def root-el (core/app-root))