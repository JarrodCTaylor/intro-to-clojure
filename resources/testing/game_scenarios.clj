(ns testing.game-scenarios)

(def hands {:low-pair [{:rank :2 :suit :hearts}
                       {:rank :2 :suit :clubs}
                       {:rank :ace :suit :spades}
                       {:rank :king :suit :hearts}
                       {:rank :queen :suit :diamonds}]
            :high-pair [{:rank :jack :suit :hearts}
                        {:rank :jack :suit :clubs}
                        {:rank :3 :suit :spades}
                        {:rank :4 :suit :hearts}
                        {:rank :5 :suit :diamonds}]
            :ace-pair [{:rank :ace :suit :hearts}
                       {:rank :ace :suit :clubs}
                       {:rank :3 :suit :spades}
                       {:rank :4 :suit :hearts}
                       {:rank :5 :suit :diamonds}]
            :2-pair [{:rank :2 :suit :hearts}
                     {:rank :2 :suit :clubs}
                     {:rank :ace :suit :spades}
                     {:rank :ace :suit :hearts}
                     {:rank :king :suit :diamonds}]
            :3-of-a-kind [{:rank :2 :suit :hearts}
                          {:rank :2 :suit :clubs}
                          {:rank :2 :suit :spades}
                          {:rank :king :suit :hearts}
                          {:rank :queen :suit :diamonds}]
            :4-of-a-kind [{:rank :2 :suit :hearts}
                          {:rank :2 :suit :clubs}
                          {:rank :2 :suit :spades}
                          {:rank :2 :suit :diamonds}
                          {:rank :queen :suit :diamonds}]
            :flush [{:rank :2 :suit :hearts}
                    {:rank :5 :suit :hearts}
                    {:rank :7 :suit :hearts}
                    {:rank :king :suit :hearts}
                    {:rank :queen :suit :hearts}]
            :not-flush [{:rank :2 :suit :hearts}
                        {:rank :5 :suit :clubs}
                        {:rank :7 :suit :hearts}
                        {:rank :king :suit :hearts}
                        {:rank :queen :suit :hearts}]
            :low-straight [{:rank :2 :suit :hearts}
                           {:rank :3 :suit :clubs}
                           {:rank :4 :suit :hearts}
                           {:rank :5 :suit :hearts}
                           {:rank :6 :suit :diamonds}]
            :high-straight [{:rank :10 :suit :hearts}
                            {:rank :jack :suit :clubs}
                            {:rank :queen :suit :hearts}
                            {:rank :king :suit :hearts}
                            {:rank :ace :suit :diamonds}]
            :straight-flush [{:rank :2 :suit :hearts}
                             {:rank :3 :suit :hearts}
                             {:rank :4 :suit :hearts}
                             {:rank :5 :suit :hearts}
                             {:rank :6 :suit :hearts}]
            :royal-flush [{:rank :10 :suit :hearts}
                          {:rank :jack :suit :hearts}
                          {:rank :queen :suit :hearts}
                          {:rank :king :suit :hearts}
                          {:rank :ace :suit :hearts}]
            :full-house [{:rank :2 :suit :hearts}
                         {:rank :2 :suit :clubs}
                         {:rank :2 :suit :spades}
                         {:rank :king :suit :hearts}
                         {:rank :king :suit :diamonds}]
            :loser [{:rank :2 :suit :hearts}
                    {:rank :4 :suit :clubs}
                    {:rank :6 :suit :spades}
                    {:rank :8 :suit :hearts}
                    {:rank :10 :suit :diamonds}]})