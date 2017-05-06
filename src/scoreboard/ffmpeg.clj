(ns scoreboard.ffmpeg
  (:require [clojure.edn :as reader]
            [clojure.string :as string]
            [scoreboard.svg :as svg]
            [me.raynes.conch.low-level :as sh]))

(def scoreboard-scale
  (str svg/width ":" (int (* svg/height (/ 4 3)))))

(defn inputs
  [vid-file scoreboards]
  ["-i"
   (str "resources/in/" vid-file)
   (map #(vector "-i" (str "/tmp/" (:timecode %) ".png"))
        scoreboards)])

(defn duration
  [vid-file]
  (-> (sh/proc "ffprobe"
               "-i" (str "resources/in/" vid-file)
               "-v" "error"
               "-show_entries" "format=duration"
               "-of" "default=noprint_wrappers=1:nokey=1")
      (sh/stream-to-string :out)
      reader/read-string))

(defn bitrate
  [vid-file]
  (-> (sh/proc "ffprobe"
               "-i" (str "resources/in/" vid-file)
               "-v" "error"
               "-show_entries" "format=bit_rate"
               "-of" "default=noprint_wrappers=1:nokey=1")
      (sh/stream-to-string :out)
      reader/read-string
      (/ 1024)
      int))

(defn run
  [vid-file scoreboards]
  (->> ["ffmpeg"
        (inputs vid-file scoreboards)
        "-codec:a" "copy"
        "-codec:v" "libx264"
        "-b:v" (str (bitrate vid-file) "k")
        "-filter_complex"
        (str \"
             (->> (map-indexed
                   (fn [idx [s1 s2]]
                     [(str "[" (inc idx) ":v]scale="
                           scoreboard-scale)
                      (str "[overlay" (inc idx) "],"
                           (if (= idx 0)
                             "[0:v]"
                             (str "[tmp" (dec idx) "]"))
                           "[overlay" (inc idx) "]overlay="
                           "150:(main_h-overlay_h)-150"
                           ":enable='between(t,"
                           (:timecode s1) "," (or (get s2 :timecode nil)
                                                  (duration vid-file)) ")'"
                           (when-not (nil? (get s2 :timecode nil))
                             (str "[tmp" idx "];")))])
                   (partition 2 1 (flatten [(map #(select-keys % [:timecode])
                                                 scoreboards) nil])))
                  doall
                  flatten
                  (apply str))
             \")
        (str "resources/out/" vid-file)]
       flatten
       (interpose " ")
       (apply str)))
