(ns f8.common.f8-touchable
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.android :refer [touchable-native-feedback]]
            [react-native.api :refer [android?]]
            [react-native.components :refer [touchable-highlight]]))

(defn f8-touchable-ios [props]
  (touchable-highlight
    (merge
      {:accessibilityTraits "button"
       :underlayColor "#3C5EAE"}
      props)))

(def f8-touchable (if (android?) touchable-native-feedback f8-touchable-ios))