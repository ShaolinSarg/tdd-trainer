(ns tdd-trainer.watcher.file-watcher
  (:require [tdd-trainer.data :as dta]
            [clojure-watch.core :refer [start-watch]]
            [clojure.string :as str]
            [clojure.tools.logging :as log]))

(defn file-to-vector-of-lines
  [file]
  (str/split-lines (slurp file)))


(defn get-latest-from-session
  "Returns the latest verson of the given file from session"
  [session-data filename]
  (get-in (last (filter #(contains? (set (keys %)) filename) (map #(:changed-files %) (:snapshots @session-data)))) [filename :latest]))


(defn add-file-to-changeset 
  "Adds the specified file to the given atomic change set"
  [session-data change-set file-path file-types-to-watch]
  
  (let [file-ext (clojure.string/join (drop-while #(not= \. %) file-path))]

    (if (some (conj #{} file-ext) file-types-to-watch)
      (let [previous-version (get-latest-from-session session-data file-path)
            latest-version (file-to-vector-of-lines file-path)]


        (log/info (str "adding previous version: " previous-version))
        (log/info (str "adding latest version: " latest-version))

        (swap! change-set assoc file-path {:latest latest-version}))

      @change-set)))


(defn watch-project 
  "adds a file/directory to the tdd session watcher"
  [change-set path file-types]
  (start-watch [{:path path
                 :event-types [:create :modify :delete]
                 :bootstrap (fn [path] (log/info "Starting to watch: " path))
                 :callback (fn [event filename] (do
                                                  (log/info (str "change in file: " filename))
                                                  (add-file-to-changeset dta/session-data change-set filename file-types)))
                 :options {:recursive true}}]))

