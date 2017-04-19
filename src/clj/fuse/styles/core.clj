(ns fuse.styles.core
  (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px em percent]]))

(defstyles styles
  [:div.wrapper
   [:div.create
    {:margin (px 40)}
    [:.create-form
     {:width (percent 75)
      :padding (px 40)}]]])
