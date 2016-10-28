(ns clams.app
  (:require [conf.core :as conf]
            clams.middleware.save-params
            [clams.route :refer [compile-routes]]
            [clams.util :refer [str->int]]
            [clojure.tools.logging :as log]
            [org.httpkit.server :as httpkit]
            ring.middleware.http-response
            ring.middleware.json
            ring.middleware.keyword-params
            ring.middleware.nested-params
            ring.middleware.params))

(defonce ^:private server (atom nil))

;; Note that these are applied in reverse order; i.e. from bottom to top.
;;
;; To be clear: that means that the functions at the bottom of this list will
;; appear at the innermost form, wrapped one-by-one by the function above.
;;
;; In case that in turn wasn't clear, it means that wrap-params happens first,
;; then the result of that is passed to wrap-nested-params.
(def default-middleware
  "The default set of middleware applied if the :middleware option isn't
   passed to start-server. Custom middleware can be applied in addition to
   these defaults by concatenating them and passing the result using the
   :middleware option."
  [ring.middleware.http-response/wrap-http-response
   ring.middleware.keyword-params/wrap-keyword-params
   clams.middleware.save-params/wrap-save-params
   ring.middleware.json/wrap-json-params
   ring.middleware.json/wrap-json-response
   ring.middleware.nested-params/wrap-nested-params
   ring.middleware.params/wrap-params])

(defn- wrap-middleware
  [routes middleware]
  (reduce #(%2 %1) routes middleware))

(defn routes
  "Loads and compiles the app routes."
  [app-ns]
  (let [routes-ns (symbol (str app-ns ".routes"))]
    (require routes-ns)
    (compile-routes app-ns (var-get (ns-resolve routes-ns 'routes)))))

(defn app [app-ns middleware]
  (fn [request]
    ((wrap-middleware (routes app-ns) middleware) request)))

(defn start-server
  ([app-ns]
   (start-server app-ns {}))
  ([app-ns opts]
   (start-server app-ns opts httpkit/run-server))
  ([app-ns opts run-server]
   (when (nil? @server)
     (log/debug "CONF_ENV =" (conf/get :conf-env))
     (let [middleware (or (:middleware opts) default-middleware)
           port       (str->int (conf/get :port))]
       (reset! server (run-server (app app-ns middleware)
                                  {:port port
                                   :max-body (conf/get :http-max-body)}))))))

(defn stop-server
  []
  (let [stop @server]
    (when-not (nil? stop)
      (stop)
      (reset! server nil))))
