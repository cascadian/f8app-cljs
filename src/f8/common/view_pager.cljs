(ns f8.common.view-pager
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.components :refer [view scroll-view view-pager-android]]
            [react-native.api :refer [ios? create-style-sheet]]))

(declare styles)

(defui ViewPager
  Object
  (initLocalState [this]
    (let [{:keys [selectedIndex]} (om/props this)]
      {:width  0
       :height 0
       :selectedIndex selectedIndex
       :initialSelectedIndex selectedIndex}))
  (render [this]
    (if (ios?)
      (.render-ios this)
      (.render-android this)))
  (render-ios [this]
    (let [{:keys [style bounces]} (om/props this)
          {:keys [width height selectedIndex initialSelectedIndex]} (om/get-state this)]
      (scroll-view
        {:ref           "scrollview"
         :contentOffset {:x (* width initialSelectedIndex)
                         :y 0}
         :style [(styles :scrollview ) style]
         :horizontal true
         :pagingEnabled true
         :bounces (not (not bounces))
         :scrollsToTop false
         :scrollEventThrottle 100
         :removeClippedSubviews true
         :automaticallyAdjustContentInsets false
         :directionalLockEnabled true
         :showsHorizontalScrollIndicator false
         :showsVerticalScrollIndicator false
         :onLayout (.-adjustCardSize this)}
        (.render-content this))))
  (render-android [this]
    (let [{:keys [width initialSelectedIndex]} (om/props this)]
      (view-pager-android
        {:ref "scrollview"
         :initialPage initialSelectedIndex
         :onPageSelected (.-handleHorizontalScroll this)
         :style (styles :container)}
        (.render-content this))))
  (adjustCardSize [this e]
    (om/set-state! this {:width (.. e -nativeEvent -layout -width)
                         :height (.. e -nativeEvent -layout -height)}))
  (componentWillReceiveProps [this {:keys [selectedIndex]}]
    (when (not= selectedIndex (om/get-state this :selectedIndex))
      (let [scrollview (om/react-ref this "scrollview")]
        (if (ios?)
          (do
            (.scrollTo scrollview
                       #js {:x        (* selectedIndex (om/get-state this :width))
                            :animated true})
            (om/set-state! this {:scrollingTo selectedIndex}))
          (do
            (.setPage scrollview selectedIndex)
            (om/set-state! this {:selectedIndex selectedIndex}))))))
  (render-content [this]
    (let [{:keys [width height]} (om/get-state this)
          style (if (ios?) (styles :card))]
      (map-indexed
        (fn [i child]
          (view
            {:style [style {width height}]
             :key (str "r_" i)}
            child))
        (om/children this))))
  (handleHorizontalScroll [this e]
    (let [{:keys [selectedIndex count onSelectedIndexChange]} (om/props this)
          {:keys [scrollingTo width]} (om/get-state this)
          new-selected-index (.. e -nativeEvent -position)
          new-selected-index (if (undefined? new-selected-index)
                           (js/Math.round (/ (.. e -nativeEvent -contentOffset -x) width))
                           new-selected-index)]
      (cond
        (or (< new-selected-index 0) (>= new-selected-index count))
          nil
        (and (some? scrollingTo) (not= new-selected-index scrollingTo))
          nil
        (or (not= selectedIndex new-selected-index) (some? scrollingTo))
          (do
            (om/set-state! this {:selectedIndex new-selected-index :scrollingTo nil})
            (when onSelectedIndexChange (onSelectedIndexChange new-selected-index)))
        :else nil
        ))))

(def view-pager (om/factory ViewPager))

(def styles (create-style-sheet
              {:container {:flex 1}
               :scrollview {:flex 1
                            :backgroundColor "transparent"}
               :card {:backgroundColor "transparent"}}))