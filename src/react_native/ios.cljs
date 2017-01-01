(ns react-native.ios
  (:require [react-native.components :refer [create-element]]))

(def ReactNative (js/require "react-native"))

(def ActivityIndicatorIOS (.-ActivityIndicatorIOS ReactNative))
(def activity-indicator-ios (partial create-element ActivityIndicatorIOS))
(def TabBarIOS (.-TabBarIOS ReactNative))
(def tab-bar-ios (partial create-element TabBarIOS))
(def TabBarItemIOS (.-TabBarItemIOS ReactNative))
(def tab-bar-item-ios (partial create-element TabBarItemIOS))