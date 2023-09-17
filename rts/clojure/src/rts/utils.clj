(ns rts.utils
  (:import java.time.LocalDateTime java.time.format.DateTimeFormatter))

;; queue names
(def queue-gps "gps")
(def queue-altitude "altitude")
(def queue-speed "speed")
(def queue-pressure "pressure")
(def queue-weather "weather")

;; for colored terminal output
(def ansi-green "\u001B[32m")
(def ansi-blue "\u001B[34m")
(def ansi-reset "\u001B[0m")

(def state (ref {:distance 1000
                 :altitude 11000
                 :speed 50
                 :pressure 540
                 :weather 10
                 :landing? false}))

(def lookup {queue-gps [:distance " km"]
             queue-altitude [:altitude " m"]
             queue-speed [:speed " km/h"]
             queue-pressure [:pressure " pa"]
             queue-weather [:weather ""]})

(defn get-attr [s]
  (first (get lookup s)))

(defn get-unit [s]
  (second (get lookup s)))

(defn get-iso-dt []
  (let [dt (.format (LocalDateTime/now) DateTimeFormatter/ISO_LOCAL_DATE_TIME)]
    (subs dt 0 (.lastIndexOf dt "."))))

(defn rfill [s]
  (str s (apply str (repeat (- 8 (count s)) \space))))
