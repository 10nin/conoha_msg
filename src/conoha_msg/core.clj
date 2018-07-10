(ns conoha-msg.core
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io])
  (:import java.net.URL))

(def account-entry-point "https://account.tyo1.conoha.io")

(defn get-json [address]
  "get json from address (address is direct link to json)"
  (let [url (URL. address)]
    (with-open [r (io/reader (.openStream url))]
      (json/read r :key-fn keyword))))

(defn get-api-version []
  (:id (first (:versions (get-json account-entry-point)))))
