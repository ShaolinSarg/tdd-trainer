(ns tdd-trainer.differ.file-differ
  (:require [clojure.data :as d]
            [clojure.string :as str]))

(defn file-to-vector-of-lines
  [file]
  (str/split-lines (slurp file)))

(defn file-diff 
  "returns the diff of 2 vectors containing the lines of each file"
  [file1-contents file2-contents]
  (let [[old new both] (d/diff file1-contents file2-contents)]
    {:old old :new new}))

(defn get-previous-version
  "returns the previous version of the file as a colleciton of lines or empty collection if not found"
  [cache filename]
  (get @cache filename []))
