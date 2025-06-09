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
(defn get-ranks
  "Extracts the rank of each card in the given hand.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - A sequence of ranks from the hand

   Example:
   (get-ranks [{:rank :ace :suit :hearts} {:rank :king :suit :clubs}])
   ;; => (:ace :king)"
  [hand]
  nil
  )

(defn get-suits
  "Extracts the suit of each card in the given hand.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - A sequence of suits from the hand

   Example:
   (get-suits [{:rank :ace :suit :hearts} {:rank :king :suit :clubs}])
   ;; => (:hearts :clubs)"
  [hand]
  nil
  )

(defn rank-frequencies
  "Counts the frequency of each rank in the hand.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - A map where keys are ranks and values are their frequencies

   Example:
   (rank-frequencies [{:rank :ace :suit :hearts} {:rank :ace :suit :clubs}])
   ;; => {:ace 2}"
  [hand]
  nil
  )

(defn has-n-of-kind?
  "Determines if the hand contains exactly n cards of the same rank.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys
   - n: The number of cards of the same rank to check for

   Returns:
   - true if the hand contains n cards of the same rank, false otherwise

   Example:
   (has-n-of-kind? [{:rank :ace :suit :hearts} {:rank :ace :suit :clubs}] 2)
   ;; => true"
  [hand n]
  nil
  )

(defn is-four-of-kind?
  "Determines if the hand contains four cards of the same rank.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - true if the hand contains four cards of the same rank, false otherwise

   Example:
   (is-four-of-kind? [{:rank :ace :suit :hearts} {:rank :ace :suit :clubs}
                      {:rank :ace :suit :diamonds} {:rank :ace :suit :spades}
                      {:rank :king :suit :hearts}])
   ;; => true"
  [hand]
  nil
  )

(defn is-three-of-kind?
  "Determines if the hand contains three cards of the same rank.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - true if the hand contains three cards of the same rank, false otherwise

   Example:
   (is-three-of-kind? [{:rank :ace :suit :hearts} {:rank :ace :suit :clubs}
                       {:rank :ace :suit :diamonds} {:rank :king :suit :hearts}
                       {:rank :queen :suit :spades}])
   ;; => true"
  [hand]
  nil
  )

(defn is-flush?
  "Determines if the hand contains five cards of the same suit.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - true if all cards in the hand have the same suit, false otherwise

   Example:
   (is-flush? [{:rank :ace :suit :hearts} {:rank :king :suit :hearts}
               {:rank :queen :suit :hearts} {:rank :jack :suit :hearts}
               {:rank :10 :suit :hearts}])
   ;; => true"
  [hand]
  nil
  )

(defn is-straight?
  "Determines if the hand contains five cards of sequential rank.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - true if the hand contains five cards of sequential rank, false otherwise

   Example:
   (is-straight? [{:rank :ace :suit :hearts} {:rank :2 :suit :clubs}
                 {:rank :3 :suit :diamonds} {:rank :4 :suit :hearts}
                 {:rank :5 :suit :spades}])
   ;; => true"
  [hand]
  nil
  )

(defn is-straight-flush?
  "Determines if the hand contains five cards of sequential rank, all of the same suit.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - true if the hand is both a straight and a flush, false otherwise

   Example:
   (is-straight-flush? [{:rank :5 :suit :hearts} {:rank :6 :suit :hearts}
                        {:rank :7 :suit :hearts} {:rank :8 :suit :hearts}
                        {:rank :9 :suit :hearts}])
   ;; => true"
  [hand]
  nil
  )

(defn is-royal-flush?
  "Determines if the hand contains a 10, Jack, Queen, King, and Ace, all of the same suit.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - true if the hand is a royal flush, false otherwise

   Example:
   (is-royal-flush? [{:rank :10 :suit :hearts} {:rank :jack :suit :hearts}
                     {:rank :queen :suit :hearts} {:rank :king :suit :hearts}
                     {:rank :ace :suit :hearts}])
   ;; => true"
  [hand]
  nil
  )

(defn is-full-house?
  "Determines if the hand contains three cards of one rank and two cards of another rank.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - true if the hand is a full house, false otherwise

   Example:
   (is-full-house? [{:rank :ace :suit :hearts} {:rank :ace :suit :clubs}
                    {:rank :ace :suit :diamonds} {:rank :king :suit :hearts}
                    {:rank :king :suit :clubs}])
   ;; => true"
  [hand]
  nil
  )

(defn is-two-pair?
  "Determines if the hand contains two cards of one rank and two cards of another rank.

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - true if the hand contains two pairs, false otherwise

   Example:
   (is-two-pair? [{:rank :ace :suit :hearts} {:rank :ace :suit :clubs}
                  {:rank :king :suit :diamonds} {:rank :king :suit :hearts}
                  {:rank :queen :suit :clubs}])
   ;; => true"
  [hand]
  nil
  )

(defn is-pair?
  "Determines if the hand contains a pair of Jacks or better (Jacks, Queens, Kings, or Aces).

   Parameters:
   - hand: A sequence of card maps, each containing :rank and :suit keys

   Returns:
   - true if the hand contains a pair of Jacks, Queens, Kings, or Aces, false otherwise

   Example:
   (is-pair? [{:rank :jack :suit :hearts} {:rank :jack :suit :clubs}
              {:rank :5 :suit :diamonds} {:rank :3 :suit :hearts}
              {:rank :2 :suit :clubs}])
   ;; => true"
  [hand]
  nil
  )
;;; ===================
;;; End Workshop Challenges
;;; ===================


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