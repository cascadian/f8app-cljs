(ns react-native.ios
  (:require [react-native.components :refer [create-element]]))

(def ReactNative (js/require "react-native"))

(def ActivityIndicatorIOS (.-ActivityIndicatorIOS ReactNative))
(def activity-indicator-ios (partial create-element ActivityIndicatorIOS))
(def TabBarIOS (js/require "TabBarIOS"))
(def tab-bar-ios (partial create-element TabBarIOS))
(def TabBarItemIOS (js/require "TabBarItemIOS"))
(def tab-bar-item-ios (partial create-element TabBarItemIOS))