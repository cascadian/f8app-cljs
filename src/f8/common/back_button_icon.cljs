(ns f8.common.back-button-icon
  (:require [react-native.api :refer [android? ios?]]))

(def back-button-icon
  (if (ios?)
    (js/require "./images/common/x-white.png")
    (js/require "./images/common/back_white.png")))