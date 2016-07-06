(ns tdd-trainer.routes.snapshot-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [tdd-trainer.routes.snapshot :refer :all]
            [tdd-trainer.differ.file-differ :refer :all]))

(def cache (atom {"thatFile.txt" {:latest ["this" "the" "file"]}}))
(def new-file (atom ["thisFile.txt"]))
(def existing-file (atom ["thatFile.txt"]))

(deftest create-file-diffs
  ;; (facts "about `process-changed-files`"
  ;;   (fact "should return the whole new file when the file is not in the cache"
  ;;     (process-changed-files cache new-file) => [{:filename "thisFile.txt" :diff '([nil "this"] [nil "is"] [nil "the"] [nil "file"])}]
  ;;     (provided
  ;;      (file-to-vector-of-lines "thisFile.txt") => ["this" "is" "the" "file"]))
    
  ;;   (fact "should return the diff when the file is already in the cache"
  ;;     (process-changed-files cache existing-file) => [{:filename "thatFile.txt" :diff '(["the" "is"] ["file" "the"])}]
  ;;     (provided
  ;;      (file-to-vector-of-lines "thatFile.txt") => ["this" "is" "the" "file"])))
  )
