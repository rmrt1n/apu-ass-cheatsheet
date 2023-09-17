(defproject rts "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.novemberain/langohr "5.4.0"]
                 [criterium "0.4.6"]
                 [jmh-clojure "0.4.1"]]
  :plugins [[lein-cljfmt "0.9.2"]]
  :main ^:skip-aot rts.core
  :target-path "target/%s"
  :jvm-opts ["-Dclojure.compiler.direct-linking=true"]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
