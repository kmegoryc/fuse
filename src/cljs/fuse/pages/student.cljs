(ns fuse.pages.student
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :as async]
            [semantic-ui.core :refer [$]]
            [fuse.pages.components :refer [button slider poll open-feedback]]))

(def all-data*
  (atom nil))

(def read-data
  (go (let [response (async/<! (http/get "http://localhost:3000/read-data"))]
        (reset! all-data* (:body response)))))

(defn student-page []
  [:div
   [:> ($ :Header) {:size "large"} "Submit Responses"]
   [:div.results
    (map-indexed
      (fn [i {:keys [type name option1 option2]}]
        ^{:key i}
        [:div.module
         (cond
           (= type "Toggle")
           (button name option1 option2)
           (= type "Slider")
           (slider name option1 option2)
           (= type "Poll")
           (poll name option1 option2)
           (= type "Open Feedback")
           (open-feedback name option1)
           :else
           [:div "Error rendering components."])])
      @all-data*)]])
