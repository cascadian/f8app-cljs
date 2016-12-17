(ns f8.tabs.info.parser
  (:require [f8.parser :as parser]))

(defmethod parser/read :info
  [{:keys [state] :as env} key params]
  (let [st @state]
    (if-let [[_ v] (find st key)]
      {:value v}
      {:remote true
       :value v})))
