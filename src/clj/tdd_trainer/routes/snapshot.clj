(ns tdd-trainer.routes.snapshot
  (:require [tdd-trainer.data :as d]
            [compojure.core :refer [defroutes POST GET]]
            [ring.util.http-response :as response]))


(defroutes snapshot-routes
  (GET "/session/:session-id/snapshot" [session-id] (response/ok @d/session-data))
  (POST "/session/:session-id/snapshot" [session-id timestamp failingTestCount failingTestNames]
        (do
          (d/update-session-data session-id {:timestamp timestamp
                                             :failing-test-count failingTestCount
                                             :failing-test-names failingTestNames})
          (response/ok))))

