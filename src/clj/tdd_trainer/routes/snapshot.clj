(ns tdd-trainer.routes.snapshot
  (:require [tdd-trainer.session :refer :all]
            [tdd-trainer.data :as dta]
            [tdd-trainer.stats.snapshot-stats :refer :all]
            [tdd-trainer.watcher.file-watcher :refer :all]
            [tdd-trainer.differ.file-differ :refer :all]
            [compojure.core :refer [defroutes POST GET]]
            [ring.util.http-response :as response]
            [clj-time.format :as f]))

(def json-date-formatter (f/formatters :mysql))


(defn process-changed-files
  "compared a file with its previous version and sets the passed in atomic file-cache version to the new verison of the file"
  [file-cache change-list]
  
  (map
   (fn [item] (let [new-file-contents (file-to-vector-of-lines item)
                    diff (file-diff (get-previous-version file-cache item) new-file-contents)]

                (swap! file-cache assoc item new-file-contents)
                {:filename item :diff diff}))

   @change-list))


(defn format-session-data
  "formats the session data for json serialisation"
  [{:keys [session-id start-time project-root watched-file-types snapshots]}]
  {:sessionId session-id
   :startTime (f/unparse json-date-formatter start-time)
   :projectBase project-root
   :watchedFiles watched-file-types
   :snapshots (map (fn [item] {
                               :timestamp (f/unparse json-date-formatter (:snapshot-timestamp item))
                               :failingTestCount (:failing-test-count item)
                               :failingTestNames (:failing-test-names item)
                               :changedFiles (:changed-files item)})
                   snapshots)
   })

(defroutes snapshot-routes 
  (POST "/session" [timestamp projectBase watchedFiles]
        (let [new-session (create-session timestamp projectBase watchedFiles)]
          (reset! dta/session-data new-session)
          (watch-project dta/change-list projectBase (set [watchedFiles]))
          (response/created (format-session-data new-session))))
  
  (GET "/session" []
       (response/ok {:sessions [(str "http://localhost:3000/session/" (:session-id @dta/session-data))]}))


  (GET "/session/:session-id" [session-id]
       (response/ok (format-session-data @dta/session-data)))

    
  (POST "/session/:session-id/snapshot" [session-id timestamp failingTestCount failingTestNames]
        (let [snapshot-timestamp (f/parse json-date-formatter timestamp)
              changed-files (process-changed-files dta/file-cache dta/change-list)]

          (dta/update-session-data session-id {:snapshot-timestamp snapshot-timestamp
                                             :failing-test-count failingTestCount
                                             :failing-test-names failingTestNames
                                             :changed-files changed-files})
          (reset! dta/change-list #{})
          (response/ok)))


  (GET "/session/:session-id/stats" [session-id]
       (let [stats (gen-stat-summary @dta/session-data)]
         (response/ok stats))))
