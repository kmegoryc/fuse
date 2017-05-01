(ns fuse.pages.teacher
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :as async]
            [semantic-ui.core :refer [$]]
            [fuse.pages.components :refer [button slider poll open-feedback]]))

(def show-hide*
  (atom {:content "Show Module Options"
         :icon "chevron down"
         :open true}))

(def create*
  (atom {:type "Choose a Module"
         :name nil
         :option1 nil
         :option2 nil}))

(def new-data*
  (atom nil))

(def all-data*
  (atom nil))

(def read-data
  (go (let [response (async/<! (http/get "http://localhost:3000/read-data"))]
        (reset! all-data* (:body response)))))

(add-watch new-data* :watcher
           (fn [key atom old-state new-state]
             (http/post "http://localhost:3000/add-module" {:form-params new-state})
             (prn "-- Atom Changed --")
             (prn "key" key)
             (prn "atom" atom)
             (prn "old-state" old-state)
             (prn "new-state" new-state)))

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
                                (reset! new-data* @create*))} "Submit"]])

(defn teacher-page []
  [:div
   [:div.results
    [:> ($ :Header) {:size "large"} "Responses"]
    [:> ($ :Progress) {:percent 50 :indicating true :progress true :color "olive"}]]
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
                                        (reset! new-data* @create*))} "Submit"]]
          :else nil)]]
      nil)]])
