(ns rts.sensor
  (:require [langohr.core :as rmq]
            [langohr.channel :as lch]
            [langohr.queue :as lq]
            [langohr.basic :as lb]
            [rts.utils :refer [state get-attr get-unit get-iso-dt ansi-green
                               ansi-reset rfill]]))

(defn run [name generator]
  (let [con (rmq/connect)
        ch (lch/open con)
        unit (get-unit name)]
    (lq/declare ch name {:exclusive false :auto-delete false})
    (loop []
      (when (> (:distance @state) 0)
        (let [data (generator)]
          (dosync (alter state assoc (get-attr name) data))
          (lb/publish ch "" name (str data))
          (printf "%s127.0.0.1 sensor %s [%s] \"measured value of %d%s\"%s\n"
                  ansi-green (rfill name) (get-iso-dt) data unit ansi-reset)
          (flush)
          (Thread/sleep 2000)
          (recur))))
    (rmq/close ch)
    (rmq/close con)
    (printf "127.0.0.1 sensor %s [%s] \"closing connection\"\n" name (get-iso-dt))
    (flush)))
