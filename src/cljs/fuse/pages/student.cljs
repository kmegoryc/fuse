(ns fuse.pages.student
  (:require [semantic-ui.core :refer [$]]
            [reagent.core :as reagent :refer [atom]]))

(def DATA
  {{:type "button"
    :name "Need individual help?"
    :options ["I'm Okay" "Yes Please"]}
   {:type "slider"
    :name "Pace"
    :options [0 10]}})

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
