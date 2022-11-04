(ns paste-confluence.core
  (:require
    [paste-confluence.confluence :as confluence]))

(defn slurp-csv [f]
  (map #(-> % .trim (.split ","))
       (-> f slurp .trim (.split "\n"))))

(defn extract-fmt [s]
  (re-seq #"%\w+" s))

(defn to-confluence []
  (let [[[fmt] & rows] (slurp-csv "resources/table.csv")
        fmt-syms (extract-fmt fmt)
        rows (take-while #(-> % first not-empty) rows)]
    (prn fmt-syms (count rows))
    (assert (= (count fmt-syms) (count rows)))
    (confluence/to-clipboard
      (map cons fmt-syms rows))))

(defn to-confluence-simple []
  (let [rows (filter #(-> % first not-empty) (slurp-csv "resources/table.csv"))
        fmt-syms (repeat "")]
    (confluence/to-clipboard
      (map cons fmt-syms rows))))
