(ns fuse.pages.teacher
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :as async]
            [semantic-ui.core :refer [$]]))

(def show-hide*
  (atom {:content "Show Module Options"
         :icon "chevron down"
         :open true}))

(def create*
  (atom {:type "Choose a Module"
         :name nil
         :option1 nil
         :option2 nil
         :votes []
         :avg 50}))

(def all-data*
  (atom #{}))

(defn read-data
  []
  (go (let [response (async/<! (http/get "http://localhost:3000/read-data"))]
        (prn (str "all data reset to: " (:body response)))
        (reset! all-data* (:body response)))))

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
                                (read-data)
                                (http/post "http://localhost:3000/add-module" {:form-params @create*})
                                (read-data)
                                (println "new data: " @create*))} "Submit"]])

(defn teacher-page []
  [:div
   [:div.results
    [:> ($ :Header) {:size "large"} "Responses"]
    [:div.results
     (map-indexed
       (fn [i {:keys [type name option1 option2 votes avg]}]
         ^{:key i}
         [:div.module
          [:> ($ :Header) {:size "medium" :class "teacher-header"} name]
          [:div.display
           [:> ($ :Button) {:primary true :icon true :circular true
                            :on-click (fn [ev]
                                        (read-data)
                                        (http/post "http://localhost:3000/remove-module" {:form-params {:name name}})
                                        (read-data))}
            [:> ($ :Icon) {:name "remove"}]]
           [:> ($ :Progress) {:percent avg :indicating true :progress true :color
                              (cond
                                (> avg 75) "red"
                                (< avg 25) "red"
                                :else "olive")}]]
          [:div {:style {:float "left" :color "grey"}} option1]
          [:div {:style {:float "right" :color "grey"}} option2]])
       @all-data*)]]
   [:> ($ :Divider)]
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
      [:> ($ :Segment) {:padded true :class "create-form"}
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
           [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! create* assoc :type "Poll"))}"Poll"]
           [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! create* assoc :type "Open Feedback" :option2 nil))} "Open Feedback"]]]
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
          (= (@create* :type) "Poll")
          [:div
           [:> ($ :Header) {:dividing true} "Poll"]
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
                                        (read-data)
                                        (http/post "http://localhost:3000/add-module" {:form-params @create*})
                                        (read-data)
                                        (println "new data: " @create*))} "Submit"]]
          :else nil)]]
      nil)]])
