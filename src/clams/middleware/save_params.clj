(ns clams.middleware.save-params
  "Middleware that converts saves the parameters map in the request
   object.")

(defn save-params-request
  "Save the current params map in the request under SAVE-KEY."
  [request save-key]
  (assoc request save-key (:params request)))

(defn wrap-save-params
  "Middleware that saves the parameters map in the request object.  By
   default the params are stored in the :saved-params key."
  ([handler] (wrap-save-params handler :saved-params))
  ([handler save-key]
   (fn [request]
     (handler (save-params-request request save-key)))))
