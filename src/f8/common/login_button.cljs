(ns f8.common.login-button
  (:require [om.next :as om :refer-macros [defui]]
            [f8.common.f8-button :refer [f8-button]]
            [react-native.api :refer [create-style-sheet]]))

(declare styles)

(defui LoginButton
  Object
  (initLocalState [this]
    {:isLoading false})
  (componentDidMount [this]
    (set! (.-_isMounted this) true))
  (componentWillUnmount [this]
    (set! (.-_isMounted this) false))
  (render [this]
    (if (om/get-state this :isLoading)
      (f8-button
        {:style [(styles :button) (:style (om/props this))]
         :caption "Please wait..."
         :onPress #()})
      (f8-button
        {:style [(styles :button) (:style (om/props this))]
         :icon (js/require "./images/login/f-logo.png")
         :caption "Log in with Facebook"
         :onPress #(.login this)})))
  (logIn [this]
    ; TODO
    ))

(def login-button (om/factory LoginButton))

(def styles (create-style-sheet
              {:button
                {:alignSelf "center"
                 :width 270}}))