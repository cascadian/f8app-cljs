(ns f8.common.parallax-background
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.api :refer [create-style-sheet Animated Dimensions]]
            [react-native.components :refer [animated-image animated-view view image]]))

(def resolveAssetSource (js/require "resolveAssetSource"))
(declare styles)

(def HEIGHT
  (if (> (.-height (.get Dimensions "window") 600))
    200
    500))
(def SCREEN-WIDTH (.-width (.get Dimensions "window")))

(defui ParallaxBackground
  Object
  (initLocalState [this]
    {:shift (Animated.Value. (or (:backgroundShift (om/props this)) 0))})
  (componentDidUpdate [this prev-props prev-state]
    (let [backgroundShift (:backgroundShift (om/props this))
          shift (om/get-state :shift)
          previous-backgroundShift (:backgroundShift prev-props)]
      (if (not= backgroundShift previous-backgroundShift)
        (.start (.timing Animated shift #js {:toValue backgroundShift :duration 300})))))
  (render [this]
    (let [{:keys [minHeight maxHeight offset backgroundColor]} (om/props this)
          buffer 10
          inputRange #js [0 (- maxHeight minHeight)]
          outputRange #js [(+ maxHeight buffer) (+ minHeight buffer)]
          height (.interopolate offset
                                #js {:inputRange inputRange
                                     :outputRange outputRange
                                     :extrapolateRight "clamp"})
          ]
      (animated-view
        {:style [(styles :container) {:height height :backgroundColor backgroundColor}]}
        (.render-background-image this)
        (.render-content this))))
  (render-background-image [this]
    (let [{:keys [backgroundImage minHeight maxHeight offset]} (om/props this)]
      (cond
        (not backgroundImage) nil
        (not (resolveAssetSource backgroundImage)) nil
        :else (let [source (resolveAssetSource backgroundImage)
                    width (.-width source)
                    translate-x (.interpolate (om/get-state this :shift)
                                              #js {:inputRange #js [0 1]
                                                   :outputRange #js [0 (- SCREEN-WIDTH width)]
                                                   :extrapolate "clamp"})
                    length (- maxHeight minHeight)
                    translate-y (.interpolate offset
                                              #js {:inputRanage #js [0 (/ length 2) length]
                                                   :outputRange #js [0 (/ (- length) 2) (/ (- length) 1.5)]
                                                   :extrapolate "clamp"})
                    initial-scale (js/Math.max (-> SCREEN-WIDTH
                                                  (/ width)
                                                  (* 2)
                                                  (- 1)) 1)
                    scale (.interpolate offset #js {:inputRange #js [(- length) 0]
                                                    :outputRange #js [2, initial-scale]
                                                    :extrapolate "clamp"})
                    transforms {:transform [{:translateX translate-x
                                             :translateY translate-y
                                             :scale scale}]}]
                (animated-image
                  {:source backgroundImage
                   :style transforms})))))
  (render-content [this]
    (when (not= (count (om/children this)) 0)
      (let [content (js/React.Children.only (om/children this))
            {:keys [minHeight maxHeight offset]} (om/props this)
            length (- maxHeight minHeight)
            opacity (.interpolate offset #js {:inputRange  #js [0 (- length 40)]
                                              :outputRange #js [1 0]
                                              :extrapolate "clamp"})
            translate-y (.interpolate offset #js {:inputRange #js [0 length]
                                                  :outputRange #js [-32 (- (- (/ length 2)) 32)]
                                                  :extrapolate "clamp"})
            transforms {:opacity opacity
                        :transform [{:translateY translate-y}]}]
        (animated-view
          {:style [(styles :contentContainer) transforms]}
          content)))))

(def parallax-background (om/factory ParallaxBackground))

(def HEADER-HEIGHT (+ HEIGHT 156))

(def styles (create-style-sheet
              {:container
                {:position "absolute"
                 :left 0
                 :top 0
                 :right 0
                 :overflow "hidden"}
               :contentContainer
                {:position "absolute"
                 :left 0
                 :top 0
                 :right 0
                 :height HEADER-HEIGHT
                 :alignItems "center"
                 :justifyContent "center"
                 :backgroundColor "transparent"}}))