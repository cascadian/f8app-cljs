(ns f8.tabs.info.section
  (:require [om.next :as om :refer [defui]]
            [react-native.components :refer [view]]
            [f8.common.f8-text :refer [text]]
            [react-native.api :refer [create-style-sheet]]))

(declare styles)

(defui Section
  Object
  (render [this]
    (let [{:keys [style title]} (om/props this)
          children (om/children this)]
      (view
        {:style [(styles :container) style]}
        (view
          {:style (styles :header)}
          (text
            {:style (styles :title)}
            title))
        children))))

(def section (om/factory Section))

(def styles
  (create-style-sheet
    {:container
      {:paddingTop 60
       :paddingBottom 0
       :backgroundColor :white}
     :header
      {:flexDirection :row
       :alignItems :center
       :justifyContent :center
       :marginBottom 30}
     :title
      {:fontSize 24
       :fontWeight :bold}}))
