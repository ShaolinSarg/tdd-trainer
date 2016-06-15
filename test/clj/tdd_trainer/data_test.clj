(ns tdd-trainer.data-test
  (:require [tdd-trainer.data :refer :all]
            [clojure.test :refer :all]))

(def snapshot {:time "1" :fails 3 :files ["one" "two" "three"]})


(deftest data-tests
  (testing "adding a snapshot to session should return the updated session"
    (is (= {:session-id 111 :snapshots [snapshot]}
           (add-snapshot-to-session {} 111 snapshot))))

  (testing "adding multiple snapshots to session should return the updated session with all snapshots"
    (is (= 2 (count (:snapshots (add-snapshot-to-session (add-snapshot-to-session {} 111 snapshot) 111 snapshot)))))))
