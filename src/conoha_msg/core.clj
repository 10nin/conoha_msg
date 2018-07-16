(ns conoha-msg.core
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clj-http.client :as client])
  (:import java.net.URL))

(defn get-json [address]
  "get json from address (address is direct link to json)"
  (let [url (URL. address)]
    (with-open [r (io/reader (.openStream url))]
      (json/read r :key-fn keyword))))

(defn post-json [address]
  "post json to address."
  (let [url (URL. address)]
    ))
(defn get-identity-version []
  (client/get "https://identity.tyo1.conoha.io/"))

(defn get-token [user pass tenantid]
  )
