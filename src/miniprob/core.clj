(ns miniprob.core
  (:gen-class)
  (:refer-clojure :exclude [eval]))

(defn eval [exp]
  """
  Evaluates the given expression.
  """
  (cond
    (number? exp) exp
    :else
      (->> exp
           (str "THERE WAS AN ATTEMPT: ")
           (Exception.)
           throw)))
