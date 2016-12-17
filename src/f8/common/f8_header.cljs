(ns f8.common.f8-header
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.components :refer [view text image touchable-opacity]]
            [react-native.api :refer [ios? android? windows? create-style-sheet]]
            [react-native.android :refer [toolbar-android]]
            [f8.common.f8-colors :refer [dark-text light-text]]))
(declare styles)
(defui F8HeaderAndroid
  Object
  (render [this]
    (let [{:keys [leftItem rightItem extraItems foreground style] :as props} (om/props this)
          {:keys [title icon layout]} rightItem
          actions (cond-> []
                          rightItem (conj {:icon  (if (not= layout "title") icon js/undefined)
                                           :title title
                                           :show  "always"})
                          extraItems (into (map #({:title (:title %)
                                                   :show  "never"}) extraItems)))
          textColor (if (= foreground "dark")
                      dark-text
                      "white")
          children (om/children this)
          content (when (> (count children) 0)
                    (view
                      {:collapsable false
                       :style       {:flex 1}}
                      children))]
      (view
        {:style [(styles :toolbarContainer) style]}
        (toolbar-android
          {:navIcon          (when leftItem (:icon leftItem))
           :onIconClicked    (when leftItem (:onPress leftItem))
           :title            (:title props)
           :titleColor       textColor
           :subtitleColor    textColor
           :actions          actions
           :onActionSelected (.-handleActionSelected this)
           :style            (styles :toolbar)}
          content)))))

(def f8-header-android (om/factory F8HeaderAndroid))

(declare item-wrapper-ios)

(defui F8HeaderIOS
  Object
  (render [this]
    (let [{:keys [leftItem title rightItem foreground style] :as props} (om/props this)
          titleColor (if (= foreground :dark)
                       dark-text
                       :white)
          itemsColor (if (= foreground :dark)
                       light-text
                       :white)

          children (om/children this)
          content (if (= (count children) 0)
                    (text
                      {:style [(styles :titleText) {:color titleColor}]}
                      title)
                    children)]
      (view
        {:style [(styles :header) style]}
        (view
          {:style (styles :leftItem)}
          (item-wrapper-ios
            {:color itemsColor
             :item leftItem}))
        (view
          {:accessible true
           :accessibilityLabel title
           :accessibilityTraits "header"
           :style (styles :centerItem)}
          content)
        (view
          {:style (styles :rightItem)}
          (item-wrapper-ios
            {:color itemsColor
             :item rightItem}))))))

(def f8-header-ios (om/factory F8HeaderIOS))

(defui ItemWrapperIOS
  Object
  (render [this]
    (let [{:keys [item color]} (om/props this)]
      (when item
        (let [{:keys [title icon layout onPress]} item
              content (cond
                        (and (not= layout "icon") title)
                          (text
                            {:style [(styles :itemText) {:color color}]}
                            (.toUpperCase title))
                        title
                          (image {:source icon}))]
          (touchable-opacity
            {:accessibilityLabel title
             :accessibilityTraits "button"
             :onPress onPress
             :style (styles :itemWrapper)}
            content))))))

(def item-wrapper-ios (om/factory ItemWrapperIOS))

(declare item-wrapper-windows)

(defui F8HeaderWindows
  Object
  (render [this]
    (let [{:keys [leftItem title rightItem foreground style]} (om/props this)
          titleColor (if (= foreground :dark) dark-text :white)
          itemsColor (if (= foreground :dark) light-text :white)
          children (om/children this)
          content (if (= 0 (js/React.Children.count children))
                    (text
                      {:style [(styles :titleText) {:color titleColor}]})
                    children)]
      (view
        {:style [(styles :header) style]}
        (view
          {:style (styles :leftItem)}
          (item-wrapper-windows
            {:color itemsColor
             :item leftItem}))
        (view
          {:accessible true
           :accessibilityLabel title
           :accessibilityTraits :header
           :style (styles :centerItem)}
          content)
        (view
          {:style (styles :rightItem)}
          (item-wrapper-windows
            {:color itemsColor
             :item rightItem}))))))

(def f8-header-windows (om/factory F8HeaderWindows))

(defui ItemWrapperWindows
  Object
  (render [this]
    (let [{:keys [item color]} (om/props this)]
      (when item
        (let [{:keys [title icon layout onPress]} item
              content (cond
                        icon (image
                               {:source icon})
                        title (text
                                {:style [(styles :itemText) {:color color}]}
                                (.toUpperCase title)))]
          (touchable-opacity
            {:accessibilityLabel title
             :accessibilityTraits :button
             :onPress onPress
             :style (styles :itemWrapperWindows)}
            content))))))

(def item-wrapper-windows (om/factory ItemWrapperWindows))

(def header (cond
              (ios?) f8-header-ios
              (android?) f8-header-android
              (windows?) f8-header-windows))

(def STATUS-BAR-HEIGHT (if (ios?) 20 25))
(def HEADER-HEIGHT (if (ios?) (+ 44 STATUS-BAR-HEIGHT) (+ 56 STATUS-BAR-HEIGHT)))

(def styles (create-style-sheet
              {:toolbarContainer {:paddingTop STATUS-BAR-HEIGHT}
               :toolbar {:height (- HEADER-HEIGHT STATUS-BAR-HEIGHT)}
               :header {:backgroundColor "transparent"
                        :paddingTop STATUS-BAR-HEIGHT
                        :height HEADER-HEIGHT
                        :flexDirection "row"
                        :justifyContent "space-between"
                        :alignItems "center"}
               :titleText {:color "white"
                           :fontWeight "bold"
                           :fontSize 20}
               :leftItem {:flex 1
                          :alignItems "flex-start"}
               :centerItem {:flex 2
                            :alignItems "center"}
               :rightItem {:flex 1
                           :alignItems "flex-end"}
               :itemWrapper {:padding 11}
               :itemWrapperWindows 11
               :itemText {:letterSpacing 1
                          :fontSize 12
                          :color "white"}}))