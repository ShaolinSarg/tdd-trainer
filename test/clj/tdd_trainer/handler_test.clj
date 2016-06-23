(ns tdd-trainer.handler-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [ring.mock.request :refer :all]
            [tdd-trainer.handler :refer :all]))

(deftest handler-tests
  (facts "about `/session/:id` route"
    (fact "it should respond to GET requests with OK"
      (let [response ((app) (request :get "/session/111"))]
        (:status response) => 200)))

  (facts "about `/session` route"
    (fact "it should respond to POST requests with CREATED"
      (let [response ((app) (request :post "/session" {:timestamp "2016-04-04 14:13:55"}))]
        (:status response) => 201)))

  (facts "about `not-found` routes"
    (fact "it should return the 404 http status"
      (let [response ((app) (request :get "/invalid"))]
        (:status response) => 404)))

  (testing "post snapshot route"
    (let [response ((app) (request :post "/session/111/snapshot" {:timestamp "2016-04-04 14:13:55"}))]
      (is (= 200 (:status response)))))
  
  (testing "get snapshot route"
    (let [response ((app) (request :get "/session/111/stats"))]
      (is (= 200 (:status response))))))
