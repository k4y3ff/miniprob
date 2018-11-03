(ns miniprob.core-test
  (:require [clojure.test :refer :all]
            [miniprob.core :refer :all])
  (:refer-clojure :exclude [eval]))

(deftest numbers-test
  (testing "Numbers evaluate to themselves."
    (is (= (eval 0) 0))
    (is (= (eval 1) 1))))
