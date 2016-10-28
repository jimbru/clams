(ns clams.route-test
  (:require [clams.route :refer :all]
            [clojure.test :refer :all]))

(deftest controller-test
  (doseq [[app-ns route-key result]
          [["app"         :foo         ['app.controllers 'foo]]
           ["app"         :foo-bar     ['app.controllers.foo 'bar]]
           ["app"         :foo-bar-baz ['app.controllers.foo.bar 'baz]]
           ["app.sub"     :foo-bar     ['app.sub.controllers.foo 'bar]]
           ["app.sub.sub" :foo-bar     ['app.sub.sub.controllers.foo 'bar]]
           ["app"         :FoO         ['app.controllers 'foo]]
           ["app"         :FOO-BAR     ['app.controllers.foo 'bar]]]]
    (is (= (controller app-ns route-key) result))))

;;;;
;; ng
;;

(def compile-routes #'clams.route/compile-routes)

(deftest compile-routes-test
  (is (= {} (compile-routes [])))
  (is (= {"/" {:get :foo}} (compile-routes [[GET "/" :foo]])))
  (is (= {"/" {:get :foo
               :post :bar}
          "/1" {:get :foo}}
         (compile-routes [[GET "/" :foo]
                          [POST "/" :bar]
                          [GET "/1" :foo]])))
  (is (thrown? AssertionError (compile-routes [[GET "/" :foo]
                                               [GET "/1" :bar]
                                               [GET "/" :baz]]))))
