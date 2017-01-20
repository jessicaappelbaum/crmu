(ns crmu.core
  (:require [cljsjs.material-ui]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]
            [reagent.core :as r :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]))

;; -------------------------
;; Views

(defn home-page []
  [ui/mui-theme-provider
   {:mui-theme (get-mui-theme {:palette {:text-color (color :green600)}})}
   [:div
    [ui/app-bar {:title "Title"
                 :icon-element-right (r/as-element [ui/icon-button (ic/action-account-balance-wallet)])}]
    [ui/paper
     [:div "Hello"
      [:a {:href "/about"} "go to about page"]]
     [ui/mui-theme-provider
      {:mui-theme (get-mui-theme {:palette {:text-color (color :blue200)}})}
      [ui/raised-button {:label "Blue button"}]]
     [ui/raised-button {:label "Green button"}]
     (ic/action-flight-takeoff)
     (ic/action-home {:color (color :grey600)})
     [ui/raised-button {:label "Click me"
                        :icon (ic/social-group)
                        :on-touch-tap #(println "clicked")}]
     [ui/raised-button {:on-touch-tap #(println "clicked")}]]]])

(defn about-page []
  [:div [:h2 "About crmu"]
   [:div [:a {:href "/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
