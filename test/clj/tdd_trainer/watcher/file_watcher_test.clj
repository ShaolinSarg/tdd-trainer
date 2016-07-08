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

(def session-data
  (atom {
         :session-id 626
         :start-time "2016-07-07 11:00:39"
         :project-root "/Users/davidsarginson/Development/repos/scala/ScalaSeed"
         :watched-file-types ".scala"
         :snapshots [{
                      :snapshot-timestamp "2016-07-07 11:00:43"
                      :failing-test-count "1"
                      :failing-test-names "numerals.RomanNumeralsSpec"
                      :changed-files {}}
                     {
                      :snapshot-timestamp "2016-07-07 11:01:32"
                      :failing-test-count "0"
                      :failing-test-names ""
                      :changed-files {
                                      "file1.clj" {
                                                   :latest [
                                                            "package numerals"
                                                            ""
                                                            "object RomanNumerals {"
                                                            "  def converter(x: Int): String = if (x == 1) \"I\" else \"V\""
                                                            "}"]}}}]}))


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


(deftest old-version-tests
  (facts "about `get-latest-from-session`"
    (fact "should return the latest version of a file from session"
      (get-latest-from-session session-data "file1.clj") => ["package numerals" "" "object RomanNumerals {" "  def converter(x: Int): String = if (x == 1) \"I\" else \"V\"" "}"])))
