(ns f8.main
  (:require [om.next :as om :refer-macros [defui]]
            [re-natal.support :as sup]
            [react-native.components :refer [view text]]
            [f8.f8-navigator :refer [f8-navigator]]
            [f8.tabs.info.f8-info-view :as info-view]
            [f8.tabs.info.module-config :as info-module]
            [routom.ajax :as ajax]
            [f8.env :refer [env]]
            [f8.state :as state])
  (:import [goog.net XhrManager]))

(set! js/React (js/require "react"))
(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))

(def logo-img (js/require "./images/cljs.png"))

(defui ^:once AppRoot
  static om/IQueryParams
  (params [this]
    {:info-query (om/get-query info-view/F8InfoView)})
  static om/IQuery
  (query [this]
    '[{:remote/info [*]}
      {:ui/info-view ?info-query}])
  Object
  (render [this]
    (view
      {:style {:flex 1}}
      (f8-navigator (om/props this)))))

(defonce RootNode (sup/root-node! 1))
(defonce app-root (om/factory RootNode))

(def main-config
  {:db/data   [{:db/ident    :app/settings
                :app/msg          "Hello Clojure in iOS and Android!"}

               {:db/ident   :nav/state
                :nav/index       0
                :nav/key-counter 0
                :nav/routes      [{:nav.route/key 0
                                   :nav.route/tab :schedule}]}]
   :db/schema {:remote/key {:db/unique :db.unique/identity}

               :db/ident {:db/unique      :db.unique/identity
                               :db/cardinality :db.cardinality/one}
               :nav/routes    {:db/valueType   :db.type/ref
                               :db/cardinality :db.cardinality/many
                               :db/isComponent true}}})

(def module-configs [main-config info-module/config])

(defonce app-state (state/init-app-state module-configs))

(def xhrm (ajax/map->XhrManager {:headers {"Content-Type" "application/json"
                                           "Accept" "application/json"}}))

(def remoting (state/init-remoting module-configs {:xhrm xhrm :url (:serverUrl env)}))

(defonce reconciler (state/init-reconciler (merge
                                             {:state app-state}
                                             remoting)))

(defn init []
  (om/add-root! reconciler AppRoot 1)
  (.registerComponent app-registry "F8" (fn [] app-root)))
