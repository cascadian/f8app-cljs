(ns f8.tabs.info.sends
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [f8.sender :refer [send]]
            [goog.net.EventType :as EventType]
            [goog.json :as json]
            [routom.graphql :refer [query->graphql]]
            [routom.ajax :refer [send]]
            [clojure.walk :refer [prewalk-replace]]
            [routom.remoting :as r]
            [cljs.core.async :refer [chan <! sliding-buffer]]))

(def ^:private GRAPH-QL-ENDPOINT "http://localhost:8080/graphql")

(defmethod r/start-send-loop :remote/info
  [_ {:keys [xhrm]}]
  (let [ch (chan (sliding-buffer 1))]
    (go
      (loop [[query callback] (<! ch)]
        (let [update-status (fn [status] (callback {:viewer [{:db/ident :viewer :remote/status status}]}))
              _ (update-status :queued)
              success (fn [{:keys [xhrIo]}]
                        (update-status :success)
                        (let [json (.getResponseJson xhrIo)
                              json (js->clj (.. json -data -viewer) :keywordize-keys true)
                              json (prewalk-replace {:question :faq/question
                                                      :answer :faq/answer
                                                      :id :server/id
                                                      :url :page/url
                                                      :title :page/title
                                                      :logo :page/logo
                                                      :wifiNetwork :config/wifiNetwork
                                                      :wifiPassword :config/wifiPassword
                                                      :pages :info/pages
                                                      :config :info/config
                                                      :faqs :info/faqs} json)
                              tx-data [(assoc json :db/ident :viewer)]]
                          (callback {:viewer tx-data})))
              error (fn [{:keys [xhrIo] :as e}]
                      (update-status :error))
              handlers {EventType/READY   #(update-status :ready)
                        EventType/SUCCESS success
                        EventType/ERROR   error}
              content {:query (query->graphql query)}
              content (json/serialize (clj->js content))
              abort-fn (send xhrm {:content  content
                                        :method   "POST"
                                        :url      GRAPH-QL-ENDPOINT
                                        :handlers handlers})]
          (callback {:viewer [{:db/ident :viewer :remote/abort-fn abort-fn}]})
          (recur (<! ch)))))
    ch))




