(ns scoreboard.util
  "Helper functions"
  (:require [clojure.set :refer [rename-keys]]
            [clojure.string :as string]
            [om.dom :as dom]
            [om.next.protocols :as p]
            #?@(:clj  [[sablono.normalize :as normalize]
                       [sablono.util :refer [element?]]
                       [clojure.edn :as reader]]
                :cljs [[sablono.core :as html :refer-macros [html]]
                       [cljs.reader :as reader]])))

#?(:clj
   (defn- compile-attrs
     "Conform attributes to a format expected by Om DOM utils"
     [attrs]
     (when-not (or (nil? attrs) (empty? attrs))
       (cond-> (rename-keys attrs {:class :className
                                   :for :htmlFor})
         (:class attrs) (update-in [:className] #(string/join #" " %))))))

#?(:clj
   (defn- compile-element
     "Given Hiccup structure, recursively conform the structure to one
     that Om DOM utils expect"
     [element]
     (cond
       (element? element)
       (let [[tag attrs children] (normalize/element element)]
         (dom/element {:tag tag
                       :attrs (compile-attrs attrs)
                       :children (vector (map compile-element children))}))
       (satisfies? p/IReactComponent element) element
       :else (dom/text-node (str element)))))

(defn dom
  "Convert Hiccup syntax to Om DOM snytax."
  [markup]
  #?(:clj (compile-element markup)
     :cljs (html markup)))

(defn timecode->seconds
  "Convert a string timecode such as '00:01:30' into seconds."
  [t]
  (let [[h m s] (map #(-> (string/replace % #"^0(\d)" "$1")
                          reader/read-string)
                     (string/split t #":"))]
    (+ (* h 60 60)
       (* m 60)
       s)))
