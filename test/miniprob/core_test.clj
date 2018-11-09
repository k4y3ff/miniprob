(ns miniprob.core-test
  (:require [clojure.test :refer :all]
            [miniprob.core :refer :all])
  (:refer-clojure :exclude [eval]))

(deftest numbers-test
  (testing "Numbers evaluate to themselves."
    (is (= (eval 0) 0))
    (is (= (eval 1) 1))))

(deftest plus-test
  (testing "+ adds numbers as expected."
    (is (= (eval '(+ 1 2)) 3))
    (is (= (eval '(+ 3 (+ 1 2))) 6))))

(deftest minus-test
  (testing "- subtracts numbers as expected"
    (is (= (eval '(- 3 2)) 1))))

(deftest primitive-procedures-test
  (testing "an expression with both - and + evaluates as expected"
    (is (= (eval '(+ 1 (- 3 2))) 2))))

(deftest flip-no-args-test
  (testing "flip returns 0 50% of the time and 1 50% of the time"
    (let [results (repeatedly 100 #(eval '(flip)))
          zero-count (count (filter zero? results))
          one-count (count (filter #(= % 1) results))]
      (is (= (+ zero-count one-count) 100))
      (is (and (>= zero-count 40) (<= zero-count 60)))
      (is (and (>= one-count 40) (<= one-count 60))))))

(deftest flipping-coins-test
  (testing "flip evaluates correctly when part of an expression with other
      operators"
    (let [results (repeatedly 100 #(eval '(+ (flip) 2)))]
      (is (empty? (filter #(not (contains? #{2 3} %)) results))))
    (let [results (repeatedly 100 #(eval '(+ (flip) (flip))))]
      (is (empty? (filter #(not (contains? #{0 1 2} %)) results))))))

(deftest flip-args-exist-test
  (testing "when eval is provided with a trace and flip is provided with a
      call-site tag, flip returns the expected value from the trace"
    (is (= (eval '(flip :tag) {:tag 0}) 0))
    (is (= (eval '(+ (flip :a) (flip :b)) {:a 2 :b 3}) 5))))

(deftest flip-args-do-not-exist-test
  (testing "when eval is provided with a trace and flip is provided with a
      call-site tag, flip returns either 0 or 1 if the tag does not exist in
      the trace"
    (let [results (repeatedly 100 #(eval '(flip :tag) {}))]
      (is (zero? (count (filter #(and (not (zero? %)) (not= 1 %)) results)))))))

(deftest flip-args-no-args-test
  (testing "when eval is provided with a trace, and some but not all flips are
      provided with a call-site tag, flip returns expected trace values when
      indicated and either 0 or 1 otherwise"
    (let [results (repeatedly 100
                              #(eval '(+ (flip) (flip :a) (flip :b))
                                      {:a 0 :b 1}))]
      (is (zero? (count (filter #(and (not= 1 %) (not= 2 %)) results)))))))

(deftest fn-simple-test
  (testing "simple anonymous functions that can return at most one value with no
      flips work as expected"
    (is (= (eval '((fn [x] (+ 2 x)) 3) {}) 5))
    (is (= (eval '((fn [] 0)) {}) 0))
    (is (= (eval '((fn [f] (f)) (fn [] 0)) {}) 0))))

(deftest fn-flip-no-args-test
  (testing "anonymous functions containing a flip with no arguments work as
      expected"
    (let [results (repeatedly 100
                              #(eval '((fn [f] (f)) (fn [] (flip))) {}))]
      (is (zero? (count (filter #(and (not (zero? %)) (not= 1 %)) results)))))))

(deftest fn-flip-tags-test
  (testing "anonymous functions containing a flip with a tag work as expected"
    (let [results (repeatedly 100
                              #(eval '((fn [f] (f)) (fn [] (+ (flip) (flip :tag)))) {:tag 1}))]
      (is (zero? (count (filter #(and (not= 1 %) (not= 2 %)) results)))))
    (is (= (eval '((fn [f] (+ (f) (f))) (fn [] (flip :tag))) 2)))))

(deftest fn-args-fn-test
  (testing "anonymous functions that are passed another fn as an argument work
      as expected"
    (is (= (eval '((fn [x] ((fn [] x))) 0)) 0))
    (is (= (eval '((fn [x] ((fn [x] x) 1)) 0)) 1))))

(deftest fn-args-symbol-test
  (testing "anonymous functions that are passed a symbol as an argument work
      as expected"
    (is (= (eval '((fn [+ x y] (+ x y)) - 2 1))) 1)))

(deftest equality-test
  (testing "equal sign = performs simple equality check"
    (is (eval '(= 0 0)))))

(deftest if-test
  (testing "if operator behaves as expected when passed a boolean value as the
      first argument"
    (is (zero? (eval '(if true 0 1))))
    (is (= (eval '(if false 0 1)) 1))))

(deftest if-check-test
  (testing "if operator behaves as expected when passed an equality check as the
      first argument"
    (is (zero? (eval '(if (= 0 0) 0 1))))
    (is (= (eval '(if (= 0 1) 0 1)) 1))))

(deftest if-fn-fn-test
  (testing "if operator behaves as expected when an argument is an argument is
      an anonymous function that calls another anonymous function"
    (is (zero? (eval '(if true 0 ((fn [f] (f f)) (fn [f] (f f)))))))
    (is (zero? (eval '(if false ((fn [f] (f f)) (fn [f] (f f))) 0))))))
