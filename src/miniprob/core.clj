(ns miniprob.core
  (:gen-class)
  (:refer-clojure :exclude [eval]))

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
        (apply (eval operator) (map #(eval %) args)))
    :else
      (->> exp
           (str "THERE WAS AN ATTEMPT: ")
           (Exception.)
           throw)))
