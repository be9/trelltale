(ns trelltale.views.layout
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer :all]))

(defn nav-list-items [page]
  (for [[path title] [["/" "Home"] ["/boards" "Boards"] ["/hooks" "Hooks"]]]
    [:li {:class (when (= page title) :active)} (link-to path title)]))

(defn common [page & body]
  (html5
    [:head
     [:title "Trelltale"]
     (include-css "//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css")
     (include-css "/css/narrow.css")]
    [:body 
      [:div.container
        [:div.header
          [:ul.nav.nav-pills.pull-right
            (nav-list-items page)]

          [:h3.text-muted "Trelltale"]]
        [:div.row.marketing
          body]
        [:div.footer
          [:p "&copy; Oleg Dashevskii 2014"]]]]))
