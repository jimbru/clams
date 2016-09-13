(defproject clams "0.3.5"
  :description "Clojure with Clams. A framework for web apps."
  :url "https://github.com/standardtreasury/clams"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[clout "2.1.2"]
                 [com.novemberain/monger "3.0.0-rc2"]
                 [compojure "1.4.0" :exclusions [ring/ring-codec
                                                 ring/ring-core]]
                 [conf "0.9.1"]
                 [http-kit "2.1.19"]
                 [metosin/ring-http-response "0.6.5" :exclusions [ring/ring-core]]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/tools.macro "0.1.5"]
                 [org.postgresql/postgresql "9.4.1210"]
                 [prismatic/schema "0.3.1"]
                 [ragtime "0.4.1"]
                 [ring "1.5.0"]
                 [ring-mock "0.1.5" :exclusions [ring/ring-codec]]
                 [ring/ring-json "0.4.0"]]
  :aot [clams.migrate])
