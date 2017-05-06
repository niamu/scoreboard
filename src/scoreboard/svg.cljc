(ns scoreboard.svg
  (:require [scoreboard.util :as util]
            #?(:clj [hiccup.core :refer [html]])
            [clojure.string :as string]
            [garden.color :as color]))

(def height 121)
(def width 370)

(defn rounded-rect
  [{:keys [x y width height radius fill]}]
  [:path
   {:d (str "M" x "," y " "
            "h" width " "
            "a" radius "," radius " 0 0 1 " radius "," radius " "
            "v" height " "
            "a" radius "," radius " 0 0 1 " (- radius) "," radius " "
            "h" (- width) " "
            "a" radius "," radius " 0 0 1 " (- radius) "," (- radius) " "
            "v" (- height) " "
            "a" radius "," radius " 0 0 1 " radius "," (- radius) " "
            "z")
    :fill fill}])

(defn scoreboard
  [data]
  (let [radius 5
        bar-height 28
        bar-count 3
        bar-padding 2
        width 350
        height (+ (* bar-height bar-count)
                  (* (* bar-padding 2) bar-count)
                  radius)]
    [:svg {:xmlns "http://www.w3.org/2000/svg"
           :version "1.1"
           :role "img"
           :width (+ width (* radius 4))
           :height (+ height (* radius 4))}
     [:defs
      [:linearGradient {:id "white_gradient"
                        :x1 0
                        :x2 1
                        :y1 0
                        :y2 1}
       [:stop {:offset "0%" :stop-color "#FFF"}]
       [:stop {:offset "100%" :stop-color (-> "#FFF"
                                              (color/darken 15)
                                              color/as-hex)}]]
      [:linearGradient {:id "black_gradient"
                        :x1 0
                        :x2 1
                        :y1 0
                        :y2 1}
       [:stop {:offset "0%" :stop-color "#333"}]
       [:stop {:offset "100%" :stop-color (-> "#333"
                                              (color/darken 30)
                                              color/as-hex)}]]
      (let [c1 (get data :player1/color "#0F0")]
        [:linearGradient {:id "player1_gradient"
                          :x1 0
                          :x2 1
                          :y1 0
                          :y2 1}
         [:stop {:offset "0%" :stop-color c1}]
         [:stop {:offset "100%" :stop-color (-> c1
                                                (color/darken 30)
                                                color/as-hex)}]])
      (let [c2 (get data :player2/color "#00F")]
        [:linearGradient {:id "player2_gradient"
                          :x1 0
                          :x2 1
                          :y1 0
                          :y2 1}
         [:stop {:offset "0%" :stop-color c2}]
         [:stop {:offset "100%" :stop-color (-> c2
                                                (color/darken 30)
                                                color/as-hex)}]])]
     (rounded-rect {:x radius
                    :y radius
                    :width width
                    :height height
                    :radius radius
                    :fill "url(#white_gradient)"})
     [:g {:transform (str "translate("
                          (+ radius bar-padding)
                          ","
                          (+ radius bar-padding)
                          ")")}
      (rounded-rect {:x 0
                     :y 0
                     :width (- width (* bar-padding 2))
                     :height (- bar-height (* bar-padding 2))
                     :radius radius
                     :fill "url(#black_gradient)"})
      [:text {:x (+ (- width 108)
                    22)
              :y (+ (/ bar-height 2)
                    (* bar-padding 4))
              :fill "#FFF"
              :style {:font-weight "bold"}
              :font-size "14"
              :font-family "Helvetica"
              :text-anchor "middle"}
       "20s"]
      [:text {:x (+ (- width 50)
                    22)
              :y (+ (/ bar-height 2)
                    (* bar-padding 4))
              :fill "#FFF"
              :style {:font-weight "bold"}
              :font-size "14"
              :font-family "Helvetica"
              :text-anchor "middle"}
       "Games"]
      [:g {:transform (str "translate("
                           0
                           ","
                           (+ bar-height (* bar-padding 4))
                           ")")}
       [:text {:x 10
               :y (+ (/ bar-height 2)
                     (* bar-padding 5))
               :fill "#000"
               :font-size "22"
               :font-family "Helvetica"
               :text-anchor "start"}
        (string/upper-case (get data :player1/name "Player1"))]
       (rounded-rect {:x (- width 50)
                      :y 0
                      :width (- width (* bar-padding 2)
                                (- width 50))
                      :height (- bar-height (* bar-padding 2))
                      :radius radius
                      :fill "url(#player1_gradient)"})
       (rounded-rect {:x (- width 108)
                      :y 0
                      :width (- width (* bar-padding 2)
                                (- width 50))
                      :height (- bar-height (* bar-padding 2))
                      :radius radius
                      :fill "url(#player1_gradient)"})
       [:text {:x (+ (- width 50)
                     22)
               :y (+ (/ bar-height 2)
                     (* bar-padding 5))
               :fill "#FFF"
               :style {:font-weight "bold"}
               :font-size "22"
               :font-family "Helvetica"
               :text-anchor "middle"}
        (get data :player1/match_20s 0)]
       [:text {:x (+ (- width 108)
                     22)
               :y (+ (/ bar-height 2)
                     (* bar-padding 5))
               :fill "#FFF"
               :style {:font-weight "bold"}
               :font-size "22"
               :font-family "Helvetica"
               :text-anchor "middle"}
        (get data :player1/game_score 0)]]
      [:line {:x1 bar-padding :y1 71 :x2 230 :y2 71
              :stroke-width "1" :stroke"#AAA"}]
      [:g {:transform (str "translate("
                           0
                           ","
                           (+ (* bar-height 2)
                              (* bar-padding 8))
                           ")")}
       [:text {:x 10
               :y (+ (/ bar-height 2)
                     (* bar-padding 5))
               :fill "#000"
               :font-size "22"
               :font-family "Helvetica"
               :text-anchor "start"}
        (string/upper-case (get data :player2/name "Player2"))]
       (rounded-rect {:x (- width 50)
                      :y 0
                      :width (- width (* bar-padding 2)
                                (- width 50))
                      :height (- bar-height (* bar-padding 2))
                      :radius radius
                      :fill "url(#player2_gradient)"})
       (rounded-rect {:x (- width 108)
                      :y 0
                      :width (- width (* bar-padding 2)
                                (- width 50))
                      :height (- bar-height (* bar-padding 2))
                      :radius radius
                      :fill "url(#player2_gradient)"})
       [:text {:x (+ (- width 50)
                     22)
               :y (+ (/ bar-height 2)
                     (* bar-padding 5))
               :fill "#FFF"
               :style {:font-weight "bold"}
               :font-size "22"
               :font-family "Helvetica"
               :text-anchor "middle"}
        (get data :player2/match_20s 0)]
       [:text {:x (+ (- width 108)
                     22)
               :y (+ (/ bar-height 2)
                     (* bar-padding 5))
               :fill "#FFF"
               :style {:font-weight "bold"}
               :font-size "22"
               :font-family "Helvetica"
               :text-anchor "middle"}
        (get data :player2/game_score 0)]]]]))

#?(:clj
   (defn render
     [args]
     (-> (scoreboard args) html)))
