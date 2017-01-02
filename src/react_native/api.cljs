(ns react-native.api)

(def ReactNative (js/require "react-native"))
(def NativeModules (.-NativeModules ReactNative))
; ReactNative APIs
(def Platform (.-Platform ReactNative))
(def BackAndroid (.-BackAndroid ReactNative))
(def StyleSheet (.-StyleSheet ReactNative))
(def Dimensions (.-Dimensions ReactNative))
(def PixelRatio (.-PixelRatio ReactNative))
(def Animated (.-Animated ReactNative))
(def InteractionManager (.-InteractionManager ReactNative))
(def Linking (.-Linking ReactNative))
(def ActionSheetIOS (.-ActionSheetIOS ReactNative))
(def findNodeHandle (.-findNodeHandle ReactNative))
(def Clipboard (.-Clipboard ReactNative))
(def LayoutAnimation (.-LayoutAnimation ReactNative))



(defn create-style-sheet [m]
  (let [styles (.create StyleSheet (clj->js m))]
    (fn [k] (aget styles (name k)))))

(def platform-os (keyword (.-OS Platform)))
(defn android? [] (= "android" platform-os))
(defn ios? [] (= "ios" (.-OS platform-os)))
(defn windows? [] (= "windows" platform-os))