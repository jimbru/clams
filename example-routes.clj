(def routes
  "example routes"
  [[GET   "/"            :root-version]
   [GET   "/v1"          :root-version]
   [GET   "/v1/ach"      :ach-index]
   [POST  "/v1/ach"      :ach-create]
   [GET   "/v1/ach/:id"  :ach-get]
   [PATCH "/v1/ach/:id"  :ach-update]
   ])
