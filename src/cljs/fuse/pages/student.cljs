(ns fuse.pages.student
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [fuse.pages.teacher :refer [all-data* read-data]]
            [cljs-http.client :as http]
            [cljs.core.async :as async]
            [clojure.pprint :as pprint]
            [semantic-ui.core :refer [$]]))

(read-data)

(def username*
  (atom nil))

(def nav
  [:> ($ :Menu) {:inverted true :pointing true :borderless true :class "menu"}
   [:> ($ :Menu.Item)
    [:> ($ :Image) {:src "http://www.guruadvisor.net/images/numero11/cloud.png" :height 32}]]
   [:> ($ :Menu.Menu) {:position :right}
    [:> ($ :Menu.Item) {:position :right} [:a {:href "/teacher"} "Teacher"]]
    [:> ($ :Menu.Item) {:position :right} [:a {:href "/student"} "Student"]]
    [:> ($ :Menu.Item) {:position :right}
     [:> ($ :Input) {:focus true :inverted true :placeholder "Username..." :style {:margin-right "10px"}
                     :on-change (fn [ev data]
                                  (reset! username* (:value (js->clj data :keywordize-keys true))))}]]]])

(def feedback*
  (atom nil))

(defn student-page []
  [:div
   nav
   [:div.page-content
    (println @username*)
    (if-not (empty? @username*)
      [:> ($ :Header) {:color "teal" :size "medium"} (str "Welcome, " @username* "!")]
      [:> ($ :Header) {:color "teal" :size "medium"} (str "Please enter your username above to enable feedback submissions.")])
    [:div.submissions
     [:> ($ :Header) {:size "large"} "Submit Responses"]
     (doall
       (map-indexed
         (fn [i {:keys [type name option1 option2 votes avg]}]
           (cond
             (= type "Toggle")
             ^{:key i}
             [:div.module
              [:div.toggle
               [:> ($ :Header) {:size "medium"} name]
               [:> ($ :Button.Group)
                [:> ($ :Button) {:disabled (empty? @username*)
                                 :on-click
                                 (fn [ev data]
                                   (go (async/<! (http/post "http://localhost:3000/update-module"
                                                            {:form-params {:id @username* :name name :choice 0}}))
                                       (read-data)
                                       (println "all data: " @all-data*)))} option1]
                [:> ($ :Button.Or)]
                [:> ($ :Button) {:disabled (nil? @username*)
                                 :on-click
                                 (fn [ev data]
                                   (go (async/<! (http/post "http://localhost:3000/update-module"
                                                            {:form-params {:id @username* :name name :choice 100}}))
                                       (read-data)
                                       (println "all data: " @all-data*)))} option2]]]]
             (= type "Slider")
             ^{:key i}
             [:div.module
              [:div.toggle
               [:> ($ :Header) {:size "medium"} name]
               [:input {:disabled (empty? @username*) :type "range" :min 0 :max 100 :style {:width "95%"} :class "mdl-slider mdl-js-slider"
                        :on-change
                        (fn [ev data]
                          (let [value (.-target.value ev)]
                            (go (async/<! (http/post "http://localhost:3000/update-module"
                                                     {:form-params {:id @username* :name name :choice value}}))
                                (read-data)))
                          (println "value: " (.-target.value ev)))}]
               [:div.labels {:style {:height "20px" :position :relative}}]
               [:div {:style {:float "left" :color "grey"}} option1]
               [:div {:style {:float "right" :color "grey"}} option2]]]
             (= type "Open Feedback")
             ^{:key i}
             [:div.module
              [:div.open-feedback
               [:> ($ :Header) {:size "medium"} name]
               [:> ($ :Form)
                [:> ($ :TextArea) {:disabled (empty? @username*) :placeholder option1 :autoHeight true
                                   :on-change
                                   (fn [ev data]
                                     (reset! feedback* (:value (js->clj data :keywordize-keys true))))}]
                [:> ($ :Button) {:primary true :style {:margin "10px 0"} :href "#"
                                 :on-click
                                 (fn [ev]
                                   (go (async/<! (http/post "http://localhost:3000/update-module"
                                                            {:form-params {:id @username* :name name :choice @feedback*}}))
                                       (read-data)))} "Submit Feedback"]]]]
             :else
             [:div "Error rendering components."]))
         @all-data*))]]])
