(ns paste-confluence.confluence
  (:require
    [clojure.java.io :as io]
    [hiccup.core :refer [html]]
    [paste-confluence.clipboard :as clipboard]))

(def table (-> "table.html" io/resource slurp))
(def subtable (-> "subtable.html" io/resource slurp .trim))

(def template (.replace table subtable "flipz"))

(defn td [item]
  [:td.confluenceTd [:span item]])
(defn row [items]
  [:tr (map td items)])
(defn rows [itemss]
  (map row itemss))

(defn to-clipboard [items]
  (->> items rows html (.replace template "flipz") clipboard/spit-clipboard))
