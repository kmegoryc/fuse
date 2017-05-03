(ns fuse.pages.teacher
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :as async]
            [semantic-ui.core :refer [$]]))

(def nav
  [:> ($ :Menu) {:inverted true :pointing true :borderless true :class "menu"}
   [:> ($ :Menu.Item)
    [:> ($ :Image) {:src "http://www.guruadvisor.net/images/numero11/cloud.png" :height 32}]]
   [:> ($ :Menu.Menu) {:position :right}
    [:> ($ :Menu.Item) {:position :right} [:a {:href "/teacher"} "Teacher"]]
    [:> ($ :Menu.Item) {:position :right} [:a {:href "/student"} "Student"]]]])

(def show-hide*
  (atom {:content "Show Module Options"
         :icon "chevron down"
         :open true}))

(def create*
  (atom {:type "Choose a Module"
         :name nil
         :option1 nil
         :option2 nil
         :avg 50}))

(def all-data*
  (atom nil))

(defn read-data
  []
  (go (let [response (async/<! (http/get "http://localhost:3000/read-data"))]
        (prn (str "all data reset to: " (:body response)))
        (reset! all-data* (:body response)))))

(defn average
  [numbers]
  (/ (apply + numbers) (count numbers)))

(read-data)

(def options
  [:div
   [:> ($ :Form.Group)
    [:> ($ :Form.Input) {:label "Option 1" :placeholder "Ex: Too Slow"
                         :on-change (fn [ev data]
                                      (swap! create* assoc :option1 (:value (js->clj data :keywordize-keys true))))}]
    [:> ($ :Form.Input) {:label "Option 2" :placeholder "Ex: Too Fast"
                         :on-change (fn [ev data]
                                      (swap! create* assoc :option2 (:value (js->clj data :keywordize-keys true))))}]]
   [:br]
   [:> ($ :Button) {:href "#"
                    :on-click (fn [ev]
                                (println "create: " @create*)
                                (go (async/<! (http/post "http://localhost:3000/add-module" {:form-params @create*}))
                                    (read-data))
                                (println "new data: " @create*))} "Submit"]])

(defn teacher-page []
  [:div
   nav
   [:div.page-content
    (if-not (empty? @all-data*)
      [:div.results
       [:> ($ :Header) {:size "large"} "Responses"]
       [:div.results
        (map-indexed
          (fn [i {:keys [type name option1 option2 votes avg]}]
            (cond
              (= type "Open Feedback")
              ^{:key i}
              [:div.module {:style {:position :relative}}
               [:> ($ :Header) {:size "medium"} option1]
               [:> ($ :Feed) {:style {:margin "20px 0"}}
                (map-indexed
                  (fn [i {:keys [id name choice]}]
                    ^{:key i}
                    [:> ($ :Feed.Event)
                     [:> ($ :Feed.Label) {:image "http://www.infragistics.com/media/8948/anonymous_200.gif"}]
                     [:> ($ :Feed.Content)
                      [:> ($ :Feed.Summary) (str id " posted a comment.")]
                      [:> ($ :Feed.Extra) {:text true} choice]]]) votes)]
               [:> ($ :Button) {:primary true :icon true :circular true :style {:position :absolute :top 0 :right 0 :margin "10px"}
                                :on-click (fn [ev]
                                            (go (async/<! (http/post "http://localhost:3000/remove-module" {:form-params {:name name}}))
                                                (read-data)))}
                [:> ($ :Icon) {:name "remove"}]]]
              :else
              ^{:key i}
              [:div.module {:style {:position :relative}}
               [:> ($ :Header) {:size "medium" :class "teacher-header"} name]

               [:> ($ :Button) {:primary true :icon true :circular true :style {:position :absolute :top 0 :right 0 :margin "10px"}
                                :on-click (fn [ev]
                                            (go (async/<! (http/post "http://localhost:3000/remove-module" {:form-params {:name name}}))
                                                (read-data)))}
                [:> ($ :Icon) {:name "remove"}]]
               (prn "votes: " votes)
               [:> ($ :Progress) {:percent (if (empty? votes)
                                             avg
                                             (/ (apply + (map #(js/parseInt (:choice %)) votes)) (count votes)))
                                  :indicating true :progress true :color
                                  (cond
                                    (> (if (empty? votes)
                                         avg
                                         (/ (apply + (map #(js/parseInt (:choice %)) votes)) (count votes))) 75) "red"
                                    (< (if (empty? votes)
                                         avg
                                         (/ (apply + (map #(js/parseInt (:choice %)) votes)) (count votes))) 25) "red"
                                    :else "olive")}]
               [:div {:style {:float "left" :color "grey"}} option1]
               [:div {:style {:float "right" :color "grey"}} option2]]))
          @all-data*)]
       [:> ($ :Divider)]])
    [:div.create
     [:> ($ :Header) {:size "large"} "Create Modules"]
     [:> ($ :Button)
      {:content (get @show-hide* :content)
       :color "teal"
       :icon (get @show-hide* :icon)
       :on-click (fn [ev]
                   (swap! show-hide* assoc
                          :content (if (= (@show-hide* :open) false) "Show Module Options" "Hide Module Options")
                          :icon (if (= (@show-hide* :open) false) "chevron down" "chevron up")
                          :open (if (= (@show-hide* :open) false) true false)))}]
     (if (= (@show-hide* :open) true)
       [:> ($ :Segment) {:style {:padding "40px"} :class "create-form"}
        [:> ($ :Form)
         [:> ($ :Form.Group)
          [:> ($ :Dropdown) {:floating true
                             :labeled true
                             :button true
                             :icon "dropdown"
                             :class "icon"
                             :text (@create* :type)}
           [:> ($ :Dropdown.Menu)
            [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! create* assoc :type "Slider"))} "Slider"]
            [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! create* assoc :type "Toggle"))} "Toggle"]
            [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! create* assoc :type "Open Feedback" :option2 nil))} "Open Feedback"]
            ]]
          [:> ($ :Form.Input) {:placeholder "Name (Ex: Pace)"
                               :on-change (fn [ev data]
                                            (swap! create* assoc :name (:value (js->clj data :keywordize-keys true))))}]]
         [:br]
         [:br]
         (cond
           (= (@create* :type) "Slider")
           [:div
            [:> ($ :Header) {:dividing true} "Slider"]
            options]
           (= (@create* :type) "Toggle")
           [:div
            [:> ($ :Header) {:dividing true} "Toggle"]
            options]
           (= (@create* :type) "Open Feedback")
           [:div
            [:> ($ :Header) {:dividing true} "Open Feedback"]
            [:> ($ :Form.Input) {:label "Prompt" :placeholder "Ex: What do you think about that?"
                                 :on-change (fn [ev data]
                                              (swap! create* assoc :option1 (:value (js->clj data :keywordize-keys true))))}]
            [:br]
            [:> ($ :Button) {:href "#"
                             :on-click (fn [ev]
                                         (go (async/<! (http/post "http://localhost:3000/add-module" {:form-params @create*}))
                                             (read-data))
                                         (println "new data: " @create*))} "Submit"]]
           :else nil)]]
       nil)]]])
