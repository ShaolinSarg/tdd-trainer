(ns tdd-trainer.data)

(def session-data (atom nil))

(def change-list (atom #{}))

(def file-cache (atom {}))

(defn add-snapshot-to-session
  "adds a snapshot to the given tdd session"
  [session session-id snapshot]

  (let [snapshots (:snapshots session)]
    (update session :snapshots conj snapshot)))


(defn update-session-data
  "updates the session data store"
  [session-id snapshot]

  (reset! session-data (add-snapshot-to-session @session-data session-id snapshot)))
