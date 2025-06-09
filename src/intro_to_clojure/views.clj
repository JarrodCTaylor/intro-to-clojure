(ns intro-to-clojure.views)

(defn cell [hand b1 b2 b3 b4 b5 bet highlighted?]
  [:div.paytable-row (when highlighted? {:class " highlight"})
   [:div.cell.paytable-hand hand]
   [:div.cell {:class (when (= 1 bet) " active-bet")} b1]
   [:div.cell {:class (when (= 2 bet) " active-bet")} b2]
   [:div.cell {:class (when (= 3 bet) " active-bet")} b3]
   [:div.cell {:class (when (= 4 bet) " active-bet")} b4]
   [:div.cell {:class (when (= 5 bet) " active-bet")} b5]])

(defn paytable [{:keys [bet winner]}]
  [:div#paytable
   (cell "ROYAL FLUSH........................." "250" "500" "750" "1000" "5000" bet (= :royal-flush winner))
   (cell "STRAIGHT FLUSH......................" "50" "100" "150" "200" "250" bet (= :straight-flush winner))
   (cell "4 OF A KIND........................." "25" "50" "75" "100" "125" bet (= :4-of-a-kind winner))
   (cell "FULL HOUSE.........................." "9" "18" "27" "36" "45" bet (= :full-house winner))
   (cell "FLUSH..............................." "6" "12" "18" "24" "30" bet (= :flush winner))
   (cell "STRAIGHT............................" "4" "8" "12" "16" "20" bet (= :straight winner))
   (cell "3 OF A KIND........................." "3" "6" "9" "12" "15" bet (= :3-of-a-kind winner))
   (cell "2 PAIR.............................." "2" "4" "6" "8" "10" bet (= :2-pair winner))
   (cell "JACKS OR BETTER....................." "1" "2" "3" "4" "5" bet (= :jacks-or-better winner))])

(defn card [idx card draw?]
  (let [held? (:held? card)
        id (str "card-" idx)
        class (str "card" (when held? " held"))
        htmx-attrs (when draw? {:hx-post (str "/hold/" idx) :hx-swap "outerHTML" :hx-trigger "click"})]
    [:div (merge {:id id :class class} htmx-attrs)
     (when held? [:div.hold "HELD"])
     [:img {:src (str "/images/" (:img-file card) ".png")}]]))

(defn hand [{:keys [hand draw?]}]
  [:div#cards
   (for [idx (range 5)]
     (card idx (nth hand idx) draw?))])

(defn information [{:keys [win bet cash oob?]}]
  [:div#info (when oob? {:hx-swap-oob true})
   (when win
     [:div.win-info
      [:div.text "WIN " win]])
   [:div.bet-info "BET " bet]
   [:div.cash-info
    [:div.text "CASH " cash]]])

(defn controls [{:keys [draw? oob?]}]
  [:div#controls (when oob? {:hx-swap-oob true})
   [:div.btn (if draw? {:class " disabled"} {:hx-post "/inc-bet" :hx-target "#paytable" :hx-swap "outerHTML"}) "BET ONE"]
   [:div.btn (if draw? {:class " disabled"} {:hx-post "/max-bet" :hx-target "#container" :hx-swap "outerHTML"}) "BET MAX"]
   [:div.btn {:hx-post (if draw? "/draw" "/deal") :hx-target "#container" :hx-swap "outerHTML"} (if draw? "DRAW" "DEAL")]])