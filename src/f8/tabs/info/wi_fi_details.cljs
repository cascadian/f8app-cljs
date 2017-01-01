(ns f8.tabs.info.wi-fi-details
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.components :refer [view]]
            [react-native.api :refer [Platform android? create-style-sheet]]
            [f8.common.items-with-separator :refer [items-with-separator]]
            [f8.tabs.info.section :refer [section]]
            [f8.common.f8-text :refer [text]]
            [f8.common.f8-colors :as colors]
            [f8.common.f8-button :refer [f8-button]]
            ))

(def Clipboard (js/require "Clipboard"))
(def ToastAndroid (js/require "ToastAndroid"))

(declare styles row)

(defui WiFiDetails
  om/IQuery
  (query [this]
    [:db/id :config/wifiNetwork :config/wifiPassword])
  Object
  (initLocalState [this]
    {:copied false})
  (render [this]
    (let [{:keys [config/network config/password]} (om/props this)
          caption (if (om/get-state this :copied) "Copied!" "Copy password")]
      (section
        {:title "WiFi"
         :style (styles :container)}
        (items-with-separator
          {}
          (row
            {:label "Network"
             :value network})
          (row
            {:label "Password"
             :value password}))
        (f8-button
          {:style (styles :button)
           :onPress #(.handleCopy this password)
           :caption caption}))))
  (handleCopy [this password]
    (.setString Clipboard password)
    (if (android?)
      (.show ToastAndroid "Copied!" (.-SHORT ToastAndroid))
      (do
        (om/set-state! this {:copied true})
        (js/setTimeout #(om/set-state! this {:copied false}) 800)))))

(def wi-fi-details (om/factory WiFiDetails))

(defui Row
  Object
  (render [this]
    (let [{:keys [label value]} (om/props this)]
      (view
        {:style (styles :row)}
        (text
          {:style (styles :label)}
          (.toUpperCase label))
        (text
          {:style (styles :value)}
          value)))))

(def row (om/factory Row))

(def style
  (create-style-sheet
    {:container
      {:paddingTop 40
       :paddingHorizontal 50}
     :row
      {:flexDirection :row
       :justifyContent :space-between
       :marginVertical 10}
     :label
      {:fontSize 15
       :color colors/light-text}
     :value
      {:fontSize 15
       :color "#002350"}
     :button
      {:marginTop 25
       :marginHorizontal 20}}))