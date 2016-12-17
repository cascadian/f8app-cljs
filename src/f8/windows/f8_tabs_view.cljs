(ns f8.windows.f8-tabs-view
  (:require [om.next :as om :refer [defui]]
            [react-native.components :as c]
            [react-native.api :as a]
            [f8.common.f8-colors :as colors]
            [f8.windows.f8-split-view :refer [f8-split-view]]))

(declare styles)

(defui ^:once F8TabsView
  Object
  (render-content [this]
    (c/text
      {:style {:fontSize 40}}
      "Hello world!"))
  (render-pane-view [this]
    (c/view
      {:style (styles :splitView)}
      (c/text
        {:style {:fontSize 40 :margin 10 :textAlign :left}}
        "Pane View")))
  (render [this]
    (let [{{:keys [tab]} :navigation} (om/props this)]
      (f8-split-view
        {:key            "splitView"
         :paneWidth      290
         :panePosition   :left
         :renderPaneView #(.render-pane-view this)}
        (c/view
          {:style (styles :content)
           :key   tab}
          (.render-content this))))))

(def f8-tabs-view (om/factory F8TabsView))

(def styles
  (a/create-style-sheet
    {:splitView   {:flex            1
                   :backgroundColor :green}
     :content     {:flex 1}
     :header      {:padding        20
                   :justifyContent :flex-end}
     :name        {:marginTop 10
                   :color     :white
                   :fontSize  12}
     :loginPrompt {:flex           1
                   :justifyContent :flex-end
                   :paddingBottom  10}
     :loginText   {:fontSize  12
                   :color     colors/light-text
                   :textAlign :center}}))
