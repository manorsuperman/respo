
(ns respo.comp.container
  (:require [respo.alias :refer [create-comp div]]
            [respo.comp.todolist :refer [comp-todolist]]))

(defn render [store] (fn [state mutate!] (div {} (comp-todolist store))))

(def comp-container (create-comp :container render))
