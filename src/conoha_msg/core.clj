(ns conoha-msg.core
  (:require [clojure.data.json :as json]
            [clojure.string :as s]
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

(defn get-token
  ([account]
   (get-token (:user account) (:passwd account) (:tenantid account)))
  ([user pass tenantid]
   (let [json-str (get-token-body user pass tenantid)]
     (:id (:token (:access (json/read-str (:body json-str) :key-fn keyword)))))))

(defn get-request
  ([url token]
   (get-request url token nil))
  ([url token query]
   (client/get url
               {:headers {"Accept" "application/json"
                          "X-Auth-Token" token}
                :query-params query})))

(defn build-uri [api-type ver]
  (format "https://%s.tyo1.conoha.io/%s"))

(defn build-networking-uri
  ([] (s/join (build-uri "networking" "v2.0") "/security-groups")))
  
(defn build-account-uri
  ([tenantid suffix] (format (s/join (build-uri "account" "v1") "/%s%s") tenantid suffix)))

(defn build-compute-uri
  ([tenantid suffix] (format (s/join (build-uri "compute" "v2") "/%s%s") tenantid suffix)))

(defn get-security-group [token]
  (get-request (build-networking-uri) token))

(defn get-notification-list
  ([tenantid token offset limit]
   (let [uri (build-account-uri tenantid "/notifications")]
     (get-request uri token {"offset" offset, "limit" limit})))
  ([tenantid token]
   (get-notification-list tenantid token 0 1000)))

(defn get-notification-detail [tenantid token notificationid]
  (let [uri (build-account-uri tenantid (format "/notifications/%s" notificationid))]
    (get-request uri token)))

(defn get-payment-history [tenantid token]
  (let [uri (build-account-uri tenantid "/payment-history")]
    (get-request uri token)))

(defn get-virtualmachine-list [tenantid token]
  (let [uri (build-compute-uri tenantid "/servers")]
    (get-request uri token)))
