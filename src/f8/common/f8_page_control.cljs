(ns f8.common.f8-page-control
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.api :refer [create-style-sheet]]
            [react-native.components :refer [view]]))

(declare circle styles)

(defui F8PageControl
  Object
  (render [this]
    (let [{:keys [count selectedIndex style]} (om/props this)
          images (map
                   #(circle
                      {:key %
                       :isSelected (= selectedIndex %)})
                   (range count))]
      (view
        {:style [(styles :container) style]}
        (view
          {:style (styles :innerContainer)}
          images)))))

(def f8-page-control (om/factory F8PageControl))

(defui Circle
  Object
  (render [this]
    (let [props (om/props this)
          isSelected (:isSelected props)
          extraStyle (if isSelected (styles :full) (styles :empty))]
      (view
        {:style [(styles :circle) extraStyle]}))))

(def circle (om/factory Circle))

(def CIRCLE-SIZE 4)

(def styles (create-style-sheet
              {:container
                {:alignItems "center"
                 :justifyContent "center"}
               :innerContainer
                {:flexDirection "row"}
               :circle
                {:margin 2
                 :width CIRCLE-SIZE
                 :height CIRCLE-SIZE
                 :borderRadius (/ CIRCLE-SIZE 2)}
               :full
                {:backgroundColor "#fff"}
               :empty
                {:backgroundColor "#fff5"}}))