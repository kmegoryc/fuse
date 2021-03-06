(ns fuse.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [semantic-ui.core :refer [$]]
            [fuse.pages.student :as student]
            [fuse.pages.teacher :as teacher]
            [fuse.pages.home :as home]))

;; -------------------------
;; Views

(defn current-page []
  [:div.page
   [(session/get :current-page)]])

(defn set-page!
  [page]
  (when-let [current-page (session/put! :current-page page)]
    current-page)
  (.scrollTo js/window 0 0))

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (set-page! #'home/home-page))

(secretary/defroute "/teacher" []
  (set-page! #'teacher/teacher-page))

(secretary/defroute "/student" []
  (set-page! #'student/student-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
