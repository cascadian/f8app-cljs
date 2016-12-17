(ns f8.graph-ql
  (:require [om.next :as om]
            [clojure.string :as string]))

(def DEFAULT-INDENT "  ")

(defn format-value [v]
  (cond
    (string? v) (str "\"" v "\"")
    (keyword? v) (name v)
    :else v))

(defn ast->graph-ql
  ([ast]
   (ast->graph-ql "" DEFAULT-INDENT ast))
  ([current-indent indent {:keys [type children key params] :as ast}]
   (letfn [(children->graph-ql []
             (if children
               (str (string/join (str "\n" indent) (map (partial ast->graph-ql indent (str indent DEFAULT-INDENT)) children))
                    "\n" current-indent "}")
               ""))
           (->arguments-ql []
             (if params
               (str (name key) "(" (string/join ", " (map (fn [[k v]] (str (name k) ": " (format-value v))) params)) ")")
               (name key)))]
     (condp = type
       :root (str "{\n" indent
                  (children->graph-ql))
       :join (str (->arguments-ql) " {\n" indent (children->graph-ql))
       :prop (->arguments-ql)))))


(defn query->graph-ql [query]
  (ast->graph-ql (om/query->ast query)))
