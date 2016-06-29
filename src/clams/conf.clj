(ns clams.conf
  "Provides app configuration. See https://github.com/jimbru/conf.
  Uses conf env-key `CLAMS_ENV`."
  (:refer-clojure :rename {get core-get set! core-set!})
  (:require [clams.util :refer [redef]]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [conf.core :as conf]))

(redef conf.core [get get-all loaded? set! unload!])

(defn load!
  "See conf.core/load!. Coerced to use CLAMS_ENV."
  []
  (conf/load! :clams-env))
