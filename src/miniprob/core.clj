(ns miniprob.core
  (:gen-class)
  (:refer-clojure :exclude [eval]))

(defn flip []
  """
  Evaluates to 0 with probability 0.5 and 1 with probability 0.5.
  """
  (if (<= (rand) 0.5) 0 1))

(defn eval [exp]
  """
  Evaluates the given expression.
  """
  (cond
    (number? exp)     exp
    (= '+ exp)        clojure.core/+
    (= '- exp)        clojure.core/-
    (seq? exp)
      (let [[operator & args] exp]
        (cond
          (= 'flip operator) (flip)
          :else
            (apply (eval operator)
                   (map #(eval %) args))))
    :else
      (->> exp
           (str "THERE WAS AN ATTEMPT: ")
           (Exception.)
           throw)))
