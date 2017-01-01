(ns f8.tabs.info.f8-info-view
  (:require [om.next :as om :refer [defui]]
            [react-native.components :refer [view]]
            [f8.tabs.info.common-questions :refer [common-questions CommonQuestions]]
            [f8.tabs.info.links-list :refer [links-list Row]]
            [f8.common.list-container :refer [list-container]]
            [f8.tabs.info.wi-fi-details :refer [wi-fi-details WiFiDetails]]
            [f8.common.pure-list-view :refer [pure-list-view]]))

(declare styles info-list)

(def POLICIES-LINKS
  [{:page/title "Terms of Service"
    :page/url "https://m.facebook.com/terms?_rdr"}
   {:page/title "Data Policy"
    :page/url "https://m.facebook.com/policies?_rdr"}
   {:page/title "Code of Conduct"
    :page/url "https://www.fbf8.com/code-of-conduct"}])

(defui F8InfoView
  static om/IQuery
  (query [this]
    `[
    :ui.info/title
     {:ui.info/viewer
      {:user/viewer [{:info/config ~(om/get-query WiFiDetails)}
                {:info/faqs ~(om/get-query CommonQuestions)}
                {:info/pages ~(om/get-query Row)}]}}])
  Object
  (render [this]
    (let [{:keys [schedule.info-view/title]} (om/props this)]
      (list-container
        {:title           title
         :backgroundImage (js/require "./images/tabs/info/info-background.png")
         :backgroundColor "#47BFBF"}
        (info-list (om/props this))))))

(def f8-info-view (om/factory F8InfoView))


(defn info-list [props]
  (let [{v :viewer} props
        {:keys [info/config info/faqs info/pages]} v]
    (pure-list-view
      {:renderEmptyList #(view
                           {}
                           (wi-fi-details
                             {:network (:wifiNetwork config)
                              :password (:wifiPassword config)})
                           (common-questions
                             {:faqs faqs}
                             (links-list
                               {:title "Facebook pages"
                                :links pages})
                             (links-list
                               {:title "Facebook policies"
                                :links POLICIES-LINKS})))}
      (dissoc props :viewer))))

