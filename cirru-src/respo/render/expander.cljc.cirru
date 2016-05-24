
ns respo.render.expander $ :require
  [] clojure.string :as string
  [] respo.util.time :refer $ [] io-get-time
  [] respo.util.format :refer $ [] purify-element
  [] respo.util.detect :refer $ [] component? element?

defn keyword->string (x)
  subs (str x)
    , 1

declare render-component

declare render-element

defn vector-contains? (outer-vec inner-vec)
  cond
    (and (>= (count outer-vec) (, 0)) (= (count inner-vec) (, 0))) true

    (and (= (count outer-vec) (, 0)) (> (count inner-vec) (, 0))) false

    :else $ if
      = (first outer-vec)
        first inner-vec
      recur (subvec outer-vec 1)
        subvec inner-vec 1
      , false

defn filter-states (partial-states coord)
  ->> partial-states
    filter $ fn (entry)
      vector-contains? (key entry)
        , coord

    into $ {}

defn render-markup
  markup states build-mutate coord component-coord
  if (component? markup)
    render-component markup (filter-states states coord)
      , build-mutate coord
    render-element markup states build-mutate coord component-coord

defn render-children
  children states build-mutate coord comp-coord
  -- println "|render children:" children
  ->> children
    map $ fn (child-entry)
      let
        (k $ first child-entry)
          child-element $ last child-entry
        [] k $ if (some? child-element)
          render-markup child-element states build-mutate (conj coord k)
            , comp-coord
          , nil

    filter $ fn (entry)
      some? $ last entry
    into $ sorted-map

defn render-element
  markup states build-mutate coord comp-coord
  let
    (children $ :children markup)
      child-elements $ render-children children states build-mutate coord comp-coord
    -- println "|children should have order:" (pr-str children)
      pr-str child-elements
      pr-str markup
    assoc markup :coord coord :c-coord comp-coord :children child-elements

defn render-component
  markup states build-mutate coord
  let
    (begin-time $ io-get-time)
      args $ :args markup
      component $ first markup
      init-state $ :init-state markup
      new-coord $ conj coord (:name markup)
      state $ if (contains? states new-coord)
        get states new-coord
        apply init-state args
      render $ :render markup
      half-render $ apply render args
      mutate $ build-mutate new-coord
      markup-tree $ half-render state mutate
      tree $ render-element markup-tree states build-mutate new-coord new-coord
      cost $ - (io-get-time)
        , begin-time

    -- println "|markup tree:" $ pr-str markup-tree
    assoc markup :coord coord :tree tree :cost cost

defn render-app (markup states build-mutate)
  -- .info js/console "|render loop, states:" $ pr-str states
  render-markup markup states build-mutate ([])
    []