(ns f8.common.f8-text
  (:require [react-native.api :refer [create-style-sheet Dimensions]]
            [react-native.components :as c]
            [f8.env :refer [env]]
            [f8.common.f8-colors :refer [dark-text light-text]]))

(def React (js/require "React"))
(declare styles)
(defn text [props & children]
  (c/text (update props :style #(vector (styles :font) %))
          children))

(defn heading1 [props & children]
  (c/text (update props :style #(vector (styles :font) (styles :h1) %))
          children))

(defn paragraph [props & children]
  (c/text (update props :style #(vector (styles :font) (styles :p) %))
          children))

(def scale (/ (.-width (.get Dimensions "window")) 375))

(defn normalize [size]
  (js/Math.round (* scale size)))

(def styles
  (create-style-sheet
    {:font {:fontFamily (:fontFamily env)}
     :h1 {:fontSize (normalize 24)
          :lineHeight (normalize 27)
          :color (:dark-text dark-text)
          :fontWeight "bold"
          :letterSpacing -1}
     :p {:fontSize (normalize 15)
         :lineHeight (normalize 23)
         :color light-text}}))
