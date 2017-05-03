(ns fuse.pages.home
  (:require [semantic-ui.core :refer [$]]))

(def nav
  [:> ($ :Menu) {:inverted true :pointing true :borderless true :class "menu"}
   [:> ($ :Menu.Item)
    [:> ($ :Image) {:src "http://www.guruadvisor.net/images/numero11/cloud.png" :height 32}]]
   [:> ($ :Menu.Menu) {:position :right}
    [:> ($ :Menu.Item) {:position :right} [:a {:href "/teacher"} "Teacher"]]
    [:> ($ :Menu.Item) {:position :right} [:a {:href "/student"} "Student"]]
    ]])

(defn home-page []
  [:div
   nav
   [:div.page-content
    [:> ($ :Header) {:size "large"} "Fuse: A Platform for Students & Teachers"]
    [:div "If only there was a way to let them know what you’re thinking, without disrupting the whole class or drawing unwanted attention to yourself. Is the teacher going too fast? Too slow? Should they explain that in another way? Students would be able to collaboratively give the teacher their live feedback, so he/she can improve the way they deliver information during lecture. With Fuse, teachers can post topic modules for their students to provide feedback on. The teacher's dashboard shows the average results of students' feedback."]
    [:> ($ :Divider)]]])
