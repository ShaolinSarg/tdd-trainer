(ns tdd-trainer.routes.snapshot-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [tdd-trainer.routes.snapshot :refer :all]
            [tdd-trainer.differ.file-differ :refer :all]))

(def cache (atom {"thatFile.txt" ["this" "the" "file"]}))
(def new-file (atom ["thisFile.txt"]))
(def existing-file (atom ["thatFile.txt"]))

(deftest create-file-diffs
  (facts "about `process-changed-files`"
    (fact "should return the whole new file when the file is not in the cache"
      (process-changed-files cache files) => [{:filename "thisFile.txt" :diff {:old nil :new ["this" "is" "the" "file"]}}]
      (provided
       (file-to-vector-of-lines "thisFile.txt") => ["this" "is" "the" "file"]))
    (fact "should return the diff when the file is already in the cache"
      (process-changed-files cache existing-file) => [{:filename "thatFile.txt" :diff {:old [nil "the" "file"] :new [nil "is" "the" "file"]}}]
      (provided
       (file-to-vector-of-lines "thatFile.txt") => ["this" "is" "the" "file"]))))
