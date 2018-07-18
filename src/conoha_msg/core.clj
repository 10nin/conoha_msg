(ns conoha-msg.core
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clj-http.client :as client])
  (:import java.net.URL))

(defn get-identity-version []
  (client/get "https://identity.tyo1.conoha.io/"))

(defn get-token-body [user pass tenantid]
  (let [req (format "{\"auth\": {\"passwordCredentials\": {\"username\": \"%s\", \"password\": \"%s\"}, \"tenantId\": \"%s\"}}" user pass tenantid)]
    (client/post "https://identity.tyo1.conoha.io/v2.0/tokens"
                 {:body req
                  :headers {"Accept" "application/json"}})))

(defn get-token [user pass tenantid]
  (let [json-str (get-token-body user pass tenantid)]
    (:id (:token (:access (json/read-str (:body json-str) :key-fn keyword))))))

(defn get-security-group [token]
  (client/get "https://networking.tyo1.conoha.io/v2.0/security-groups"
              {:headers {"Accept" "application/json"
                         "X-Auth-Token" token}}))

