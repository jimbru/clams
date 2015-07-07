(defproject clams "0.2.0"
  :description "Clojure with Clams. A framework for web apps."
  :url "https://github.com/standardtreasury/clams"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies
    [
     [clout "2.0.0"]
     [com.novemberain/monger "3.0.0-rc2"]
     [compojure "1.2.0"]
     [http-kit "2.1.19"]
     [metosin/ring-http-response "0.5.2"]
     [org.clojure/clojure "1.6.0"]
     [org.clojure/tools.macro "0.1.5"]
     [org.postgresql/postgresql "9.3-1102-jdbc41"]
     [prismatic/schema "0.3.1"]
     [ragtime "0.4.1"]
     [ring "1.3.1"]
     [ring-mock "0.1.5"]
     [ring/ring-json "0.3.1"]
     ]
    :aot [clams.migrate])
