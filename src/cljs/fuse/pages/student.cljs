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

(defn student-page []
  [:div
   nav
   [:div.page-content
    [:div.results
     [:> ($ :Header) {:size "large"} "Submit Responses"]
     (doall
       (map-indexed
         (fn [i {:keys [type name option1 option2 votes avg]}]
           ^{:key i}
           [:div.module
            [:div.toggle
             [:> ($ :Header) {:size "medium"} name]
             [:> ($ :Button.Group)
              [:> ($ :Button) {:disabled (nil? @username*)
                               :on-click
                               (fn [ev data]
                                 (go (async/<! (http/post "http://localhost:3000/update-module" {:form-params {:id @username* :name name :choice 0}}))
                                     (read-data)
                                     (println "all data: " @all-data*)))} option1]
              [:> ($ :Button.Or)]
              [:> ($ :Button) {:disabled (nil? @username*)
                               :on-click
                               (fn [ev data]
                                 (go (async/<! (http/post "http://localhost:3000/update-module" {:form-params {:id @username* :name name :choice 100}}))
                                     (read-data)
                                     (println "all data: " @all-data*)))} option2]]]
            #_(cond
                (= type "Slider")
                [:div.slider
                 [:> ($ :Header) {:size "medium"} name]
                 [:input {:class "mdl-slider mdl-js-slider"
                          :tabIndex 0
                          :min 0
                          :max 100
                          :type "range"
                          :value 50
                          :on-change (println "changed")}]
                 [:div {:style {:display "inline-block"}}]
                 [:div {:style {:float "left" :color "grey"}} option1]
                 [:div {:style {:float "right" :color "grey"}} option2]]
                (= type "Toggle")
                [:div.toggle
                 [:> ($ :Header) {:size "medium"} name]
                 [:> ($ :Button.Group)
                  [:> ($ :Button) {:on-click
                                   (fn [ev data]
                                     (swap! button* assoc :option1 0)
                                     (pprint/pprint @button*)
                                     (pprint/pprint (str "all-data: " @all-data*)))} option1]
                  [:> ($ :Button.Or)]
                  [:> ($ :Button) {:on-click
                                   (fn [ev data]
                                     (pprint/pprint (str "specific part of all data: " ((keyword name) @all-data*)))
                                     (swap! all-data* update-in [:votes] conj 1)
                                     (pprint/pprint (str "all-data: " @all-data*)))} option2]]]
                (= type "Poll")
                [:div.poll
                 [:> ($ :Header) {:size "medium"} name]
                 [:> ($ :Dropdown) {:floating true
                                    :labeled true
                                    :button true
                                    :icon "dropdown"
                                    :class "icon"
                                    :text "Vote"}
                  [:> ($ :Dropdown.Menu)
                   [:> ($ :Dropdown.Item) option1]
                   [:> ($ :Dropdown.Item) option2]]]]
                (= type "Open Feedback")
                [:div.open-feedback
                 [:> ($ :Header) {:size "medium"} name]
                 [:> ($ :Feed)
                  [:> ($ :Feed.Event)
                   [:> ($ :Feed.Label) {:image "http://placehold.it/10x10"}]
                   [:> ($ :Feed.Content)
                    [:> ($ :Feed.Summary) "Anonymous student posted a comment"]
                    [:> ($ :Feed.Extra) {:text true} "Ours is a life of constant reruns. We're always circling back to where we'd we started, then starting all over again."]]]]]
                :else
                [:div "Error rendering components."])])
         @all-data*))]]])
