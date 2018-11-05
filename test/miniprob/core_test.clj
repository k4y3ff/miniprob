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
