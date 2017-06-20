
(ns respo.app.comp.task
  (:require-macros [respo.macros
                    :refer
                    [defcomp div input span button span-> space-> value->]])
  (:require [hsl.core :refer [hsl]] [respo.core] [respo.app.style.widget :as widget]))

(def style-task {:display :flex, :padding "4px 0px"})

(defn on-click [props state] (fn [event dispatch!] (println "clicked.")))

(defn handle-done [task-id] (fn [e dispatch!] (dispatch! :toggle task-id)))

(def style-done
  {:width 32, :height 32, :outline :none, :border :none, :vertical-align :middle})

(defn on-text-change [task]
  (fn [event dispatch!]
    (let [task-id (:id task), text (:value event)]
      (dispatch! :update {:id task-id, :text text}))))

(defn handle-remove [task] (fn [e dispatch!] (dispatch! :remove (:id task))))

(defn on-text-state [cursor] (fn [e dispatch!] (dispatch! :states [cursor (:value e)])))

(defcomp
 comp-task
 (states task)
 (let [state (or (:data states) "")]
   (div
    {:style style-task}
    (value-> task {:right 8})
    (button
     {:style (merge
              style-done
              {:background-color (if (:done? task) (hsl 200 20 80) (hsl 200 80 70))}),
      :event {:click (handle-done (:id task))}})
    (space-> 8 nil)
    (input
     {:value (:text task), :style widget/input, :event {:input (on-text-change task)}})
    (space-> 8 nil)
    (input {:value state, :style widget/input, :event {:input (on-text-state cursor)}})
    (space-> 8 nil)
    (div {:style widget/button, :event {:click (handle-remove task)}} (span-> "Remove" nil))
    (space-> 8 nil)
    (div {} (span-> state nil)))))