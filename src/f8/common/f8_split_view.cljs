(ns f8.common.f8-split-view
  (:require [om.next :as om :refer [defui]]
            [react-native.components :as c]
            [react-native.api :as a]
            [react-native.windows :refer [split-view-windows SplitViewWindows]]))

(declare styles)

(defui F8SplitView
  Object
  (render [this]
    (let [{:keys [panePosition] :as props} (om/props this)
          Right (.. SplitViewWindows -positions -Right)
          Left (.. SplitViewWindows -positions -Left)]
      (split-view-windows
        (merge
          {:ref #(set! (.-_splitView this) %)}
          props
          {:panePosition (if (= panePosition :right) Right Left)
           :onPaneOpen #(.onPaneOpen this)
           :onPaneClose #(.onPaneClose this)}))))
  (componentWillUnmount [this]
    (this.context.removeBackButtonListener (.-handleBackButton this))
    (set! (.-_splitView this) nil))

  (handleBackButton [this]
    (.closePane this)
    true)

  (onPaneOpen [this]
    (this.context.removeBackButtonListener (.-handleBackButton this))
    (when-let [f (:onPaneOpen (om/props this))]
      (f)))

  (onPaneClose [this]
    (this.context.removeBackButtonListener (.-handleBackButton this))
    (when-let [f (:onPaneClose (om/props this))]
      (f)))

  (closePane [this]
    (when-let [splitView (.-_splitView this)]
      (splitView.closePane)))

  (openPane [this]
    (when-let [splitView (.-_splitView this)]
      (splitView.openPane))))

(set! (.-contextTypes F8SplitView) #js {:addBackButtonListener js/React.PropTypes.func
                                        :removeBackButtonListener js/React.PropsTypes.func})

(def f8-split-view (om/factory F8SplitView))

(def styles
  (a/create-style-sheet
    {}))