(ns f8.common.list-container
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.api :refer [platform-os ios? android? windows? Animated Dimensions NativeModules findNodeHandle]]
            [react-native.components :refer [view animated-text animated-view]]
            [f8.common.f8-header :refer [header HEADER-HEIGHT]]
            [f8.common.f8-segmented-control :refer [f8-segmented-control]]
            [f8.common.parallax-background :refer [parallax-background]]
            [f8.common.f8-style-sheet :refer [create-style-sheet]]
            [f8.common.f8-text :refer [text]]
            [f8.common.view-pager :refer [view-pager]]
            [react-native.android :refer [progress-bar-android]]
            [react-native.ios :refer [activity-indicator-ios]]
            [react-native.windows :refer [progress-ring-windows]]))

(declare styles)

(def EMPTY-CELL-HEIGHT (if (> (.-height (.get Dimensions "window")) 600) 200 150))

(def activity-indicator (condp = platform-os
                          :ios activity-indicator-ios
                          :android progress-bar-android
                          :windows progress-ring-windows))

(defui ListItemLoading
  Object
  (render [this]
    (let [child (first (om/children this))]
      ; TODO imitate RelayLoading component
      (.render-child this child {})))
  (render-child [this child props]
    (if (not props)
      (view
        {:style {:height 400}
         (when-let [renderHeader (:renderHeader (om/props child))]
           (renderHeader))
                (view
                  {:style {:flex            1
                           :backgroundColor "white"
                           :alignItems      "center"
                           :justifyContent  "center"}}
                  (activity-indicator))})
      (js/React.cloneElement child (merge (om/props this) props)))))

(def list-item-loading (om/factory ListItemLoading))

(defui ListContainer
  Object
  (initLocalState [this]
    (set! (.-_refs this) [])
    {:idx                (or (:selectedSegment (om/props this)) 0)
     :anim               (Animated.Value. 0)
     :stickyHeaderHeight 0})
  (render [this]
    (let [{:keys [leftItem hasUnreadNotifications stickyHeader
                  selectedSectionColor backgroundImage backgroundColor title
                  rightItem extraItems]} (om/props this)
          leftItem (cond-> leftItem
                           (and (not leftItem) (or (android?) (windows?)))
                           {:title   "Menu"
                            :icon    (if hasUnreadNotifications (js/require "./images/common/hamburger-unread.png")
                                                                (js/require "./images/common/hamburger.png"))
                            :onPress #(.handleShowMenu this)})
          children (om/children this)
          segments (mapv #(:title (om/props %)) children)
          idx (om/get-state this :idx)
          sticky-header-height (om/get-state this :stickyHeaderHeight)
          sticky-header (when stickyHeader
                          (view
                            nil
                            (f8-segmented-control
                              {:values         segments
                               :selectedIndex  idx
                               :selectionColor selectedSectionColor
                               :onChange       #(.handleSelectSegment this)}
                              stickyHeader)))
          segment-count (count segments)
          backgroundShift (if (= 1 segment-count)
                            0
                            (/ idx (dec segment-count)))]
      (view
        {:style (styles :container)}
        (view
          {:style (styles :headerWrapper)}
          (parallax-background
            {:minHeight       (+ sticky-header-height HEADER-HEIGHT)
             :maxHeight       (+ EMPTY-CELL-HEIGHT sticky-header-height HEADER-HEIGHT)
             :offset          (om/get-state this :anim)
             :backgroundImage backgroundImage
             :backgroundColor backgroundColor}
            (.renderParallaxContent this))
          (header
            {:title      title
             :leftItem   leftItem
             :rightItem  rightItem
             :extraItems extraItems}
            (.renderHeaderTitle this))
          (.renderFixedStickyHeader this sticky-header))
        (view-pager
          {:count                 segment-count
           :selectedIndex         idx
           :onSelectedIndexChange #(.handleSelectSegment this)}
          children)
        (.renderFloatingStickyHeader this sticky-header))))
  (renderParallaxContent [this]
    (let [{:keys [parallaxContent title]} (om/props this)]
      (condp = platform-os
        :android (view nil)
        :windows (view nil)
        (if parallaxContent
          parallaxContent
          (text
            {:style (styles :parallaxText)}
            title)))))
  (renderHeaderTitle [this]
    (when (ios?)
      (let [{:keys [parallaxContent title]} (om/props this)
            transform (when (not parallaxContent)
                        (let [distance (- EMPTY-CELL-HEIGHT (om/get-state this :stickyHeaderHeight))]
                          {:opacity (.interpolate (om/get-state this :anim)
                                                  #js {:inputRange  #js [(- distance 20) distance]
                                                       :outputRange #js [0 1]
                                                       :extrapolate :clamp})}))]
        (animated-text
          {:style [(styles :headerTitle) transform]}
          title))))

  (handleScroll [this idx e]
    (when (= idx (om/get-state this :idx))
      (let [y (cond-> 0
                      (ios?) (do (let [height (- EMPTY-CELL-HEIGHT (om/get-state this :stickyHeaderHeight))
                                       anim (om/get-state this :anim)
                                       y-offset (.. e -nativeEvent -contentOffset -y)]
                                   (.setValue anim y-offset)
                                   (js/Math.min y-offset height))))
            scroll-fn (fn [ii ref]
                        (when (and (not= ii idx) ref)
                          (when (.-scrollTo ref)
                            (.scrollTo ref #js {:y y :animated false}))))]
        (doall (map-indexed scroll-fn (.-_refs this))))))

  (renderFakeHeader [this]
    (when (ios?)
      (view
        {:style {:height (- EMPTY-CELL-HEIGHT (om/get-state this :stickyHeaderHeight))}})))

  (renderFixedStickyHeader [this stickyHeader]
    (if (ios?)
      (view
        {:style {:height (om/get-state this :stickyHeaderHeight)}})
      stickyHeader))

  (renderFloatingStickyHeader [this stickyHeader]
    (when (and stickyHeader (ios?))
      (let [{:keys [stickyHeaderHeight anim]} (om/get-state this)
            opacity (if (= stickyHeaderHeight 0) 0 1)
            transform (when (not (.-F8Scrolling NativeModules))
                        (let [distance (- EMPTY-CELL-HEIGHT stickyHeaderHeight)
                              translateY (.interpolate anim #js {:inputRange       #js [0 distance]
                                                                 :outputRange      #js [distance 0]
                                                                 :extrapolateRight :clamp})]
                          [{:translateY translateY}]))]
        (animated-view
          {:ref      #(set! (.-_pinned this) %)
           :onLayout #(.handleStickyHeaderLayout this)
           :style    [(styles :stickyHeader) {:opacity opacity} {:transform transform}]}
          stickyHeader))))

  (handleStickyHeaderLayout [this e]
    (om/set-state! this {:stickyHeaderHeight (.. e -nativeEvent -layout -height)}))

  (componentWillReceiveProps [this {:keys [selectedSegment]}]
    (when (and (number? selectedSegment)
               (not= selectedSegment (om/get-state this :idx)))
      (om/set-state! this {:idx selectedSegment})))

  (componentDidUpdate [this prev-props prev-state]
    (when-let [Scrolling (.-F8Scrolling NativeModules)]
      (let [prev-idx (:idx prev-state)
            current-idx (om/get-state this :idx)
            prev-header-height (:stickyHeaderHeight prev-state)
            current-header-height (om/get-state this :stickyHeaderHeight)
            refs (.-_refs this)
            prev-ref (get refs prev-idx)
            current-ref (get refs current-idx)
            find-ref-node-handle (fn [ref]
                                   (when ref
                                     (when-let [getScrollResponder (.-getScrollResponder ref)]
                                       (findNodeHandle getScrollResponder))))]
        (when (or (not= current-idx prev-idx)
                  (not= current-header-height prev-header-height))
          (when-let [oldScrollViewTag (find-ref-node-handle prev-ref)]
            (.unpin Scrolling oldScrollViewTag))
          (when-let [newScrollViewTag (find-ref-node-handle current-ref)]
            (let [pinnedViewTag (findNodeHandle (.-_pinned this))
                  distance (- EMPTY-CELL-HEIGHT prev-header-height)]
              (.pin Scrolling newScrollViewTag pinnedViewTag distance))))

        ; moved from handleSelectSegment
        (when (not= current-idx prev-idx)
          (if-let [onSegmentChange (:onSegmentChange (om/props this))]
            (onSegmentChange))))))

  (handleSelectSegment [this idx]
    ; NOTE callback function moved to componentDidUpdate
    (om/set-state! this {:idx idx}))

  (handleShowMenu [this]
    (let [openDrawer (om/get-computed this :openDrawer)]
      (openDrawer))))

(def list-container (om/factory ListContainer))

(def styles
  (create-style-sheet
    {:container     {:flex            1
                     :backgroundColor :white}
     :headerWrapper {:android {:elevation        2
                               :backgroundColor  :transparent
                               :borderRightWidth 1
                               :marginRight      -1
                               :borderRightColor :transparent}}
     :listView      {:ios     {:backgroundColor :transparent}
                     :android {:backgroundColor :white}
                     :windows {:backgroundColor :white}}
     :headerTitle   {:color      :white
                     :fontWeight :bold
                     :fontSize   20}
     :parallaxText  {:color         :white
                     :fontSize      42
                     :fontWeight    :bold
                     :letterSpacing -1}
     :stickyHeader  {:position :absolute
                     :top      HEADER-HEIGHT
                     :left     0
                     :right    0}}))