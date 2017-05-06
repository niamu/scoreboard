(ns scoreboard.csv
  (:require [scoreboard.util :as util]
            [clojure.string :as string]
            [clojure.walk :refer [keywordize-keys]]))

#?(:clj
   (defn csv->scoreboards
     [csv]
     (let [[header & lines] (->> (string/replace csv "\r" "\n")
                                 string/split-lines
                                 (remove empty?)
                                 (map #(string/split % #",")))]
       (->> (map (fn [line]
                   (->> (interleave header line)
                        (apply hash-map)
                        keywordize-keys))
                 lines)
            (map (fn [scoreboard]
                   (assoc scoreboard
                          :timecode
                          (util/timecode->seconds (:timecode scoreboard)))))
            (sort-by :timecode)))))
