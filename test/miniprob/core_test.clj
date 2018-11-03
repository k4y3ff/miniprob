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
