(ns f8.f8-navigator
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.api :as api :refer [android? create-style-sheet]]
            [react-native.components :as c
             :refer [view Navigator navigator text status-bar image]]))
(declare styles)

(defui F8Navigator
  Object
  (render [this]
    (if-let [tabs-view (om/shared this [:platform-specific-components :tabs-view])]
      (do
        (tabs-view (om/props this)))
      (throw (js/Error. "no tabs view configured for this platform")))))

(def f8-navigator (om/factory F8Navigator))

(def styles (create-style-sheet
              {:container
               {:flex            1
                :backgroundColor "black"}}))