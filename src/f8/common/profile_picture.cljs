(ns f8.common.profile-picture
  (:require
    [om.next :as om :refer-macros [defui]]
    [react-native.components :refer [image]]
    [react-native.api :refer [PixelRatio]]))

(defui ProfilePicture
  Object
  (render [this]
    (let [{:keys [userID size]} (om/props this)
          scaled-size (* size (.get PixelRatio))
          uri (str "http://graph.facebook.com/" userID "/picture?width=" scaled-size "&height=" scaled-size)]
      (image
        {:source {:uri uri}
         :style {:width size
                 :height size
                 :borderRadius (/ size 2)}}))))

(def profile-picture (om/factory ProfilePicture))