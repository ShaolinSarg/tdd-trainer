(ns tdd-trainer.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[tdd_trainer started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[tdd_trainer has shut down successfully]=-"))
   :middleware identity})
