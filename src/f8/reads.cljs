(ns f8.reads
  (:require [routom.parsing :as p :refer [read-value]]
            [datascript.core :as d]))

(defmethod p/read-value :db/ident
  [{:keys [db query]} key {singleton-key :key}]
  {:value (d/pull db query [key singleton-key])})
