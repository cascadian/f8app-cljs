(ns f8.tabs.info.third-party-notices
  (:require [om.next :as om :refer [defui]]
            [react-native.components :refer [view web-view]]
            [f8.common.f8-header :refer [header]]
            [react-native.api :refer [create-style-sheet InteractionManager]]))

(declare styles loading)

(defui ThirdPartyNotices
  Object
  (render [this]
    (let [{:keys []} (om/props this)]
      (view
        {:style (styles :container)}
        (header
          {:title "Third Party Notices"
           :style (styles :header)
           :leftItem {:icon (js/require "./images/common/back_white.png")
                      :title "Back"
                      :layout :icon
                      ; TODO navigation
                      :onPress #()}})
        (loading
          {}
          (web-view
            {:style (styles :webview)
             :source {:uri "file:///android_res/raw/third-party-notices.html"}}))))))

(def third-party-notices (om/factory ThirdPartyNotices))

(defui Loading
  Object
  (initLocalState [this]
    {:loaded false})
  (componentDidMount [this]
    (.runAfterInteractions InteractionManager #(om/set-state! this {:loaded true})))
  (render [this]
    (let [{:keys []} (om/props this)]
      (when (om/get-state this :loaded) (js/React.children.only (om/children this))))))

(def loading (om/factory Loading))

(def styles
  (create-style-sheet
    {:container
     {:flex 1
      :backgroundColor :white}
     :header
     {:backgroundColor "#47BFBF"}
     :webview
     {:fliex 1}}))