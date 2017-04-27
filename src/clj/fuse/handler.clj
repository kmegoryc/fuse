(ns fuse.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [fuse.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]))

(def mount-target
  [:div#app
   [:h3 "ClojureScript has not been compiled!"]
   [:p "please run "
    [:b "lein figwheel"]
    " in order to start the compiler"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css "//cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.2.2/semantic.min.css"
                (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
    (head)
    [:body {:class "body-container"}
     mount-target
     (include-js "/js/app.js")]))

(def a (atom {}))

(defn update-modules
  [request]
  ;;do our server backend stuff
  ;;(prn "hi this worked!")
  (clojure.pprint/pprint {:request request})
  (spit "modules.edn" request) ;; <- the backend stuff
  ;;tell the browser everything went ok
  {:status 200
   :body "update successful!"})

(defn slurp-data
  []
  (slurp "modules.edn"))

(defroutes routes
  (GET "/" [] (loading-page))
  (GET "/student" [] (loading-page))
  (GET "/teacher" [] (loading-page))
  (POST "/teacher" request (update-modules request))
  (GET "/data" [] (slurp-data))

  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
