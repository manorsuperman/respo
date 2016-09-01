
(set-env!
  :dependencies '[[org.clojure/clojure         "1.8.0"       :scope "provided"]
                  [org.clojure/clojurescript   "1.9.89"      :scope "provided"]
                  [adzerk/boot-cljs            "1.7.228-1"   :scope "test"]
                  [adzerk/boot-reload          "0.4.11"      :scope "test"]
                  [binaryage/devtools          "0.7.2"       :scope "test"]
                  [cirru/boot-cirru-sepal      "0.1.9"       :scope "test"]
                  [adzerk/boot-test            "1.1.1"       :scope "test"]
                  [mvc-works/hsl               "0.1.2"       :scope "test"]])

(require '[adzerk.boot-cljs   :refer [cljs]]
         '[adzerk.boot-reload :refer [reload]]
         '[cirru-sepal.core   :refer [transform-cirru]]
         '[adzerk.boot-test   :refer :all])

(def +version+ "0.3.10")

(task-options!
  pom {:project     'respo/respo
       :version     +version+
       :description "Responsive DOM library"
       :url         "https://github.com/respo-mvc/respo"
       :scm         {:url "https://github.com/respo-mvc/respo"}
       :license     {"MIT" "http://opensource.org/licenses/mit-license.php"}})

(deftask compile-cirru []
  (set-env!
    :source-paths #{"cirru/"})
  (comp
    (transform-cirru)
    (target :dir #{"compiled/"})))

(deftask dev []
  (set-env!
    :asset-paths #{"assets"}
    :source-paths #{"cirru/src" "cirru/app"})
  (comp
    (watch)
    (transform-cirru)
    (reload :on-jsload 'respo.main/on-jsload
            :cljs-asset-path ".")
    (cljs)
    (target)))

(deftask build-simple []
  (set-env!
    :asset-paths #{"assets"}
    :source-paths #{"cirru/src" "cirru/app"})
  (comp
    (transform-cirru)
    (cljs :compiler-options {})
    (target)))

(deftask build-advanced []
  (set-env!
    :asset-paths #{"assets"}
    :source-paths #{"cirru/src" "cirru/app"})
  (comp
    (transform-cirru)
    (cljs :optimizations :advanced :compiler-options {})
    (target)))

(deftask rsync []
  (with-pre-wrap fileset
    (sh "rsync" "-r" "target/" "tiye:repo/respo-mvc/respo" "--exclude" "main.out" "--delete")
    fileset))

(deftask send-tiye []
  (comp
    (build-advanced)
    (rsync)))

; some problems due to uglifying
(deftask build []
  (set-env!
    :source-paths #{"cirru/src"})
  (comp
    (transform-cirru)
    (pom)
    (jar)
    (install)
    (target)))

(deftask deploy []
  (set-env! :repositories #(conj % ["clojars" {:url "https://clojars.org/repo/"}]))
  (comp
    (build)
    (push :repo "clojars" :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))

(deftask watch-test []
  (set-env!
    :source-paths #{"cirru/src" "cirru/test"})
  (comp
    (watch)
    (transform-cirru)
    (test :namespaces '#{respo.html-test})))
