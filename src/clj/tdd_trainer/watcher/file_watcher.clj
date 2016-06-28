(ns tdd-trainer.watcher.file-watcher
  (:require [clojure-watch.core :refer [start-watch]]))


(defn add-file-to-changeset 
  "Adds the specified file to the given atomic change set"
  [change-set file-path file-types-to-watch]
  (let [file-ext (clojure.string/join (drop-while #(not= \. %) file-path))]
    (if (some (conj #{} file-ext) file-types-to-watch)
      (swap! change-set conj file-path)
      @change-set)))


(defn watch-project 
  "adds a file/directory to the tdd session watcher"
  [change-set path file-types]
  (start-watch [{:path path
                 :event-types [:create :modify :delete]
                 ;;:bootstrap (fn [path] (println "Starting to watch " path))
                 :callback (fn [event filename] (add-file-to-changeset change-set filename file-types))
                 :options {:recursive true}}]))

