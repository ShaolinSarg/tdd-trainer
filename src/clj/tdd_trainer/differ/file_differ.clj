(ns tdd-trainer.differ.file-differ
  (:require [clojure.data :as d]
            [clojure.string :as str]))

(defn file-to-vector-of-lines
  [file]
  (str/split-lines (slurp file)))

(defn reduce-diff
  "takes the output of clojures diff function and removes irrelevent rows"
  [old-data new-data]
  (let [tups (map vector old-data new-data)
        just-diffs (filter (fn [[o n]] (not= o n)) tups)]
    just-diffs))

(defn file-diff 
  "returns the diff of 2 vectors containing the lines of each file"
  [file1-contents file2-contents]
  (let [[old new both] (d/diff file1-contents file2-contents)
        reduced-diff (reduce-diff old new)]
    reduced-diff))

(defn get-previous-version
  "returns the previous version of the file as a colleciton of lines or empty collection if not found"
  [cache filename]
  (get @cache filename []))
