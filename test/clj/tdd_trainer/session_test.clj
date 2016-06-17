(ns tdd-trainer.session-test
  (:require [tdd-trainer.session :refer :all]
            [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [clj-time.core :as t]))

(def start-time (t/date-time 2016 4 6 12 14 04))

(deftest creating-session-tests
  (facts "about `create-session`"
    (fact "it must return a map with a session id"
      (create-session "2016-04-06 12:14:04") => {:session-id 111 :start-time start-time :snapshots[]})))
