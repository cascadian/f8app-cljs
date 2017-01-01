(ns f8.reads
  (:require [routom.parsing :as p :refer [read]]
            [datascript.core :as d]))

(defmethod p/read :db/ident
  [{:keys [state query]} key {singleton-key :key}]
  {:value (d/pull (d/db state) query [key singleton-key])})
