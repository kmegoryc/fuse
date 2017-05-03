(ns fuse.styles.core
  (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px em percent]]))

(def global
  [:div.field {:padding 0}])

(def module
  [:div.module {:padding (px 40)
                :border-radius (px 5)
                :border "1px solid lightgrey"
                :margin [[(px 20) 0]]}
   [:div.teacher-header {:margin-top (px 10)}]])

(defstyles styles
  [:div.page
   [:.menu {:border-radius 0}]
   [:div.page-content
    {:margin [[(px 80) (px 200)]]}
    module
    global
    [:div.create {:margin [[(px 40) :auto]]}]
    [:div.results {:margin [[(px 40) :auto]]}]
    [:div.submissions {:margin [[(px 40) :auto]]}]]])
