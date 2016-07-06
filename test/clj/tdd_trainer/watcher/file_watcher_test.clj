(ns tdd-trainer.watcher.file-watcher-test
  (:require [clojure.test :refer :all]
            [clojure.set :as set]
            [midje.sweet :refer :all]
            [tdd-trainer.watcher.file-watcher :refer :all]))

(def clj-file "/users/blah/test_file1_test.clj")
(def empty-changeset (atom {}))

(def clojure-files #{".clj"})
(def scala-files #{".scala"})
(def clojure-scala-files (set/union clojure-files scala-files))

(deftest change-file-tests
  (facts "about `add-file-to-changeset`"
    (fact "should add a changed file to the given change-set"
      (add-file-to-changeset (atom {}) clj-file clojure-files) => {clj-file {:latest ["the first line" "the second line"]
                                                                             :diffs '(([nil "the first line"] [nil "the second line"]))}}
      (provided
       (file-to-vector-of-lines clj-file) => ["the first line" "the second line"]))

    (fact "should only add files that are the specified type"
      (add-file-to-changeset (atom {}) clj-file scala-files) => {}
      (add-file-to-changeset (atom {}) clj-file clojure-scala-files) => {clj-file {:latest ["the first line" "the second line"] :diffs '(([nil "the first line"] [nil "the second line"]))}}
      (provided
       (file-to-vector-of-lines clj-file) => ["the first line" "the second line"]))))
