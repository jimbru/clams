(ns clams.test.middleware.save-params-test
  (:require [clojure.test :refer :all]
            [clams.middleware.save-params :refer :all]))

(deftest save-params-request-test
  (let [params {:params {"algeria" 11
                         "Bulgaria" "Cambodia"
                         "dominica egypt " {"x" 22 "y" "france"}
                         33 "gambia"}}
        save (save-params-request params :saved-params)]
    (is (= (:saved-params save) (:params params))))
  (is (or (save-params-request nil :saved-params) true)) ; Should not throw an exception
  (is (nil? (:params (save-params-request {:params nil} :saved-params))))
  (is (empty? (:params (save-params-request {:params {}} :saved-params)))))
