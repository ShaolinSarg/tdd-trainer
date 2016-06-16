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

(ns tdd-trainer.stats.snapshot-stats
  (:require [clj-time.core :as t]))


(defn diff-times
  [start end]

  (t/in-seconds (t/interval start end)))


(defn snapshot-gaps
  "returns a list of the gaps between each snapshot"
  [start snapshots]

  (loop [out []
         s start
         svs snapshots]
    (if (empty? svs)
      out
      (let [snapshot-time (:snapshot-timestamp (first svs))]
        (recur (conj out (diff-times s snapshot-time)) snapshot-time (rest svs))))))


(defn average-snapshot-gap
  "returns the average gap inbetween snapshots"
  [gaps]

  (double (/ (reduce + gaps) (count gaps))))

(defn gen-stat-summary
  "generates the map containing all of the required stats"
  [session-id start snapshots]
  (let [gaps (snapshot-gaps start snapshots)
        ave-save-time (average-snapshot-gap gaps)]
    {:average-save-time ave-save-time}))