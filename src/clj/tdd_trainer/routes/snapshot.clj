(ns tdd-trainer.routes.snapshot
  (:require [tdd-trainer.session :refer :all]
            [tdd-trainer.data :as d]
            [tdd-trainer.stats.snapshot-stats :refer :all]
            [compojure.core :refer [defroutes POST GET]]
            [ring.util.http-response :as response]
            [clj-time.format :as f]))

(def json-date-formatter (f/formatters :mysql))

(defn format-session-data
  "formats the session data for json serialisation"
  [{:keys [session-id start-time project-root snapshots]}]
  {:sessionId session-id
   :startTime (f/unparse json-date-formatter start-time)
   :projectBase project-root
   :snapshots (map (fn [item] {
                               :timestamp (f/unparse json-date-formatter (:timestamp item))
                               :failingTestCount (:failing-test-count item)
                               :failingTestNames (:failing-test-names item)})
                   snapshots)
   })

(defroutes snapshot-routes 
  (POST "/session" [timestamp projectBase]
        (let [new-session (create-session timestamp projectBase)]
          (reset! d/session-data new-session)
          (response/created (format-session-data new-session))))
  
  (GET "/session" []
       (response/ok {:sessions [(str "http://localhost:3000/session/" (:session-id @d/session-data))]}))


  (GET "/session/:session-id" [session-id]
       (response/ok (format-session-data @d/session-data)))

    
  (POST "/session/:session-id/snapshot" [session-id timestamp failingTestCount failingTestNames]
        (do
          (d/update-session-data session-id {:snapshot-timestamp (f/parse json-date-formatter timestamp)
                                             :failing-test-count failingTestCount
                                             :failing-test-names failingTestNames})
          (response/ok)))


  (GET "/session/:session-id/stats" [session-id]
       (let [stats (gen-stat-summary @d/session-data)]
         (response/ok stats))))

