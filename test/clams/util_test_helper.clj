(ns clams.util-test-helper)

(def v1 "thing")

(defn f1
  []
  "thing")

(defmacro m1
  [x]
  `(do [~x]))
