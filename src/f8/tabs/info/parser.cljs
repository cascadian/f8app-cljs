(ns f8.tabs.info.parser
  (:require [om.next :as om]
            [routom.parsing :as p]
            [datascript.core :as d]))


(defmethod p/read-value :ui/info-view
  [{:keys [db query] :as env} key params]
  {:value (d/pull db query [:db/ident key])})

(defmethod p/read-target :ui/info-view
  [{:keys [target query ast db] :as env} key params]
  (when (= target :remote/info)
    ; 1. get query roots
    ; 2. check remote status of query roots
    ; 3. return ast for any query roots that haven't started
    (let [{query* :query} (om/process-roots query)
          ast* (om/query->ast query*)
          lookup-refs (map (fn [c] [:db/ident (:dispatch-key c)]) (:children ast*))
          remote-statuses (d/pull-many db [:db/ident :remote/status] lookup-refs)
          inactive-remotes (vec (filter some? (map-indexed (fn [i {:keys [remote/status]}]
                                                             (when (= :inactive status)
                                                               (get-in ast* [:children i])))
                                                           remote-statuses)))]
      (when (seq inactive-remotes)
        {target (assoc ast :children (vec inactive-remotes))}))))
