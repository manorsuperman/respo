
ns respo.examples.dom-tree $ :require
  [] clojure.string :as string
  [] respo.render.differ :refer $ [] find-element-diffs find-props-diffs

def example-1 $ {} (:name :div)
  :props $ sorted-map
  :children $ sorted-map
  :coord $ []

def example-2 $ {} (:name :span)
  :props $ sorted-map
  :coord $ []
  :children $ sorted-map

def example-3 $ {} (:name :div)
  :props $ sorted-map :class |demo
  :coord $ []
  :children $ sorted-map

def example-4 $ {} (:name :div)
  :props $ sorted-map :class |another
  :coord $ []
  :children $ sorted-map

def example-5 $ {} (:name :div)
  :props $ sorted-map
  :coord $ []
  :children $ sorted-map 1 $ {} (:name :div)
    :props $ sorted-map
    :coord $ [] 1
    :children $ sorted-map

def example-6 $ {} (:name :div)
  :props $ sorted-map :class |example-6
  :coord $ []
  :children $ sorted-map 1 $ {} (:name :div)
    :props $ sorted-map :style $ sorted-map :color |red
    :coord $ [] 1
    :children $ sorted-map

def example-7 $ {} (:name :div)
  :props $ sorted-map :class |example-7 :spell-check false
  :coord $ []
  :children $ sorted-map 1 $ {} (:name :div)
    :props $ sorted-map :style $ sorted-map :color |yellow :display |inline-block
    :coord $ [] 1
    :children $ sorted-map 0 $ {} (:name :span)
      :props $ sorted-map
      :coord $ [] 1 0
      :children $ sorted-map

defn diff-demos ()
  println "|DOM diff 1->2:" $ find-element-diffs ([])
    []
    , example-1 example-2
  newline
  println "|DOM diff 1->3:" $ find-element-diffs ([])
    []
    , example-1 example-3
  newline
  println "|DOM diff 1->4:" $ find-element-diffs ([])
    []
    , example-1 example-4
  newline
  println "|DOM diff 1->5:" $ find-element-diffs ([])
    []
    , example-1 example-5
  newline
  println "|DOM diff 3->4:" $ find-element-diffs ([])
    []
    , example-3 example-4
  newline
  println "|DOM diff 3->5:" $ find-element-diffs ([])
    []
    , example-3 example-5
  newline
  println "|DOM diff 6->7:" $ find-element-diffs ([])
    []
    , example-6 example-7

def props-demo-1 $ {} (:placeholder |Task)
  :style $ {} :color |red
  :value |

def props-demo-2 $ {} (:placeholder |Task)
  :style $ {} :color |red
  :value |d

defn diff-props-demos ()
  println "|props diff:" $ find-props-diffs ([])
    []
    , props-demo-1 props-demo-2