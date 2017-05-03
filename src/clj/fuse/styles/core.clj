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
    {:padding (px 40)}
    [:div.wrapper
     {:width (percent 75)}]
    module
    global
    [:div.create {:width (percent 75)
                  :margin [[(px 40) 0]]}]
    [:div.results {:width (percent 75)
                   :margin [[(px 40) 0]]}]]])
