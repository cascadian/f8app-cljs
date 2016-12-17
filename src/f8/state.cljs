(ns f8.state
  (:require [om.next :as om]
            [f8.parser :as parser]
            [re-natal.support :as sup]))

(defonce app-state (atom {:app/msg      "Hello Clojure in iOS and Android!"
                          :reload-count 0
                          :nav/state    {:nav/index       0
                                         :nav/key-counter 0
                                         :nav/routes      [{:key 0 :tab :schedule}]}}))


(defonce shared-state (atom {:platform-specific-components {}}))

(defonce reconciler
         (om/reconciler
           {:state        app-state
            :shared-fn    (fn [_] @shared-state)
            :parser       parser/parse-fn
            :root-render  sup/root-render
            :root-unmount sup/root-unmount}))