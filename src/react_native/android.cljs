(ns react-native.android
  (:require [react-native.components :refer [create-element]]))

(def ReactNative (js/require "react-native"))

(def DrawerLayoutAndroid (.-DrawerLayoutAndroid ReactNative))
(def drawer-layout-android (partial create-element DrawerLayoutAndroid))
(def toolbar-android (partial create-element (.-ToolbarAndroid ReactNative)))
(def touchable-native-feedback (partial create-element (.-TouchableNativeFeedback ReactNative)))
(def progress-bar-android (partial create-element (.-ProgressBarAndroid ReactNative)))
(def ToastAndroid (.-ToastAndroid ReactNative))