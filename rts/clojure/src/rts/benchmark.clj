(ns rts.benchmark
  (:require [rts.core :refer [-main]])
  (:import java.time.Instant
           java.time.temporal.ChronoUnit
           java.time.Duration))

(defn benchmark []
  (let [start (.truncatedTo (Instant/now) ChronoUnit/MICROS)
        m (-main)
        end (.truncatedTo (Instant/now) ChronoUnit/MICROS)]
    (.toMillis (Duration/between start end))))

(println (benchmark))
