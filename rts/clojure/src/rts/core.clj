(ns rts.core
  (:gen-class)
  (:require [rts.sensor :as sensor]
            [rts.fc :as fc]
            [rts.utils :refer [state queue-gps queue-altitude queue-speed
                               queue-pressure queue-weather]]))

(defn start-sensors []
  (doseq [[queue generator]
          [[queue-gps #(- (:distance @state) 50)]
           [queue-altitude #(+ (:altitude @state) (* (rand-int 200) (rand-nth [1 -1])))]
           [queue-speed #(+ (:speed @state) (* (rand-int 5) (rand-nth [1 -1])))]
           [queue-pressure
            #(if (= 700 (:distance @state))
               300
               (+ (:pressure @state) (* (rand-int 10) (rand-nth [1 -1]))))]
           [queue-weather #(rand-int 10)]]]
    (future (sensor/run queue generator))))

(defn reset-state []
  (dosync (alter state assoc :distance 1000))
  (dosync (alter state assoc :altitude 11000))
  (dosync (alter state assoc :speed 50))
  (dosync (alter state assoc :pressure 540))
  (dosync (alter state assoc :weather 10))
  (dosync (alter state assoc :landing? false)))

(defn -main [& _args]
  (reset-state)
  (println "simulation started")
  (start-sensors)
  (fc/run)
  (shutdown-agents)
  (println "simulation completed"))
