(ns clams.app
  (:require [clams.conf :as conf]
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
(defonce default-middleware
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

(defn app [app-ns app-middleware]
  (let [handler (wrap-middleware (routes app-ns)
                                 (concat default-middleware app-middleware))]
    (fn [request]
      (handler request))))

(defn start-server
  ([app-ns]
   (start-server app-ns {}))
  ([app-ns opts]
   (start-server app-ns opts httpkit/run-server))
  ([app-ns opts run-server]
    (when (nil? @server)
      (conf/load!)
      (log/debug "CLAMS_ENV =" (conf/get :clams-env))
      (let [middleware (:middleware opts)
            port       (str->int (conf/get :port))]
        (reset! server (run-server (app app-ns middleware) {:port port}))))))

(defn stop-server
  []
  (let [stop @server]
    (when-not (nil? stop)
      (stop)
      (reset! server nil))))
