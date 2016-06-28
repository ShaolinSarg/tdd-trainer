(ns tdd-trainer.watcher.file-watcher-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [tdd-trainer.watcher.file-watcher :refer :all]))

;; add files to list of changes for a snapshot
;; add list of files changed to snapshot

(def clj-file "/users/blah/test_file1_test.clj")

(deftest change-file-tests
  (facts "about `add-file-to-changeset`"
    (fact "should add a changed file to the given change-set"
      (add-file-to-changeset (atom #{}) clj-file #{".clj"}) => #{clj-file})

    (fact "should only add files that are the specified type"
      (add-file-to-changeset (atom #{}) clj-file #{".scala"}) => #{}
      (add-file-to-changeset (atom #{}) clj-file #{".scala" ".clj"}) => #{clj-file})))
