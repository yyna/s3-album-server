(ns datafy
  (:require [clojure.core.protocols :as p]))

(extend-protocol p/Datafiable
  java.util.Map
  (p/datafy [o] (let [entries (.entrySet o)]
                  (reduce (fn [m [^String k v]]
                            (assoc m (keyword k) (p/datafy v)))
                          {} entries)))
  java.util.List
  (p/datafy [o] (vec (map p/datafy o))))