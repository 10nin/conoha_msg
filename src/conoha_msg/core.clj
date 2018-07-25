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

(defn get-request
  ([url token]
   (get-request url token nil))
  ([url token query]
   (client/get url
               {:headers {"Accept" "application/json"
                          "X-Auth-Token" token}
                :query-params query})))
 
(defn get-security-group [token]
  (get-request "https://networking.tyo1.conoha.io/v2.0/security-groups" token))

(defn get-notification-list
  ([tenantid token offset limit]
   (let [uri (format "https://account.tyo1.conoha.io/v1/%s/notifications" tenantid)]
     (get-request uri token {"offset" offset, "limit" limit})))
  ([tenantid token]
   (get-notification-list tenantid token 0 1000)))

(defn get-notification-detail [tenantid token notificationid]
  (let [uri (format "https://account.tyo1.conoha.io/v1/%s/notifications/%s" tenantid notificationid)]
    (get-request uri token)))
