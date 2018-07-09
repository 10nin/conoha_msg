(ns conoha-msg.core
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io])
  (:import java.net.URL))

(defn get-json [address]
  "get json from address (address is direct link to json)"
  (let [url (URL. address)]
    (with-open [r (io/reader (.openStream url))]
      (json/read r :key-fn keyword))))
