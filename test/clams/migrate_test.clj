(ns clams.migrate-test
  (:require [clams.migrate :as migrate]
            [clojure.test :refer :all]
            [ragtime.jdbc :as jdbc]))

(deftest read-empty-configs-test
  (is (nil? (#'migrate/sql-config)))
  (is (nil? (#'migrate/mongo-config))))

(def fake-migrations
  [{:scheme :mongo
    :id "001-foo"
    :up (constantly "foo")
    :down (constantly "foo")}
   {:scheme :mongo
    :id "002-bar"
    :up (constantly "bar")
    :down (constantly "bar")}
   {:scheme :jdbc
    :id "003-baz"
    :up (constantly "baz")
    :down (constantly "baz")}])

(def fake-conf {:mongo-url "mongo url"})

(deftest mongo-config-test
  (with-redefs [ragtime.jdbc/load-resources (constantly fake-migrations)
                monger.core/connect-via-uri (constantly {:db "mongo handle"})
                conf.core/get (fn [k] (get fake-conf k))]
    (is (= {:database "mongo handle" :migrations (take 2 fake-migrations)}
           (#'migrate/mongo-config)))

    (let [state (atom nil)]
      (is (= nil
             (#'migrate/run-migrations (fn [c] (swap! state conj c)))))
      (is (= [{:database "mongo handle" :migrations (take 2 fake-migrations)}]
             @state)))))

(deftest load-files-test
  (let [migrations (jdbc/load-files ["/abc/migrate/001-foo.up.mongoclj"
                                     "/abc/migrate/001-foo.down.mongoclj"
                                     "/abc/migrate/002-bar.up.mongoclj"])]
    (is (= ["001-foo" "002-bar"] (map :id migrations)))
    (is (= [:mongo] (distinct (map :scheme migrations))))
    (is (= 2 (count (filter identity (map :up migrations)))))
    (is (= 2 (count (filter identity (map :down migrations)))))))
