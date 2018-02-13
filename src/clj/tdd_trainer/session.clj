(ns tdd-trainer.session
  (require [clj-time.format :as f]))

(def json-datetime-formatter (f/formatters :mysql))

(defn create-session 
  "returns an initialised tdd session with a session id and start time"
  [start-time project-root watched-files]
  {:session-id (rand-int 999)
   :project-root project-root
   :start-time (f/parse json-datetime-formatter start-time)
   :watched-file-types watched-files
   :snapshots []})
