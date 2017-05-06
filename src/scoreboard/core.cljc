(ns scoreboard.core
  #?(:clj (:gen-class))
  (:require [clojure.string :as string]
            [scoreboard.util :as util]
            [scoreboard.svg :as svg]
            [scoreboard.csv :as csv]
            #?@(:clj [[scoreboard.ffmpeg :as ffmpeg]
                      [me.raynes.conch.low-level :as sh]])
            [om.next :as om #?(:clj :refer :cljs :refer-macros) [defui]]
            [om.dom :as dom :include-macros true]))

(defonce app-state
  (atom {:player1/name "Beierling"
         :player1/match_20s "0"
         :player1/game_score "2"
         :timecode "00:00:02"
         :player2/name "Slater"
         :player2/match_20s "1"
         :player2/game_score "0"}))

(defui Root
  Object
  (render [this]
    (let [state (om/props this)]
      (-> [:div (svg/scoreboard state)]
          util/dom))))

(om/add-root! (om/reconciler {:state app-state})
              Root
              #?(:clj  nil
                 :cljs (. js/document (getElementById "app"))))

#?(:clj
   (defn -main
     [& args]
     ;; Read in a CSV file of scoreboard data and output an SVG image
     ;; of each scoreboard image with the filename being the timestamp.
     ;; Finally, convert each SVG image to a PNG with librsvg (rsvg-convert)
     (let [in-files (-> (sh/proc "ls" "-1" "resources/in/")
                        (sh/stream-to-string :out)
                        string/split-lines)
           csv-file (-> (filter #(string/includes? % ".csv") in-files)
                        first)
           vid-file (-> (remove #(string/includes? % ".csv") in-files)
                        first)
           scoreboards (csv/csv->scoreboards (slurp (str "resources/in/"
                                                         csv-file)))]
       (doall
        (map (fn [scoreboard]
               (let [filename (:timecode scoreboard)]
                 (spit (str "/tmp/" filename ".svg")
                       (svg/render scoreboard))
                 (sh/proc "rsvg-convert"
                          "--format=png"
                          (str "--output=/tmp/" filename ".png")
                          (str "/tmp/" filename ".svg"))))
             scoreboards))
       (spit "script/ffmpeg.sh"
             (str "#!/bin/sh\n\n"
                  (ffmpeg/run vid-file scoreboards))))))
