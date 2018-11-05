(ns miniprob.core
  (:gen-class)
  (:refer-clojure :exclude [eval]))

(defn flip
  ([]
    "Evaluates to 0 with probability 0.5 and 1 with probability 0.5."
    (if (<= (rand) 0.5) 0 1))
  ([tag trace]
    """
    If the tag is a key in the trace, returns the corresponding value from the
    trace. Otherwise, evaluates to 0 with a probability 0.5 and 1 with
    probability 0.5.
    """
    (let [t (get trace tag)]
      (if (nil? t)
        (flip)
        t))))

(defn eval
  ([exp]
    """
    Evaluates the given expression.
    """
    (eval exp {}))
  ([exp trace]
    """
    Evaluates the given expression, overriding flip call sites using the given
    trace when indicated.
    """
    (cond
      (number? exp)     exp
      (= '+ exp)        clojure.core/+
      (= '- exp)        clojure.core/-
      (seq? exp)
        (let [[operator & args] exp]
          (cond
            (= 'flip operator) (flip (first args) trace)
            :else
              (apply (eval operator trace)
                     (map #(eval % trace) args))))
      :else
        (->> exp
             (str "THERE WAS AN ATTEMPT: ")
             (Exception.)
             throw))))
