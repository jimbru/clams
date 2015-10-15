(ns clams.test.keyword-params-test
  (:require [clojure.test :refer :all]
            [clams.keyword-params :refer :all]))

(deftest keyword-params-request-test
  (let [p {:params {"algeria" 11
                    "Bulgaria" "Cambodia"
                    "dominica egypt " {"x" 22 "y" "france"}
                    33 "gambia"}}
        k (keyword-params-request p)]
    (is (= (:string-params k) (:params p)))
    (is (not= (:params k) (:params p)))
    (is (= {:algeria 11, 33 "gambia", "dominica egypt " {:x 22, :y "france"}, :Bulgaria "Cambodia"}
           (:params k))))
  (is (or (keyword-params-request nil) true)) ; Should not throw an exception
  (is (nil? (:params (keyword-params-request {:params nil}))))
  (is (empty? (:params (keyword-params-request {:params {}})))))
