(ns fuse.pages.teacher
  (:require [semantic-ui.core :refer [$]]))

(def module-button
  (atom {:content "Show Module Options"
         :icon "chevron down"
         :open true}))

(def module-selection
  (atom {:selection "Choose a Module"}))

(defn teacher-page []
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
     [:> ($ :Segment) {:class "create-form"}
      [:> ($ :Form)
       [:> ($ :Form.Group)
        [:> ($ :Dropdown) {:floating true
                           :labeled true
                           :button true
                           :icon "dropdown"
                           :className "icon"
                           :text (@module-selection :selection)}
         [:> ($ :Dropdown.Menu)
          [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! module-selection assoc :selection "Slider"))} "Slider"]
          [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! module-selection assoc :selection "Toggle"))} "Toggle"]
          [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! module-selection assoc :selection "Poll"))}"Poll"]
          [:> ($ :Dropdown.Item) {:on-click (fn [ev] (swap! module-selection assoc :selection "Open Feedback"))} "Open Feedback"]]]]
       [:> ($ :Form.Group)
        [:> ($ :Form.Input) {:label "Name" :placeholder "Ex: Pace"}]
        (cond
          (= (@module-selection) "Slider") nil)]]]
     nil)])
