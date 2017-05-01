(ns fuse.styles.core
  (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px em percent]]))

(def global
  [:div.field {:padding 0}])

(defstyles styles
  [:div.page
   [:.menu {:border-radius 0}]
   [:div.content
    {:margin (px 40)}
    [:div.module {:margin [[(px 20) 0]]}]
    global
    [:.create-form
     {:width (percent 75)
      :padding (px 40)}]]])
