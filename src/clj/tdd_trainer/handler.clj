(ns tdd-trainer.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [tdd-trainer.layout :refer [error-page]]
            [tdd-trainer.routes.home :refer [home-routes]]
            [tdd-trainer.routes.snapshot :refer [snapshot-routes]]
            [compojure.route :as route]
            [tdd-trainer.env :refer [defaults]]
            [mount.core :as mount]
            [tdd-trainer.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'snapshot-routes
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
