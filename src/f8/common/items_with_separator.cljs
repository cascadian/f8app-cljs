(ns f8.common.items-with-separator
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.api :refer [PixelRatio create-style-sheet]]
            [react-native.components :refer [view]]))

(declare styles)

(defui ItemsWithSeparator
  Object
  (render [this]
    (let [{:keys [separatoryStyle style]} (om/props this)
          children (om/children this)
          separators (map
                       #((view
                           {:style [(styles :separator) separatoryStyle]
                            :key (str "separator-" %)}))
                       (range (count children)))
          child-elements
          (butlast (interleave
                     children
                     separators))])))

(def items-with-separator (om/factory ItemsWithSeparator))

(def styles (create-style-sheet
              {:separator
                {:backgroundColor "#0322500A"
                 :height (/ 1 (.get PixelRatio))}}))
