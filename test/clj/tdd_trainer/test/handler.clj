(ns tdd-trainer.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [tdd-trainer.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response)))))

  (testing "post session route"
    (let [response ((app) (request :post "/session"))]
      (is (= 201 (:status response)))))

  (testing "post snapshot route"
    (let [response ((app) (request :post "/session/111/snapshot"))]
      (is (= 200 (:status response)))))
  
  (testing "get snapshot route"
    (let [response ((app) (request :get "/session/111/snapshot"))]
      (is (= 200 (:status response)))))

  (testing "get snapshot route"
    (let [response ((app) (request :get "/session/111/stats"))]
      (is (= 200 (:status response))))))
