(ns miniprob.core
  (:refer-clojure :exclude [apply eval]))

(declare apply)

(defrecord Procedure [params body env])

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
    (eval exp {} {}))
  ([exp trace]
    """
    Evaluates the given expression, overriding flip call sites using the given
    trace when indicated.
    """
    (eval exp trace {}))
  ([exp trace env]
    """
    Evaluates the given expression, overriding flip call sites using the given
    trace when indicated and using the given environment env to evaluate
    variables in anonymous functions.
    """
    (cond
      (boolean? exp)    exp
      (number? exp)     exp
      (= '+ exp)        clojure.core/+
      (= '- exp)        clojure.core/-
      (= '= exp)        clojure.core/=
      (symbol? exp)     (env exp)
      (seq? exp)
        (let [[operator & operands] exp]
          (cond
            (= 'if operator)
              (let [[check result else] operands]
                (if (eval check trace env)
                  result
                  else))
            (= 'flip operator)
              (flip (first operands) trace)
            (= 'fn operator)
              (let [[params body] operands]
                (Procedure. params body env))
            :else
              (apply (eval operator trace env)
                     trace
                     (map #(eval % trace env) operands)))))))

(defn apply [procedure trace args]
  """
  Given a procedure--which can be either a Clojure function or a Procedure--
  along with a trace and arguments for the procedure, applies the procedure to
  the arguments, overriding flip call sites using the given trace when
  indicated.
  """
 (if (= (type procedure) Procedure)
   (eval (:body procedure)
         trace
         (merge (:env procedure)
                (zipmap (:params procedure) args)))
   (clojure.core/apply procedure args)))
