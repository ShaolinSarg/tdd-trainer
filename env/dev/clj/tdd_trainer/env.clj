(ns tdd-trainer.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [tdd-trainer.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[tdd_trainer started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[tdd_trainer has shut down successfully]=-"))
   :middleware wrap-dev})
