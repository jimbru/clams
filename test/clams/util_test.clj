(ns clams.util-test
  (:require [clams.util :refer :all]
            clams.util-test-helper
            [clojure.test :refer :all]))

(deftest fully-qualified-symbol-test
  (dorun
    (for [args [[["foo" "bar"] 'foo/bar]
                [['foo 'bar] 'foo/bar]
                [['foo "bar"] 'foo/bar]
                [["foo" 'bar] 'foo/bar]
                [['foo.bar "baz"] 'foo.bar/baz]]]
      (is (= (apply fully-qualified-symbol (first args)) (second args))))))

;; Apparently tests are run under their own namespaces.
;; We'll make a note of this one so that we might inspect it later.
(def this-ns *ns*)

(redef clams.util-test-helper [v1 f1])

(deftest redef-test
  (let [publics     (ns-publics this-ns)
        get-public  #(var-get (get publics %))]
    (is (contains? publics 'v1))
    (is (= (get-public 'v1) "thing"))
    (is (contains? publics 'f1))
    (is (= ((get-public 'f1)) "thing"))))

(redefmacro clams.util-test-helper [m1])

(deftest redefmacro-test
  (let [publics (ns-publics this-ns)]
    (is (contains? publics 'm1))
    (is (= (:macro (meta (get publics 'm1))) true))))

(deftest test-str->int
  (testing "str->int"
    (is (= (str->int "0") 0))
    (is (= (str->int "1") 1))
    (is (= (str->int "-1") -1))
    (is (= (str->int "123") 123))
    (is (= (str->int 123) 123))
    (is (= (str->int -1) -1))
    (is (= (str->int "2147483647") 2147483647))
    (is (= (str->int "2147483648") 2147483648))
    (is (nil? (str->int nil)))))
