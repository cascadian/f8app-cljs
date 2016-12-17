(ns f8.tabs.info.f8-info-view
  (:require [om.next :as om :refer [defui]]
            [react-native.components :refer [view]]
            [f8.tabs.info.common-questions :refer [common-questions]]
            [f8.tabs.info.links-list :refer [links-list]]
            [f8.common.list-container :refer [list-container]]
            [f8.tabs.info.wi-fi-details :refer [wi-fi-details]]
            [f8.common.pure-list-view :refer [pure-list-view]]))

(declare styles info-list)

(def POLICIES-LINKS
  [{:title "Terms of Service"
    :url "https://m.facebook.com/terms?_rdr"}
   {:title "Data Policy"
    :url "https://m.facebook.com/policies?_rdr"}
   {:title "Code of Conduct"
    :url "https://www.fbf8.com/code-of-conduct"}])

(defui F8InfoView
  static om/IQuery
  (query [this]
    [{:config [:wifiNetwork :wifiPassword]}
     {:faqs [:question :answer]}
     {:pages [:title :url :logo]}])
  Object
  (render [this]
    (let [{:keys []} (om/props this)]
      (list-container
        {:title           "Information"
         :backgroundImage (js/require "./images/tabs/info/info-background.png")
         :backgroundColor "#47BFBF"}
        (info-list (om/props this))))))

(def f8-info-view (om/factory F8InfoView))


(defn info-list [props]
  (let [{v :viewer} props
        {:keys [config faqs pages]} v]
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

