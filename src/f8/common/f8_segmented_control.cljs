(ns f8.common.f8-segmented-control
  (:require [om.next :as om :refer-macros [defui]]
            [f8.common.f8-style-sheet :refer [create-style-sheet]]
            [react-native.api :refer [android? ios?]]
            [react-native.components :refer [view text touchable-opacity]]))
(declare styles segment)

(defui F8SegmentedControl
  Object
  (render [this]
    (let [{:keys [values selectedIndex selectionColor onChange style]} (om/props this)
          segments (map-indexed
                     (fn [index value]
                       (segment
                         {:key value
                          :value value
                          :isSelected (= index selectedIndex)
                          :selectionColor (or selectionColor "white")
                          :onPress #(onChange index)}))
                     values)]
      (view
        {:style [(styles :container) style]}))))

(def f8-segmented-control (om/factory F8SegmentedControl))

(defui Segment
  Object
  (render [this]
    (let [{:keys [onPress isSelected value selectionColor]} (om/props this)
          selectedButtonStyle (when isSelected {:borderColor selectionColor})
          deselectedLabelStyle (when (and isSelected (android?)) (styles :deslectedLabel))
          title (when value (.toUpperCase value))
          accessibilityTraits (cond-> ["button"]
                                      isSelected (conj "selected"))]
      (touchable-opacity
        {:accessibilityTraits accessibilityTraits
         :activeOpacity 0.8
         :onPress onPress
         :style [(styles :button) selectedButtonStyle]}
        (text
          {:style [(styles :label) deselectedLabelStyle]}
          title)))))

(def segement (om/factory Segment))

(def HEIGHT 32)

(def styles (create-style-sheet
              {:container
                {:flexDirection "row"
                 :backgroundColor "transparent"
                 :ios
                    {:paddingBottom 6
                     :justifyContent "center"
                     :alignItems "center"}
                 :android
                    {:paddingLeft 60}
                 :windows {:paddingLeft 60}}
               :button
                {:borderColor "transparent"
                 :alignItems "center"
                 :justifyContent "center"
                 :backgroundColor "transparent"
                 :ios
                  {:height HEIGHT
                   :paddingHorizontal 20
                   :borderRadius (/ HEIGHT 2)
                   :borderWidth 1}
                 :android
                  {:paddingBottom 6
                   :paddingHorizontal 10
                   :borderBottomWidth 3
                   :marginRight 10}
                 :windows
                 {:paddingBottom 6
                  :paddingHorizontal 10
                  :borderBottomWidth 3
                  :marginRight 10}}
               :label
                {:letterSpacing 1
                 :fontSize 12
                 :color "white"}
               :deselectedLabel
                {:color "rgba(255, 255, 255, 0.7)"}}))