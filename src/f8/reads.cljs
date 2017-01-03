(ns f8.reads
  (:require [routom.parsing :as p :refer [read-value]]
            [datascript.core :as d]))

(defmethod p/read-value :db/ident
  [{:keys [db query] {:keys [key]} :ast} _ params]
  {:value (d/q '[:find (pull ?e ?selector) .
                 :in $ ?selector
                 :where [?e :db/ident ?key]]
               db query (second key))})
