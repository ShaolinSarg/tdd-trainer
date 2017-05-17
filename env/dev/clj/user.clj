(ns user
  (:require [mount.core :as mount]
            tdd-trainer.core))

(defn start []
  (mount/start-without #'tdd-trainer.core/repl-server))

(defn stop []
  (mount/stop-except #'tdd-trainer.core/repl-server))

(defn restart []
  (stop)
  (start))


