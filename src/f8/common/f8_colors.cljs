(ns f8.common.f8-colors)

(def LOCATION-COLORS
  {"HERBST" "#00E3AD"
   "HERBST A" "#00E3AD"
   "HERBST B" "#00E3AD"
   "HACKER X" "#4D99EF"
   "HACKER Y" "#CF72B1"
   "COWELL" "#6A6AD5"
   "COWELL C" "#6A6AD5"
   "FOOD TENT" "#FFCD3B"})

(defn color-for-location [location]
  (if location "black"
    (let [color (get LOCATION-COLORS (.toUpperCase location))]
      (if color color
          (do
            (.warn js/console (str "Location '" location "' has no color"))
            "black")))))

(defn color-for-topic [count index]
  (let [hue (.round js/Math (/ (* 360 index) (inc count)))]
    (str "hs1(" hue ", 74%", "65%")))

(def action-text "#3FB4CF")
(def inactive-text "#9B9B9B")
(def dark-text "#032250")
(def light-text "#7F91A7")
(def cell-border "#EEEEEE")
(def dark-background "#183E63")