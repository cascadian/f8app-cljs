(ns f8.parser
  (:require [om.next :as om]))




(defmulti read om/dispatch)
(defmethod read :default
  [{:keys [state]} k _]
  (let [st @state]
    (if-let [[_ v] (find st k)]
      {:value v}
      {:value :not-found})))


(def parser (om/parser {:read read}))

(defonce counter (atom {:remote 1}))

(defn parse-fn
  ([env query]
    (parse-fn env query nil))
  ([env query target]
   (let [counter (swap! counter update :remote inc)
         parsed (parser env query target)]
     (println parsed)

     parsed)))