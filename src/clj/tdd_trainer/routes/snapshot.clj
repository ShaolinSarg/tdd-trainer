(ns tdd-trainer.routes.snapshot
  (:require [tdd-trainer.session :refer :all]
            [tdd-trainer.data :as d]
            [tdd-trainer.stats.snapshot-stats :refer :all]
            [compojure.core :refer [defroutes POST GET]]
            [ring.util.http-response :as response]
            [clj-time.format :as f]))

(def json-date-formatter (f/formatters :mysql))

(defn format-session-data
  [{:keys [session-id start-time snapshots]}]
  {:sessionId session-id
   :startTime (f/unparse json-date-formatter start-time)
   :snapshots snapshots})

(defroutes snapshot-routes 
  (POST "/session" [timestamp]
        (let [new-session (create-session timestamp)]
          (reset! d/session-data new-session)
          (response/created (format-session-data new-session))))
  
  (GET "/session/:session-id/stats" [session-id]
       (let [stats (gen-stat-summary @d/session-data)]
         (response/ok stats)))
  
  (GET "/session/:session-id/snapshot" [session-id] (response/ok (format-session-data @d/session-data)))
  (POST "/session/:session-id/snapshot" [session-id timestamp failingTestCount failingTestNames]
        (do
          (d/update-session-data session-id {:timestamp timestamp
                                             :failing-test-count failingTestCount
                                             :failing-test-names failingTestNames})
          (response/ok))))

