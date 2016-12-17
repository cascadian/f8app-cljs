(ns f8.common.pure-list-view
  (:require [om.next :as om :refer-macros [defui]]
            [react-native.api :refer [Dimensions ios? android? create-style-sheet]]
            [react-native.components :refer [list-view ListView view]]))

(declare styles cloneWithData)

(def LIST-VIEW-PAGE-SIZE (if (android?) 20 1))

(defui PureListView
  Object
  (initLocalState [this]
    (let [data-source (ListView.DataSource.
                        #js {:getRowData (fn [dataBlob sid rid] (get-in dataBlob [sid rid]))
                         :getSectionHeaderData (fn [dataBlob sid] (get dataBlob sid))
                         :rowHasChanged (fn [row1 row2] (not= row1 row2))
                         :sectionHeaderHasChanged (fn [s1 s2] (not= s1 s2))})]
      {:contentHeight 0
       :dataSource (cloneWithData data-source (:data (om/props this)) )}))
  (componentWillReceiveProps [this next-props]
    (let [next-data (:data next-props)]
      (when (not= (:data (om/props this)) next-data)
        (om/update-state! this update :dataSource #(cloneWithData % next-data)))))
  (render [this]
    (let [{:keys [contentInset minContentHeight] :as props} (om/props this)
          {:keys [top bottom]} contentInset
          contentHeight (om/get-state this :contentHeight)
          bottom-offset (js/Math.max 0 (- minContentHeight contentHeight))
          bottom (+ bottom bottom-offset)]
      (list-view
        (merge
          {:initialListSize 20
           :pageSize        LIST-VIEW-PAGE-SIZE}
          props
          {:ref "listview"
           :dataSource (om/get-state this :dataSource)
           :renderFooter #(.renderFooter this)
           :contentInset {:bottom bottom :top top}
           :onContentSizeChange (.-onContentSizeChange this)}
          ))))
  (onContentSizeChange [this contentWidth contentHeight]
    (om/set-state! this {:contentHeight contentHeight}))
  (scrollTo [this & args]
    ; TODO need to call apply?
    (.scrollTo (om/react-ref this "listview") args))
  (getScrollResponder [this]
    (.getScrollResponder (om/react-ref this "listview")))
  (renderFooter [this]
    (if (= 0 (.getRowCount (om/get-state this :dataSource)))
      (let [renderEmptyList (:renderEmptyList (om/props this))]
        (and renderEmptyList (renderEmptyList)))
      (let [renderFooter (:renderFooter (om/props this))]
        (and renderFooter (renderFooter))))))

(defn cloneWithData [dataSource data]
  (cond
    (not data) (.cloneWithRows dataSource [])
    (vector? data) (.cloneWithRows dataSource data)
    :else (.cloneWithRowsAndSections dataSource data)))

(def styles
  (create-style-sheet
    {:separator
      {:backgroundColor "#eeeeee"
       :height 1}}))

(def pure-list-view (om/factory PureListView))