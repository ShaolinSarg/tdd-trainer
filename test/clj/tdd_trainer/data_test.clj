(ns tdd-trainer.data-test
  (:require [tdd-trainer.data :refer :all]
            [clojure.test :refer :all]
            [clj-time.core :as t]))

(def start-time (t/date-time 2016 5 5 12 24 42))

(def snapshot {:time "1" :fails 3 :files ["one" "two" "three"]})


(deftest data-tests
  (testing "adding a snapshot to session should return the updated session"
    (is (= {:session-id 111 :start-time start-time :snapshots [snapshot]}
           (add-snapshot-to-session {:session-id 111 :start-time start-time :snapshots []} 111 snapshot))))

  (testing "adding multiple snapshots to session should return the updated session with all snapshots"
    (is (= 2 (count (:snapshots (add-snapshot-to-session (add-snapshot-to-session {} 111 snapshot) 111 snapshot)))))))
