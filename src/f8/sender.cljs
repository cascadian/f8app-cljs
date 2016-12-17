(ns f8.sender
  (:require [om.next :as om]
            [f8.graph-ql :as ql]
            [goog.events :refer [listen] :as ev]))

(def GRAPH-QL-ENDPOINT "http://localhost:8080/parse")
(def DONE 4)

(defn send
  [target-expression-map callback]
  (if-let [query (:remote target-expression-map)]
    (let [q (ql/query->graph-ql query)
          xhr (js/XMLHttpRequest.)]
      (.setRequestHeader xhr "Accept" "application/json")
      (.setRequestHeader xhr "Content-Type" "application/json")
      (.onreadystatechange xhr
                           (fn [_]
                             (let [ready-state (.-readyState xhr)]
                               (when (= DONE ready-state)
                                 (when (= 200 (.-status xhr))
                                   (let [response-text (.-responseText xhr)
                                         json (.stringify js/JSON response-text)]
                                     (callback (js->clj json :keywordize-keys true) query)))))))
      (.open xhr "POST" GRAPH-QL-ENDPOINT)
      (.send (.stringify js/JSON #js {:query q}))


      )
    (throw (js/Error. "The :remote target was not found"))))
