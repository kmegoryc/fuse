(ns fuse.pages.teacher
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [semantic-ui.core :refer [$]]))

(def show-hide*
  (atom {:content "Show Module Options"
         :icon "chevron down"
         :open true}))

(def create*
  (atom {:type "Choose a Module"
         :name nil
         :option1 nil
         :option2 nil}))

(def data*
  (atom
    {:data []}))

(def a (atom {}))

(add-watch data* :watcher
           (fn [key atom old-state new-state]
             (http/post "http://localhost:3000/teacher" {:form-params new-state})
             (prn "-- Atom Changed --")
             (prn "key" key)
             (prn "atom" atom)
             (prn "old-state" old-state)
             (prn "new-state" new-state)))

(defn teacher-page []
  [:div
   [:div.results
    [:> ($ :Progress) {:percent 50 :indicating true :progress true :color "olive"}]]
   [:div.create
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
           [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! create* assoc :type "Open Feedback" :option1 nil))} "Open Feedback"]]]
         [:> ($ :Form.Input) {:placeholder "Name (Ex: Pace)"
                              :on-change (fn [ev data]
                                           (swap! create* assoc :name (:value (js->clj data :keywordize-keys true))))}]]
        [:br]
        [:br]
        (cond
          (= (@create* :type) "Slider")
          [:div
           [:> ($ :Header) {:dividing true} "Slider"]
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
                                        (swap! data* update-in [:data] conj @create*)
                                        (prn data*))} "Submit" ]]
          (= (@create* :type) "Toggle")
          [:div
           [:> ($ :Header) {:dividing true} "Toggle"]
           [:> ($ :Form.Group)
            [:> ($ :Form.Input) {:label "Option 1" :placeholder "Ex: I Need Help"
                                 :on-change (fn [ev data]
                                              (swap! create* assoc :option1 (:value (js->clj data :keywordize-keys true))))}]
            [:> ($ :Form.Input) {:label "Option 2" :placeholder "I'm Okay"
                                 :on-change (fn [ev data]
                                              (swap! create* assoc :option2 (:value (js->clj data :keywordize-keys true))))}]]
           [:br]
           [:> ($ :Button) {:href "#"
                            :on-click (fn [ev]
                                        (swap! data* update-in [:data] conj @create*)
                                        (prn data*))} "Submit"]]
          (= (@create* :type) "Poll")
          [:div
           [:> ($ :Header) {:dividing true} "Poll"]
           [:> ($ :Form.Group)
            [:> ($ :Form.Input) {:label "Option 1" :placeholder "Ex: Work time"
                                 :on-change (fn [ev data]
                                              (swap! create* assoc :option1 (:value (js->clj data :keywordize-keys true))))}]
            [:> ($ :Form.Input) {:label "Option 2" :placeholder "Ex: Keep going"
                                 :on-change (fn [ev data]
                                              (swap! create* assoc :option2 (:value (js->clj data :keywordize-keys true))))}]]
           [:br]
           [:> ($ :Button) {:href "#"
                            :on-click (fn [ev]
                                        (swap! data* update-in [:data] conj @create*)
                                        (prn data*))} "Submit"]]
          (= (@create* :type) "Open Feedback")
          [:div
           [:> ($ :Header) {:dividing true} "Open Feedback"]
           [:> ($ :Form.Input) {:label "Prompt" :placeholder "Ex: What do you think about that?"
                                :on-change (fn [ev data]
                                             (swap! create* assoc :option1 (:value (js->clj data :keywordize-keys true))))}]
           [:br]
           [:> ($ :Button) {:href "#"
                            :on-click (fn [ev]
                                        (swap! data* update-in [:data] conj @create*)
                                        (prn data*))} "Submit"]]
          :else nil)]]
      nil)]])
