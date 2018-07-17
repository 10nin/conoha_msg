(ns conoha-msg.core
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clj-http.client :as client])
  (:import java.net.URL))

(defn get-identity-version []
  (client/get "https://identity.tyo1.conoha.io/"))

(defn get-token-body [user pass tenantid]
  (let [req (format "{\"auth\": {\"passwordCredentials\": {\"username\": \"%s\", \"password\": \"%s\"}, \"tenantId\": \"%s\"}}" user pass tenantid)]
    (print req)
    (client/post "https://identity.tyo1.conoha.io/v2.0/tokens"
                 {:body req
                  :accept "application/json"})))
