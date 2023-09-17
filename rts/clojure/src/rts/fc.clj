(ns rts.fc
  (:require [langohr.core :as rmq]
            [langohr.channel :as lch]
            [langohr.queue :as lq]
            [langohr.consumers :as lc]
            [rts.utils :refer [state ansi-blue ansi-reset get-iso-t queue-gps
                               queue-altitude queue-speed queue-pressure
                               queue-weather]]))

(defn handle-gps [_ch _meta payload]
  (let [distance (Integer/parseInt (String. payload "UTF-8"))]
    (cond
      (and (<= distance 250) (> distance 0) (not (:landing? @state)))
      (do
        (printf "%s127.0.0.1 fc landing-gear [%s] \"initiating aircraft landing\"%s\n"
                ansi-blue (get-iso-dt) ansi-reset)
        (dosync (alter state assoc :landing? true)))

      (zero? distance)
      (printf "%s127.0.0.1 fc landing-gear [%s] \"aircraft has successfully landed\"%s\n"
              ansi-blue (get-iso-dt) ansi-reset))
    (flush)))

(defn handle-altitude [_ch _meta payload]
  (let [altitude (Integer/parseInt (String. payload "UTF-8"))]
    (cond
      (:landing? @state)
      (do
        (printf "%s127.0.0.1 fc wing-flaps   [%s] \"preparing for landing, lowering wing flaps\"%s\n"
                ansi-blue (get-iso-dt) ansi-reset)
        (dosync (alter state assoc :altitude (if (= 50 (:distance @state)) 0 (quot altitude 2)))))

      (> altitude 11100)
      (do
        (printf "%s127.0.0.1 fc wing-flaps   [%s] \"altitude too high, lowering wing flaps\"%s\n"
                ansi-blue (get-iso-dt) ansi-reset)
        (dosync (alter state assoc :altitude (- altitude 100))))

      (and (< altitude 10900) (not (:landing? @state)))
      (do
        (printf "%s127.0.0.1 fc wing-flaps   [%s] \"altitude too low, raising wing flaps\"%s\n"
                ansi-blue (get-iso-dt) ansi-reset)
        (dosync (alter state assoc :altitude (+ altitude 100)))))
    (flush)))

(defn handle-speed [_ch _meta payload]
  (let [speed (Integer/parseInt (String. payload "UTF-8"))]
    (when (:landing? @state)
      (printf "%s127.0.0.1 fc main-engine  [%s] \"preparing for landing, reducing speed\"%s\n"
              ansi-blue (get-iso-dt) ansi-reset)
      (flush)
      (dosync (alter state assoc :speed (if (= 50 (:distance @state)) 0 (quot speed 2)))))))

(defn handle-pressure [_ch _meta payload]
  (let [pressure (Integer/parseInt (String. payload "UTF-8"))]
    (when (< pressure 500)
      (printf "%s127.0.0.1 fc oxygen-mask  [%s] \"dangerously low cabin pressure, deploying oxygen masks\"%s\n"
              ansi-blue (get-iso-dt) ansi-reset)
      (flush)
      (dosync (alter state assoc :pressure (+ pressure 75))))))

(defn handle-weather [_ch _meta payload]
  (let [weather (Integer/parseInt (String. payload "UTF-8"))]
    (when (< weather 3)
      (printf "%s127.0.0.1 fc main-engine  [%s] \"bad weather conditions, reducing speed\"%s\n"
              ansi-blue (get-iso-dt) ansi-reset)
      (flush)
      (dosync (alter state assoc :speed (- (:speed @state) 5))))))

(defn handle [[ch queue handler]]
  (lq/declare ch queue {:exclusive false :auto-delete false})
  (loop []
    (when (> (:distance @state) 0)
      (lc/subscribe ch queue handler {:auto-ack true})
      (recur))))

(defn run []
  (let [con (rmq/connect)
        ch-gps (lch/open con)
        ch-altitude (lch/open con)
        ch-speed (lch/open con)
        ch-pressure (lch/open con)
        ch-weather (lch/open con)
        handle-details [[ch-gps queue-gps handle-gps]
                        [ch-altitude queue-altitude handle-altitude]
                        [ch-speed queue-speed handle-speed]
                        [ch-pressure queue-pressure handle-pressure]
                        [ch-weather queue-weather handle-weather]]
        futures (map #(future (handle %)) handle-details)]
    (doseq [completion futures]
      @completion)
    (rmq/close ch-gps)
    (rmq/close ch-altitude)
    (rmq/close ch-speed)
    (rmq/close ch-pressure)
    (rmq/close ch-weather)
    (rmq/close con)))
