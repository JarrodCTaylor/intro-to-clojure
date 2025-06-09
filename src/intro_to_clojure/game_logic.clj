(ns intro-to-clojure.game-logic)

(def hand-size 5)
(def suits [:hearts :diamonds :clubs :spades])
(def ranks [:2 :3 :4 :5 :6 :7 :8 :9 :10 :jack :queen :king :ace])

(defn create-deck []
  (for [suit suits
        rank ranks]
    {:suit suit
     :rank rank
     :img-file (str (name rank) "-" (name suit))
     :held? false}))

(defn shuffle-deck [deck]
  (shuffle deck))

(defn deal-cards [deck n]
  {:deal (vec (take n deck))
   :remaining (drop n deck)})

(defn deal-new-hand []
  (let [deck (shuffle-deck (create-deck))]
    (deal-cards deck hand-size)))

(defn toggle-hold [hand idx]
  (update-in hand [idx :held?] not))

(defn draw-new-cards [hand deck]
  (let [draw-count (count (filter #(not (:held? %)) hand))
        {:keys [deal remaining]} (deal-cards deck draw-count)
        {:keys [new-hand]} (reduce (fn [res card]
                                     (if (:held? card)
                                       (update-in res [:new-hand] #(conj % card))
                                       (-> res
                                           (update-in [:new-hand] #(conj % (first (:deal res))))
                                           (update-in [:deal] rest))))
                                   {:deal deal :new-hand []}
                                   hand)]
    {:hand new-hand
     :deck remaining}))

(defn rank-value [{:keys [rank]}]
  (rank {:2 2 :3 3 :4 4 :5 5 :6 6 :7 7
         :8 8 :9 9 :10 10 :jack 11 :queen 12
         :king 13 :ace 14}))

;;; ===================
;;; Workshop Challenges
;;; ===================
(defn get-ranks [hand]
  (map :rank hand))

(defn get-suits [hand]
  (map :suit hand))

(defn rank-frequencies [hand]
  (frequencies (get-ranks hand)))

(defn has-n-of-kind? [hand n]
  (let [rank-equals-n (fn [[_ rank]] (= n rank))]
    (some rank-equals-n (rank-frequencies hand))))

(defn is-four-of-kind? [hand]
  (has-n-of-kind? hand 4))

(defn is-three-of-kind? [hand]
  (has-n-of-kind? hand 3))

(defn is-flush? [hand]
  (= 1 (count (distinct (get-suits hand)))))

(defn is-straight? [hand]
  (let [values (sort (map rank-value hand))
        step-pairs (partition 2 1 values)
        deltas (for [[a b] step-pairs] (- b a))]
    (apply = 1 deltas)))

(defn is-straight-flush? [hand]
  (and (is-straight? hand) (is-flush? hand)))

(defn is-royal-flush? [hand]
  (and (is-flush? hand)
       (= (set (get-ranks hand)) #{:10 :jack :queen :king :ace})))

(defn is-full-house? [hand]
  (let [freqs (vals (rank-frequencies hand))]
    (= (set freqs) #{2 3})))

(defn is-two-pair? [hand]
  (let [freqs (vals (rank-frequencies hand))]
    (= (frequencies freqs) {1 1, 2 2})))

(defn is-pair?
  "Jacks or better"
  [hand]
  (let [pairs (filter (fn [[_ rank]] (= 2 rank)) (rank-frequencies hand))
        contains-face-card? (fn [[suit _]] (contains? #{:jack :queen :king :ace} suit))]
    (some contains-face-card? pairs)))

(defn evaluate-hand [hand]
  (cond
    (is-royal-flush? hand)     {:hand-type :royal-flush :multiplier 250}
    (is-straight-flush? hand)  {:hand-type :straight-flush :multiplier 50}
    (is-four-of-kind? hand)    {:hand-type :4-of-a-kind :multiplier 25}
    (is-full-house? hand)      {:hand-type :full-house :multiplier 9}
    (is-flush? hand)           {:hand-type :flush :multiplier 6}
    (is-straight? hand)        {:hand-type :straight :multiplier 4}
    (is-three-of-kind? hand)   {:hand-type :3-of-a-kind :multiplier 3}
    (is-two-pair? hand)        {:hand-type :2-pair :multiplier 2}
    (is-pair? hand)            {:hand-type :jacks-or-better :multiplier 1}
    :else                      {:hand-type nil :multiplier 0}))

(defn score-game [{:keys [hand bet cash]}]
  (let [{:keys [hand-type multiplier]} (evaluate-hand hand)
        winnings (if (and (= 5 bet) (= :royal-flush hand-type))
                   4000 ;; Support original video poker royal flush max payout
                   (* bet multiplier))]
    {:winnings winnings
     :cash (if (zero? winnings)
                (- cash bet)
                (+ cash winnings))
     :winning-type hand-type}))

(defn inc-bet [current]
  (if (= 5 current) 1 (inc current)))