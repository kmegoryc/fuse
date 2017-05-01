(ns fuse.middleware
  (:require [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.format-params :refer [wrap-transit-json-params]]
            [ring.middleware.format-response :refer [wrap-transit-json-response]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]))

(defn wrap-middleware [handler]
  (-> handler
    (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
    wrap-transit-json-params
    wrap-transit-json-response
    wrap-exceptions
    wrap-reload))
