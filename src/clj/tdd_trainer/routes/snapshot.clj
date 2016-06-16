(ns tdd-trainer.routes.snapshot
  (:require [tdd-trainer.data :as d]
            [tdd-trainer.stats.snapshot-stats :refer :all]
            [compojure.core :refer [defroutes POST GET]]
            [ring.util.http-response :as response]))


(defroutes snapshot-routes
  (POST "/session" [timestamp] (response/created {:session-id 111 :snapshots []}))
  (GET "/session/:session-id/stats" [session-id] (response/ok))

  (GET "/session/:session-id/snapshot" [session-id] (response/ok @d/session-data))
  (POST "/session/:session-id/snapshot" [session-id timestamp failingTestCount failingTestNames]
        (do
          (d/update-session-data session-id {:timestamp timestamp
                                             :failing-test-count failingTestCount
                                             :failing-test-names failingTestNames})
          (response/ok))))

