(ns f8.common.f8-drawer-layout
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.android :refer [DrawerLayoutAndroid drawer-layout-android]]))

(defui F8DrawerLayout
  Object
  (render [this]
    (let [{:keys [drawerPosition] :as props} (om/props this)
          right-position (.. DrawerLayoutAndroid -positions -Right)
          left-position (.. DrawerLayoutAndroid -positions -Left)]
      (drawer-layout-android
        (merge props
               {:ref #(set! (.-_drawer this) %)
                :drawerPosition (if (= drawerPosition "right") right-position left-position)
                :onDrawerOpen #(.onDrawerOpen this)
                :onDrawerClose #(.onDrawerClose this)}))))
  (componentWillUnmount [this]
    (let [removeBackButtonListener (om/get-computed this :removeBackButtonListener)]
      (removeBackButtonListener (.-handleBackButton this)))
    (set! (.-_drawer this) nil))
  (handleBackButton [this]
    (.closeDrawer this)
    true)
  (onDrawerOpen [this]
    (let [addBackButtonListener (om/get-computed this :addBackButtonListener)
          {:keys [onDrawerOpen]} (om/props this)]
      (addBackButtonListener (.-handleBackButton this))
      (when onDrawerOpen (onDrawerOpen))))
  (onDrawerClose [this]
    (let [removeBackButtonListener (om/get-computed this :removeBackButtonListener)
          {:keys [onDrawerClose]} (om/props this)]
      (removeBackButtonListener (.-handleBackButton this))
      (when onDrawerClose (onDrawerClose))))
  (closeDrawer [this]
    (let [drawer (.-_drawer this)]
      (when drawer (.closeDrawer drawer))))
  (openDrawer [this]
    (let [drawer (.-_drawer this)]
      (when drawer (.openDrawer drawer)))))

(def f8-drawer-layout (om/factory F8DrawerLayout))