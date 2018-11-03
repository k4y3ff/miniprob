(defproject miniprob "0.1.0-SNAPSHOT"
  :description "An evaluator for a tiny probabilistic language."
  :url "http://github.com/k4y3ff/miniprob"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot miniprob.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
