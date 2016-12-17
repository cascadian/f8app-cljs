(ns f8.common.map-view
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.api :refer [PixelRatio InteractionManager create-style-sheet]]
            [react-native.components :refer [Image image view]]))

(declare styles urlForMap)

(defui MapView
  Object
  (initLocalState [this]
    (set! (.-_isMounted this) false)
    {:loaded false})
  (componentDidMount [this]
    (set! (.-_isMounted this) true)
    (.runAfterInteractions InteractionManager
                           #(and (.-_isMounted this)
                                 (om/set-state! this {:loaded true}))))
  (componentWillUnmount [this]
    (set! (.-_isMounted this) false))
  (render [this]
    (let [image (when (om/get-state this :loaded)
                  (image
                    {:style (styles :map)
                     :source {:uri (urlForMap (:map (om/props this)))}}))]
      (view
        {:style [(styles :container) (:style (om/props this))]}))))

(def map-view (om/factory MapView))

(defn urlForMap [map]
  (if (not map)
    ""
    (let [pixel-ratio (.get PixelRatio)]
      (case pixel-ratio
        1 (.-x1url map)
        2 (.-x2url map)
        3 (.-x3url map)
        (.-x3url map)))))

(def styles
  (create-style-sheet
    {:container
      {:backgroundColor "white"
       :height 400}
     :map
      {:flex 1
       :resizeMode (.. Image -resizeMode -contain)}}))