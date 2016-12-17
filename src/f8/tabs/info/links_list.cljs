(ns f8.tabs.info.links-list
  (:require [om.next :as om :refer [defui]]
            [f8.tabs.info.section :refer [section]]
            [react-native.api :refer [create-style-sheet Linking]]
            [react-native.components :refer [image view]]
            [f8.common.items-with-separator :refer [items-with-separator]]
            [f8.common.f8-touchable :refer [f8-touchable]]
            [f8.common.f8-text :refer [text]]))



(declare styles row)

(defui LinksList
  Object
  (render [this]
    (let [{:keys [links title]} (om/props this)
          rows (map
                 (fn [link]
                   (row
                     {:link link
                      :key (:title link)}))
                 links)]
      (section
        {:title title}
        (items-with-separator
          {:separatorStyle (styles :separator)}
          rows)))))

(def links-list (om/factory LinksList))

(defui Row
  Object
  (render [this]
    (let [{:keys [link]} (om/props this)
          {:keys [logo title url onPress]} link
          img (and logo (image
                          {:style (styles :picture)
                           :source {:uri logo}}))]
      (f8-touchable
        {:onPress #(.handlePress this url onPress)}
        (view
          {:style (styles :row)}
          img
          (text
            {:style (styles :title)
             :numberOfLines 2}
            title)
          (image
            {:source (js/require "./images/common/disclosure.png")})))))
  (handlePress [this url onPress]
    (when onPress (onPress))
    (when url (.openUrl Linking url))))

(def row (om/factory Row))

(def IMAGE-SIZE 44)

(def styles
  (create-style-sheet
    {:separator
      {:marginHorizontal 20}
     :row
      {:flexDirection :row
       :alignItems :center
       :paddingVertical 10
       :paddingHorizontal 20
       :backgroundColor :white
       :height 60}
     :picture
      {:width IMAGE-SIZE
       :height IMAGE-SIZE
       :borderRadius (/ IMAGE-SIZE 2)
       :marginRight 16}
     :title
      {:fontSize 17
       :color f8.common.f8-colors/dark-text
       :flex 1}
     :button
      {:padding 10}
     :like
      {:letterSpacing 1
       :color f8.common.f8-colors/action-text
       :fontSize 12}}))