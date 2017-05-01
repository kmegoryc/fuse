(ns fuse.pages.components
  (:require [semantic-ui.core :refer [$]]))

(defn button
  [name option1 option2]
  [:div
   [:> ($ :Header) {:size "medium"} name]
   [:> ($ :Button.Group)
    [:> ($ :Button) option1]
    [:> ($ :Button.Or)]
    [:> ($ :Button) option2]]])

(defn slider
  [name option1 option2]
  [:div
   [:> ($ :Header) {:size "medium"} name]
   [:input {:min 0
            :max 20
            :type "range"
            :defaultValue 10}]])

(defn poll
  [name option1 option2]
  [:div
   [:> ($ :Header) {:size "medium"} name]])

(defn open-feedback
  [name option1]
  [:div
   [:> ($ :Header) {:size "medium"} name]])
