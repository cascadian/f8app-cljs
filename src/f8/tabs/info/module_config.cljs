(ns f8.tabs.info.module-config
  (:require [f8.tabs.info.parser]
            [f8.tabs.info.sends]))

(def config
  {:db/data            [
                        {:db/ident :remote/viewer
                         :remote/status :inactive}
                        {:db/ident       :ui/info-view
                         :ui.info/title  "Info"
                         :remote/viewer [:db/ident :remote/viewer]}]
   :db/schema          {:remote/viewer      {:db/valueType   :db.type/ref
                                              :db/cardinality :db.cardinality/one}
                        :info/config         {:db/valueType   :db.type/ref
                                              :db/cardinality :db.cardinality/one
                                              :db/isComponent true}
                        :config/wifiNetwork  {}
                        :config/wifiPassword {}
                        :info/faqs           {:db/valueType   :db.type/ref
                                              :db/cardinality :db.cardinality/many
                                              :db/isComponent true}
                        :faq/question        {}
                        :faq/answer          {}
                        :faq/id              {:db/unique :db.unique/identity}
                        :server/id           {:db/unique :db.unique/identity}

                        :info/pages          {:db/valueType   :db.type/ref
                                              :db/cardinality :db.cardinality/one
                                              :db/isComponent true}
                        :page/id             {:db/unique :db.unique/identity}
                        :page/title          {}
                        :page/url            {}
                        :page/logo           {}}
   :reconciler/remotes #{:remote/info}})
