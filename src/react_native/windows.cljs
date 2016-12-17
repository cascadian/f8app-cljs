(ns react-native.windows
  (:require [react-native.components :refer [create-element]]))

(def ReactNative (js/require "react-native-windows"))

(def SplitViewWindows (.-SplitViewWindows ReactNative))
(def split-view-windows (partial create-element SplitViewWindows))
(def ProgressRingWindows (.-ProgressRingWindows ReactNative))
(def progress-ring-windows (partial create-element ProgressRingWindows))
