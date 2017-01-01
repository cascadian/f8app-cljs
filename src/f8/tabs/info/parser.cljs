(ns f8.tabs.info.parser
  (:require [routom.parsing :as p]
            [datascript.core :as d]))


(defmethod p/read-value :schedule/info-view
  [{:keys [db query] :as env} key params]
  {:value (d/pull db query [:db/ident key])})

(defmethod p/read-target :schedule/info-view
  [{:keys [target] :as env} key params]
  (when (= target key)
    (let [{v :value} (p/read-value env key params)]
      (when-not v
        {target true}))))
