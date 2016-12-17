(ns f8.common.carousel
  (:require [om.next :as om :refer-macros [defui]]
            [f8.common.f8-style-sheet :refer [create-style-sheet]]
            [f8.common.view-pager :refer [view-pager]]))

(declare styles)

(defui Carousel
  Object
  (render [this]
    (let [{:keys [count selectedIndex renderCard] :as props} (om/props this)
          cards (for [i (range count)
                      :let [card (renderCard i)
                            absv (js/Math.abs (- i selectedIndex))]
                      :when (< absv 2)]
                  card)]
      (view-pager
        (merge {:style (styles :carousel)} props {:bounces true})
        cards))))

(def styles (create-style-sheet
              {:carousel
               {:ios
                {:margin 10
                 :overflow "visible"
                 :backgroundColor "black"}}}))

(def carousel (om/factory Carousel))