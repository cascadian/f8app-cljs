(ns f8.tabs.info.common-questions
  (:require [om.next :as om :refer [defui]]
            [f8.common.items-with-separator :refer [items-with-separator]]
            [f8.tabs.info.section :refer [section]]
            [react-native.api :refer [create-style-sheet LayoutAnimation]]
            [react-native.components :refer [view]]
            [f8.common.f8-touchable :refer [f8-touchable]]
            [f8.common.f8-text :refer [text]]))

(declare styles row)

(defui CommonQuestions
  static om/IQuery
  (query [this]
    [:db/id :server/id :faq/question :faq/answer])
  Object
  (render [this]
    (let [{:keys [faqs]} (om/props this)
          rows (map
                 (fn [{:keys [question answer]}]
                   (row
                     {:question question
                      :answer answer
                      :key question}))
                 faqs)]
      (section
        {:title "Common questions"}
        (items-with-separator
          {:separatorStyle (styles :separator)}
          rows)))))

(def common-questions (om/factory CommonQuestions))

(defui Row
  Object
  (initLocalState [this]
    {:expanded false})
  (render [this]
    (let [{:keys [answer question]} (om/props this)
          expanded (om/get-state this :expanded)
          answer-element (and expanded
                              (view
                                {:style (styles :answer)}
                                (text
                                  {:style (styles :text)}
                                  answer)))]
      (view
        {:style (styles :row)}
        (f8-touchable
          {:onPress #(.toggle this)}
          (view
            {:style (styles :question)}
            (text
              {:style (styles :symbol)}
              (if expanded "\u2212" "+"))
            (text
              {:style (styles :text)}
              question)))
        answer-element)))
  (toggle [this]
    (.configureNext LayoutAnimation (.. LayoutAnimation -Presets -easeInEaseOut))
    (om/update-state! this update :expanded not)))

(def row (om/factory Row))

(def styles
  (create-style-sheet
    {:separator
      {:marginHorizontal 20}
     :question
      {:paddingVertical 14
       :paddingHorizontal 20
       :flexDirection :row
       :backgroundColor :white}
     :symbol
      {:fontSize 15
       :lineHeight 21
       :width 22
       :color "#99A7B9"}
     :answer
      {:padding 14
       :paddingLeft (+ 20 22)}
     :text
      {:fontSize 15
       :lineHeight 21
       :color "#002350"
       :flex 1}}))




