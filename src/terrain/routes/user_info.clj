(ns terrain.routes.user-info
  (:use [compojure.core]
        [terrain.services.user-info]
        [terrain.util]
        [terrain.util.service :only [success-response]])
  (:require [terrain.clients.iplant-groups :as ipg]
            [terrain.util.config :as config]))

(defn secured-user-info-routes
  []
  (optional-routes
   [config/user-info-routes-enabled]

   (GET "/user-search" [:as {:keys [params]}]
     (user-search (:search params)))

   (GET "/user-info" [:as {:keys [params]}]
     (user-info (as-vector (:username params))))))


(defn admin-user-info-routes
  []
  (optional-routes
   [config/user-info-routes-enabled]

   (GET "/users/:username/groups" [username]
     (success-response (ipg/list-groups-for-user username)))))
