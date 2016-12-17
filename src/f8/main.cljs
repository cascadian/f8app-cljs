(ns f8.main
  (:require [om.next :as om :refer-macros [defui]]
            [re-natal.support :as sup]
            [react-native.components :refer [view text]]
            [f8.f8-navigator :refer [f8-navigator]]
            [f8.state :as state]))

(set! js/React (js/require "react"))
(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))

(def logo-img (js/require "./images/cljs.png"))

(defui ^:once AppRoot
  static om/IQuery
  (query [this]
    '[:app/msg
      :reload-count])
  Object
  (render [this]
    (view
      {:style {:flex 1}}
      (f8-navigator (om/props this)))))

(defonce RootNode (sup/root-node! 1))
(defonce app-root (om/factory RootNode))

(defn init []
  (om/add-root! state/reconciler AppRoot 1)
  (.registerComponent app-registry "F8" (fn [] app-root)))
