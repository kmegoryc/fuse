(ns fuse.styles.core
  (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px em percent]]))

(def global
  [:div.field {:padding 0}])

(def module
  [:div.module {:padding [[(px 30) 0]]}
   [:.teacher-header {:margin-left (px 55)}]
   [:div.display {:display "flex"
                  :align-items "center"}
    [:.progress {:width (percent 100)
                 :margin "1.5em 1em"}]]])

(defstyles styles
  [:div.page
   [:.menu {:border-radius 0}]
   [:div.page-content
    {:width (percent 75)
     :padding (px 40)}
    module
    global
    [:div.create {:margin [[0 0 (px 80) 0]]}]
    [:div.results {:margin [[0 0 (px 80) 0]]}]]])
