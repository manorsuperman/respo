
(ns respo.update.core
  (:require [clojure.string :as string]))

(defn update-transform [old-store op-type op-data op-id]
  (println (pr-str old-store) (pr-str op-type) (pr-str op-data))
  (case
    op-type
    :add
    (conj old-store {:id op-id, :text op-data})
    :remove
    (->>
      old-store
      (filter (fn [task] (not (= (:id task) op-data))))
      (into []))
    :clear
    []
    :update
    (let [task-id (:id op-data) text (:text op-data)]
      (->>
        old-store
        (map
          (fn [task]
            (if (= (:id task) task-id) (assoc task :text text) task)))
        (into [])))
    old-store))
