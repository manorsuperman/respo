
(ns respo.component.wrap
  (:require [respo.alias :refer [create-comp div]]
            [respo.component.text :refer [comp-text]]))

(defn render [] (fn [state mutate!] (comp-text "wrap" nil)))

(def comp-wrap (create-comp :wrap render))
