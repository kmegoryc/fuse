(ns fuse.pages.student
  (:require [semantic-ui.core :refer [$]]
            [reagent.core :as reagent :refer [atom]]))

(def data*
  (atom
    {:data []}))

(defn button
  [name options]
  [:> ($ :Header) {:size "medium"} name]
  [:> ($ :Button.Group)
   [:> ($ :Button) "I'm Okay"]
   [:> ($ :Button.Or)]
   [:> ($ :Button) "Yes Please"]])

(defn student-page []
  [:div
   [:div.modules]])
