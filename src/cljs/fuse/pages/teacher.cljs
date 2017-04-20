(ns fuse.pages.teacher
  (:require [reagent.core :as reagent :refer [atom]]
            [semantic-ui.core :refer [$]]))

(def module-button
  (atom {:content "Show Module Options"
         :icon "chevron down"
         :open true}))

(def module-selection
  (atom {:selection "Choose a Module"
         :name nil}))

(def slider-data
  (atom {:percent 50
         :color "olive"}))

(def toggle-data
  (atom {:option1 nil
         :option2 nil}))

(def poll-data
  (atom {:option1 nil
         :option2 nil}))

(defn teacher-page []
  [:div
   [:div.results
    [:> ($ :Progress) {:percent (@slider-data :percent) :indicating true :progress true :color (@slider-data :color)}]
    ]
   [:div.create
    [:> ($ :Button)
     {:content (get @module-button :content)
      :color "teal"
      :icon (get @module-button :icon)
      :on-click (fn [ev]
                  (swap! module-button assoc
                         :content (if (= (@module-button :open) false) "Show Module Options" "Hide Module Options")
                         :icon (if (= (@module-button :open) false) "chevron down" "chevron up")
                         :open (if (= (@module-button :open) false) true false)))}]
    (if (= (@module-button :open) true)
      [:> ($ :Segment) {:padded true :class "create-form"}
       [:> ($ :Form)
        [:> ($ :Form.Group)
         [:> ($ :Dropdown) {:floating true
                            :labeled true
                            :button true
                            :icon "dropdown"
                            :class "icon"
                            :text (@module-selection :selection)}
          [:> ($ :Dropdown.Menu)
           [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! module-selection assoc :selection "Slider"))} "Slider"]
           [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! module-selection assoc :selection "Toggle"))} "Toggle"]
           [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! module-selection assoc :selection "Poll"))}"Poll"]
           [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! module-selection assoc :selection "Open Feedback"))} "Open Feedback"]]]
         [:> ($ :Form.Input) {:placeholder "Name (Ex: Pace)"}]]
        [:br]
        [:br]
        (cond
          (= (@module-selection :selection) "Slider")
          [:div
           [:> ($ :Header) {:dividing true} "Slider"]
           [:> ($ :Form.Group)
            [:> ($ :Form.Input) {:label "Min Value" :placeholder "Ex: 0"}]
            [:> ($ :Form.Input) {:label "Max Value" :placeholder "Ex:10"}]]
           [:input {:class "mdl slider mdl-js-slider" :type "range"}]]
          (= (@module-selection :selection) "Toggle")
          [:div
           [:> ($ :Header) {:dividing true} "Toggle"]
           [:> ($ :Form.Group)
            [:> ($ :Form.Input) {:label "Option 1" :placeholder "Ex: I Need Help"}]
            [:> ($ :Form.Input) {:label "Option 2" :placeholder "I'm Okay"}]]
           [:br]
           [:> ($ :Button.Group) {:size "large"}
            [:> ($ :Button) "One"]
            [:> ($ :Button.Or)]
            [:> ($ :Button) "Two"]]]
          (= (@module-selection :selection) "Poll")
          [:div
           [:> ($ :Header) {:dividing true} "Poll"]
           [:> ($ :Form.Group)
            [:> ($ :Form.Input) {:label "Option 1" :placeholder "Ex: Work time"}]
            [:> ($ :Form.Input) {:label "Option 2" :placeholder "Ex: Keep going"}]]]
          (= (@module-selection :selection) "Open Feedback") "Open Feedback"
          :else nil)
        [:> ($ :Button) "Submit"]]
       ]
      nil)]
   ])
