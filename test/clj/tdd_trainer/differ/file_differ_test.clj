(ns tdd-trainer.differ.file-differ-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [tdd-trainer.differ.file-differ :refer :all]))

(def file1 ["this is the first line" "this is the second line" "this is the 3rd line" "this is the 4th line"])

(def file2 ["this is the first line" "this is the 2nd line" "this is the 3rd line" "this is the forth line"])

(def file-cache {"test_file1.clj" file1})

(def old-file1 [nil nil nil "def convert(num: Int): String = ???"])
(def old-file2 [nil nil nil "def convert(num: Int): String = \"I\""])



(deftest file-differ-tests
  (facts "about `file-diff`"
    (fact "must return the whole file if it is new"
      (file-diff file1 file2) => {:old  [nil "this is the second line" nil "this is the 4th line"]
                                  :new [nil "this is the 2nd line" nil "this is the forth line"]}))


  (facts "about `get-previous-version`"
    (fact "should return an empty collection if the file has not been saved before"
      (get-previous-version file-cache "filename") => [])

    (fact "should return the contents of the cached file if found"
      (get-previous-version file-cache "test_file1.clj") => file1))

  (facts "about `reduce-diff`"
    (fact "it should strip out leading nil values"
      (reduce-diff old-file1 old-file2) => '(["def convert(num: Int): String = ???"
                                              "def convert(num: Int): String = \"I\""]))))
