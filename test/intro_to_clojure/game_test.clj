(ns intro-to-clojure.game-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [testing.game-scenarios :refer [hands]]
    [intro-to-clojure.game-logic :as sut]))

(deftest test-create-deck

  (testing "Deck has correct number of cards"
    (let [expected (* (count sut/suits) (count sut/ranks))
          actual (count (sut/create-deck))]
      (is (= expected actual))))

  (testing "Deck has all suits and ranks"
    (let [deck (sut/create-deck)
          actual-suits (set (map :suit deck))
          actual-ranks (set (map :rank deck))
          expected-suits (set sut/suits)
          expected-ranks (set sut/ranks)]
      (is (= expected-suits actual-suits))
      (is (= expected-ranks actual-ranks))))

  ;; NOTE: This is a very brittle test.
  ;; While it is easy to understand,
  ;; It may be advisable to use something like:
  ;; * https://github.com/clojure/spec.alpha/
  ;; * https://github.com/metosin/malli
  ;; for checking if maps conform to expectations
  (testing "Cards have correct format"
    (let [random-card (rand-nth (sut/create-deck))
          actual-keys (keys random-card)
          expected-keys [:suit :rank :img-file :held?]]
      (is (= expected-keys actual-keys))
      (is (false? (:held? random-card))))))

(deftest test-shuffle-deck
  (testing "Shuffling changes order not content of a deck"
    (let [deck (sut/create-deck)
          shuffled (sut/shuffle-deck deck)]
      (is (= (set deck) (set shuffled)) "Deck contents should be equal.")
      (is (not= deck shuffled)) "Deck order should be different")))

(deftest test-deal-cards
  (testing "Dealing returns correct number of cards"
    (let [deck (sut/create-deck)
          {:keys [deal remaining]} (sut/deal-cards deck 5)]
      (is (= 5 (count deal)) "Dealt hand should contain 5 cards")
      (is (= 47 (count remaining)) "After dealing a hand 47 card should remain in deck")))

  ;; TODO held cards
  )

(deftest test-rank-value
  (testing "Returns correct rank values"
    (let [deck (sut/create-deck)
          hearts (take 13 deck)]
      (doseq [rank (range 2 15)]
        (is (= rank (sut/rank-value (nth hearts (- rank 2)))))))))

(deftest test-get-ranks
  (testing "Returns ranks from hand"
    (let [expected [:2 :2 :ace :ace :king]
          actual (sut/get-ranks (:2-pair hands))]
      (is (= expected actual)))))

(deftest test-get-suits
  (testing "Returns suits from hand"
    (let [expected [:hearts :clubs :spades :hearts :diamonds]
          actual (sut/get-suits (:2-pair hands))]
      (is (= expected actual)))))

(deftest test-rank-frequencies
  (testing "Returns correct frequencies"
    (let [expected {:2 2 :ace 2 :king 1}
          actual (sut/rank-frequencies (:2-pair hands))]
          (is (= expected actual)))))

(deftest test-has-n-of-kind
  (testing "Correctly identify n of a kind"
    (is (sut/has-n-of-kind? (:low-pair hands) 2))
    (is (not (sut/has-n-of-kind? (:low-pair hands) 3)))
    (is (sut/has-n-of-kind? (:3-of-a-kind hands) 3))
    (is (not (sut/has-n-of-kind? (:3-of-a-kind hands) 4)))
    (is (sut/has-n-of-kind? (:4-of-a-kind hands) 4))))

(deftest test-is-flush
  (testing "Correctly identify flush"
    (is (sut/is-flush? (:flush hands)))
    (is (not (sut/is-flush? (:not-flush hands))))))

(deftest test-is-straight
  (testing "Correctly identify straight"
    (is (sut/is-straight? (:low-straight hands)))
    (is (sut/is-straight? (:high-straight hands)))
    (is (not (sut/is-straight? (:flush hands))))))

(deftest test-is-straight-flush
  (testing "Correctly identify straight flush"
    (is (sut/is-straight-flush? (:straight-flush hands)))
    (is (not (sut/is-straight-flush? (:low-straight hands))))
    (is (not (sut/is-straight-flush? (:flush hands))))))

(deftest test-is-royal-flush
  (testing "Correctly identify royal flush"
    (is (sut/is-royal-flush? (:royal-flush hands)))
    (is (not (sut/is-royal-flush? (:straight-flush hands))))
    (is (not (sut/is-royal-flush? (:flush hands))))))

(deftest test-is-four-of-kind
  (testing "Correctly identify four of a kind"
    (is (sut/is-four-of-kind? (:4-of-a-kind hands)))
    (is (not (sut/is-four-of-kind? (:3-of-a-kind hands))))))

(deftest test-is-full-house
  (testing "Correctly identify full house"
    (is (sut/is-full-house? (:full-house hands)))
    (is (not (sut/is-full-house? (:3-of-a-kind hands)))
        (is (not (sut/is-full-house? (:2-pair hands)))))))

(deftest test-is-three-of-kind
  (testing "Correctly identify three of a kind"
      (is (sut/is-three-of-kind? (:3-of-a-kind hands)))
      (is (sut/is-three-of-kind? (:full-house hands))) ;; A full house also contains a three of a kind
      (is (not (sut/is-three-of-kind? (:low-pair hands))))))

  (deftest test-is-two-pair
    (testing "Correctly identify two pair"
      (is (sut/is-two-pair? (:2-pair hands)))
      (is (not (sut/is-two-pair? (:low-pair hands))))
      (is (not (sut/is-two-pair? (:full-house hands))))))

(deftest test-is-pair
  (testing "Correctly identify jacks or better pair"
    (is (sut/is-pair? (:high-pair hands)))
    (is (sut/is-pair? (:ace-pair hands)))
    (is (not (sut/is-pair? (:low-pair hands))))))

(deftest test-evaluate-hand
  (testing "Evaluates hands correctly"
    (is (= :royal-flush (:hand-type (sut/evaluate-hand (:royal-flush hands)))))
    (is (= :straight-flush (:hand-type (sut/evaluate-hand (:straight-flush hands)))))
    (is (= :4-of-a-kind (:hand-type (sut/evaluate-hand (:4-of-a-kind hands)))))
    (is (= :full-house (:hand-type (sut/evaluate-hand (:full-house hands)))))
    (is (= :flush (:hand-type (sut/evaluate-hand (:flush hands)))))
    (is (= :straight (:hand-type (sut/evaluate-hand (:low-straight hands)))))
    (is (= :3-of-a-kind (:hand-type (sut/evaluate-hand (:3-of-a-kind hands)))))
    (is (= :2-pair (:hand-type (sut/evaluate-hand (:2-pair hands)))))
    (is (= :jacks-or-better (:hand-type (sut/evaluate-hand (:high-pair hands)))))
    (is (= nil (:hand-type (sut/evaluate-hand (:low-pair hands)))))
    (is (= nil (:hand-type (sut/evaluate-hand (:loser hands)))))))

(deftest test-draw-new-cards
  (testing "Keeps held cards and replaces others"
    (let [{:keys [deal remaining]} (sut/deal-new-hand)
          with-held-cards (-> deal (sut/toggle-hold 0) (sut/toggle-hold 4))
          {:keys [hand]} (sut/draw-new-cards with-held-cards remaining)]
      (is (= 5 (count hand)))
      (is (contains? (set hand) (first with-held-cards)))
      (is (not (contains? (set hand) (nth with-held-cards 1))))
      (is (not (contains? (set hand) (nth with-held-cards 2))))
      (is (not (contains? (set hand) (nth with-held-cards 3))))
      (is (contains? (set hand) (last with-held-cards))))))

(deftest test-score-game

  (testing "Correctly scores hand with jacks or better"
    (let [game-state {:hand (:high-pair hands) :bet 5 :cash 100}
          {:keys [winnings cash winning-type]} (sut/score-game game-state)]
      (is (= 5 winnings))
      (is (= 105 cash))
      (is (= :jacks-or-better winning-type))))

  (testing "Correctly scores hand with royal flush and max bet"
    (let [game-state {:hand (:royal-flush hands) :bet 5 :cash 100}
          {:keys [winnings cash winning-type]} (sut/score-game game-state)]
      (is (= 4000 winnings))
      (is (= 4100 cash))
      (is (= :royal-flush winning-type))))

  (testing "Correctly scores hand with royal flush and NON max bet"
    (let [game-state {:hand (:royal-flush hands) :bet 3 :cash 100}
          {:keys [winnings cash winning-type]} (sut/score-game game-state)]
      (is (= 750 winnings))
      (is (= 850 cash))
      (is (= :royal-flush winning-type))))

  (testing "Correctly scores a losing hand"
    (let [game-state {:hand (:loser hands) :bet 5 :cash 100}
          {:keys [winnings cash winning-type]} (sut/score-game game-state)]
      (is (= 0 winnings))
      (is (= 95 cash))
      (is (= nil winning-type)))))