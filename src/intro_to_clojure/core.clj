(ns intro-to-clojure.core
  (:require
    [hiccup2.core :as h]
    [org.httpkit.server :as httpkit]
    [reitit.ring :as ring]
    [muuntaja.core :as m]
    [intro-to-clojure.game-logic :as game]
    [intro-to-clojure.views :as views]))

(def initial-state {:cash 100
                    :bet 1
                    :deck nil
                    :hand nil})

(def game-state (atom initial-state))

(defn response-builder [status & body]
  {:status status
   :headers {"Content-Type" "text/html"}
   :body (str (h/html body))})

(defn deal-handler [_]
  (let [{:keys [cash bet]} @game-state
        {:keys [deal remaining]} (game/deal-new-hand)]
    (swap! game-state assoc :hand deal :deck remaining)
    (response-builder 200
                      [:div#container
                       (views/paytable {:bet bet})
                       (views/hand {:hand deal :draw? true})
                       (views/information {:bet bet :cash cash :oob? false})
                       (views/controls {:draw? true :oob? false})])))

(defn hold-handler [{{:keys [id]} :path-params}]
  (let [index (Integer/parseInt id)
        new-hand-state (game/toggle-hold (:hand @game-state) index)
        card (nth new-hand-state index)]
    (swap! game-state assoc :hand new-hand-state)
    (response-builder 200 (views/card index card true))))

(defn draw-handler [_]
  (let [{:keys [deck hand cash bet]} @game-state
        {:keys [hand deck]} (game/draw-new-cards hand deck)
        {:keys [winnings cash winning-type]} (game/score-game {:hand hand :bet bet :cash cash})]
    (swap! game-state assoc :hand hand :deck deck :cash cash)
    (response-builder 200
                      [:div#container
                       (views/paytable {:bet bet :winner winning-type})
                       (views/hand {:hand hand :draw? false})
                       (views/information {:win winnings :bet bet :cash cash :oob? false})
                       (views/controls {:draw? false :oob? false})])))

(defn inc-bet-handler [_]
  (let [current-bet (:bet @game-state)
        cash (:cash @game-state)
        new-bet (game/inc-bet current-bet)]
    (swap! game-state assoc :bet new-bet)
    (response-builder 200
                      (views/paytable {:bet new-bet})
                      (views/information {:bet new-bet :cash cash :oob? true}))))

(defn max-bet-handler [_]
  (let [{:keys [deal remaining]} (game/deal-new-hand)
        cash (:cash @game-state)
        new-bet 5]
    (swap! game-state assoc :hand deal :deck remaining :bet new-bet)
    (response-builder 200
                      [:div#container
                       (views/paytable {:bet new-bet})
                       (views/hand {:hand deal :draw? true})
                       (views/information {:bet new-bet :cash cash :oob? false})
                       (views/controls {:draw? true :oob? false})])))

(defn init-handler [_]
  (reset! game-state initial-state)
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Initialized"})

(def app
  (-> (ring/ring-handler
        (ring/router
          [["/hold/:id" {:post hold-handler}]
           ["/deal" {:post deal-handler}]
           ["/draw" {:post draw-handler}]
           ["/inc-bet" {:post inc-bet-handler}]
           ["/max-bet" {:post max-bet-handler}]
           ["/init" {:post init-handler}]]
          {:data {:muuntaja m/instance}})
        (ring/routes
        (ring/create-resource-handler {:path "/" :root "public"})
        (ring/create-default-handler)))))

(defn start-server [port]
  (let [server (httpkit/run-server app {:port port})]
    (println (str "Open http://localhost:" port " in your browser."))
    server))

(defn -main [& args]
  (let [port (or (some-> args first Integer/parseInt) 3000)]
    (start-server port)))

(comment
  (def stop-server (start-server 3000))
  (stop-server))