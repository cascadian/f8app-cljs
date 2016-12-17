(ns f8.common.f8-button
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.components :refer [create-element image touchable-opacity view]]
            [react-native.api :refer [create-style-sheet]]
            [f8.common.f8-text :refer [text]]
            [f8.common.f8-colors :refer [light-text]]))

(declare styles)
(def linear-gradient (partial create-element (js/require "react-native-linear-gradient")))

(defui F8Button
  Object
  (render [this]
    (let [{:keys [type icon caption style onPress]} (om/props this)
          caption (.toUpperCase caption)
          icon (when icon (image {:source icon :style (styles :icon)}))
          content (if (= type "primary")
                    (linear-gradient {:start [0.5 1]
                                      :end [1 1]
                                      :colors ["#6A6AD5" "#6F86D9"]
                                      :style [(styles :button) (styles :primaryButton)]}
                                     icon
                                     (text {:style [(styles :caption) (styles :primaryCaption)]}
                                           caption))
                    (let [border (when (= type "bordered") (styles :border))]
                      (view
                        {:style [(styles :button) border]}
                        icon
                        (text
                          {:style [(styles :caption) (styles :secondaryCaption)]}
                          caption))))]
      (touchable-opacity
        {:accessibilityTraits "button"
         :onPress onPress
         :activeOpacity 0.8
         :style [(styles :container) style]}
        content)
      )))

(def f8-button (om/factory F8Button))

(def HEIGHT 50)

(def styles (create-style-sheet
              {:container {:height HEIGHT}
               :button {:flex 1
                        :flexDirection "row"
                        :alignItems "center"
                        :justifyContent "center"
                        :paddingHorizontal 40}
               :border {:borderWidth 1
                        :borderColor light-text
                        :borderRadius (/ HEIGHT 2)}
               :primaryButton {:borderRadius (/ HEIGHT 2)
                               :backgroundColor "transparent"}
               :icon {:marginRight 12}
               :caption {:letterSpacing 1
                         :fontSize 12}
               :primaryCaption {:color "white"}
               :secondaryCaption {:color light-text}}))

