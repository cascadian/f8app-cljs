(ns f8.common.f8-style-sheet
  (:require [react-native.api :as a]))

(defn create-style-sheet [styles-map]
  (let [platform-styles (reduce-kv
            (fn [m k {:keys [ios android windows] :as v}]
              (let [rest (dissoc v :ios :android :windows)
                    f (fn [s] (assoc m k (merge rest s)))]
                (cond
                  (and ios (a/ios?)) (f ios)
                  (and android (a/android?)) (f android)
                  (and windows (a/windows?)) (f windows)
                  :else (f {}))))
            {}
            styles-map)]
    (a/create-style-sheet platform-styles)))