(ns f8.ios.core
  (:require [f8.main :as main]
            [f8.state :as state]
            #_ [f8.ios.f8-tabs-view :refer [F8TabsView f8-tabs-view]]
            ))

(def AppRoot main/AppRoot)
(defonce app-root main/app-root)

#_ (swap! state/shared-state assoc-in [:platform-specific-components :tabs-view] f8-tabs-view)

(defn init []
  (main/init))