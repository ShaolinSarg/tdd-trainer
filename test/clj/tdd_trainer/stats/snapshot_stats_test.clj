;; Copyright 2016 David Sarginson

;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at

;; http://www.apache.org/licenses/LICENSE-2.0

;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns tdd-trainer.stats.snapshot-stats-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [clj-time.core :as t]
            [tdd-trainer.stats.snapshot-stats :refer :all]))

(def session-start (t/date-time 2016 6 6 22 11 00))
(def snapshots [{:snapshot-timestamp (t/date-time 2016 6 6 22 11 12)}
            {:snapshot-timestamp (t/date-time 2016 6 6 22 12 12)}
            {:snapshot-timestamp (t/date-time 2016 6 6 22 12 52)}
            {:snapshot-timestamp (t/date-time 2016 6 6 22 14 52)}])


(deftest save-time-tests
  (facts "about `snapshot-gaps`"
    (fact "it should return the difference between each snapshot in seconds"
      (snapshot-gaps session-start snapshots) => [12 60 40 120]))

  (facts "about `average-snapshot-gap`"
    (fact "it should return the average snapshot gap"
      (average-snapshot-gap [12 60 40 120]) => 58.0)))



(deftest output-tests
  (facts "about 'gen-stat-summary'"
    (fact "should contain the average save time"
      (gen-stat-summary 111 (t/now) {:snapshots snapshots}) => {:average-save-time 13})))
