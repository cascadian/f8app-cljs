(ns react-native.components)

(set! js/React (js/require "react"))
(def ReactNative (js/require "react-native"))

(defn create-element [rn-comp opts & children]
  (apply js/React.createElement rn-comp (clj->js opts) children))

; ReactNative components
(def app-registry (.-AppRegistry ReactNative))
(def view (partial create-element (.-View ReactNative)))
(def text (partial create-element (.-Text ReactNative)))
(def Image (.-Image ReactNative))
(def image (partial create-element Image))
(def touchable-highlight (partial create-element (.-TouchableHighlight ReactNative)))
(def status-bar (partial create-element (.-StatusBar ReactNative)))
(def Navigator (.-Navigator ReactNative))
(def navigator (partial create-element Navigator))
(def view-pager-android (partial create-element (.-ViewPagerAndroid ReactNative)))
(def scroll-view (partial create-element (.-ScrollView ReactNative)))
(def touchable-opacity (partial create-element (.-TouchableOpacity ReactNative)))
(def Animated (.-Animated ReactNative))
(def animated-view (partial create-element (.-View Animated)))
(def animated-image (partial create-element (.-Image Animated)))
(def animated-text (partial create-element (.-Text Animated)))
(def ListView (.-ListView ReactNative))
(def list-view (partial create-element ListView))
(def WebView (.-WebView ReactNative))
(def web-view (partial create-element WebView))
(def TouchableWithoutFeedback (.-TouchableWithoutFeedback ReactNative))
(def touchable-without-feedback (partial create-element TouchableWithoutFeedback))
