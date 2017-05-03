(ns fuse.styles.core
  (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px em percent]]))

(def global
  [:div.field {:padding 0}])

(def module
  [:div.module {:padding (px 40)
                :border-radius (px 5)
                :border "1px solid lightgrey"
                :margin [[(px 10) 0]]}
   [:div.teacher-header {:margin-top (px 10)}]])

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
                   :margin [[(px 40) 0]]}]
    [:div.submissions {:width (percent 50)
                       :margin [[(px 40) 0]]}]]])
