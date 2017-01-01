(ns f8.state
  (:require [om.next :as om]
            [routom.parsing :as p]
            [routom.datascript :as rd]
            [routom.remoting :as r]
            [re-natal.support :as sup]
            [datascript.core :as d]))


(defn init-app-state [module-configs]
  (let [schema (->> module-configs
                    (map :db/schema)
                    (apply merge))
        tx-data (mapcat :db/data module-configs)
        conn (d/create-conn schema)
        app-state (atom {:conn         conn
                         :reload-count 0})]
    (d/transact! conn tx-data)
    {:state app-state}))

(defn init-remoting [module-configs opts]
  (let [remotes (mapcat #(get % :reconciler/remotes #{}) module-configs)
        target-chan-map (reduce
                          (fn [m remote]
                            (assoc m remote (r/start-send-loop remote opts)))
                          {}
                          remotes)
        send-fn (r/create-send-fn target-chan-map)]
    {:remotes remotes
     :send send-fn}))

(defonce shared-state (atom {:platform-specific-components {}}))

(defn init-reconciler [opts]
  (om/reconciler
    (merge {:shared-fn    (fn [_] @shared-state)
            :parser       (-> p/parser
                              (rd/wrap-conn)
                              (p/wrap-ast-filtering #"^remote" #"^ui"))
            :merge        rd/merge!
            :root-render  sup/root-render
            :root-unmount sup/root-unmount}
           opts)))