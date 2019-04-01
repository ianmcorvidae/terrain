(ns terrain.routes.analyses
  (:require [compojure.api.sweet :refer [describe]]
            [common-swagger-api.schema :refer [context GET PATCH POST DELETE]]
            [common-swagger-api.schema.quicklaunches :refer [QuickLaunch
                                                             NewQuickLaunch
                                                             UpdateQuickLaunch
                                                             QuickLaunchFavorite
                                                             NewQuickLaunchFavorite
                                                             QuickLaunchUserDefault
                                                             UpdateQuickLaunchUserDefault
                                                             NewQuickLaunchUserDefault
                                                             QuickLaunchGlobalDefault
                                                             UpdateQuickLaunchGlobalDefault
                                                             NewQuickLaunchGlobalDefault]]

            [common-swagger-api.schema.apps :refer [AnalysisSubmission]]
            [ring.util.http-response :refer [ok]]
            [terrain.util :refer [optional-routes]]
            [schema.core :as s]
            [terrain.util.config :as config]
            [terrain.clients.analyses :as analyses])
  (:import [java.util UUID]))

(s/defschema DeletionResponse
  {:id (describe UUID "The UUID of the resource that was deleted")})

(defn secured-quicklaunch-routes
  "The routes for accessing analyses information. Currently limited to Quick
   Launches."
  []
  (optional-routes [config/app-routes-enabled])

  (context "/quicklaunches" []
    :tags ["analyses-quicklaunches"]

    (GET "/:id" [id]
      :summary     "Get Quick Launch information by its UUID."
      :description "Gets Quick Launch information, including the UUID, the name of
      the user that owns it, and the submission JSON"
      :return  QuickLaunch
      (ok (analyses/get-quicklaunch id)))

    (PATCH "/:id" [id]
      :body        [quicklaunch UpdateQuickLaunch]
      :return      QuickLaunch
      :summary     "Modifies an existing Quick Launch"
      :description "Modifies an existing Quick Launch, allowing the caller to change
      owners and the contents of the submission JSON"
      (ok (analyses/update-quicklaunch id quicklaunch)))

    (POST "/" []
      :body        [quicklaunch NewQuickLaunch]
      :return      QuickLaunch
      :summary     "Adds a Quick Launch to the database"
      :description "Adds a Quick Launch and corresponding submission information to the
      database. The username passed in should already exist. A new UUID will be
      assigned and returned."
      (ok (analyses/add-quicklaunch quicklaunch)))

    (DELETE "/:id" [id]
      :return      DeletionResponse
      :summary     "Deletes a Quick Launch"
      :description "Deletes a Quick Launch from the database. Will returns a success
      even if called on a Quick Launch that has either already been deleted or never
      existed in the first place"
      (ok (analyses/delete-quicklaunch id)))))

(defn secured-quicklaunch-favorites-routes
  []
  (optional-routes [config/app-routes-enabled])

  (context "/quicklaunch/favorites" []
    :tags ["analyses-quicklaunch-favorites"]

    (GET "/:id" [id]
      :return      QuickLaunchFavorite
      :summary     "Get information for a favorited Quick Launch"
      :description "Get information for a favorited Quick Launch, including the
      UUID, the user that favorited it, and the Quick Launch UUID"
      (ok (analyses/get-quicklaunch-favorite id)))

    (GET "/" []
      :return      [QuickLaunchFavorite]
      :summary     "Get information about all favorited Quick Launches"
      :description "Get in information about all favorited Quick Launches by the
      logged in user, including their UUIDs and Quick Launch UUIDS"
      (ok (analyses/get-all-quicklaunch-favorites)))

    (POST "/" []
      :body        [fave NewQuickLaunchFavorite]
      :return      QuickLaunchFavorite
      :summary     "Favorite a Quick Launch"
      :description "Favorite a Quick Launch for the logged in user"
      (ok (analyses/add-quicklaunch-favorite fave)))

    (DELETE "/:id" [id]
      :return      DeletionResponse
      :summary     "Un-favorite a Quick Launch"
      :description "Un-favorite a Quick Launch for the logged in user. Does not
      delete the Quick Launch, just the entry that indicated that it was a
      favorite of the user"
      (ok (analyses/delete-quicklaunch-favorite id)))))

(defn secured-quicklaunch-user-defaults-routes
  []
  (context "/quicklaunch/defaults/user" []
    :tags ["analyses-quicklaunch-user-defaults"]

    (GET "/:id" [id]
      :return      QuickLaunchUserDefault
      :summary     "Get information for a user-defined Quick Launch default"
      :description "Get information for a user-defined Quick Launch default.
      These are not the ones defined by the app creator, they're the user
      overrides"
      (ok (analyses/get-quicklaunch-user-default id)))

    (GET "/" []
      :return      [QuickLaunchUserDefault]
      :summary     "Get information for all of the user-defined Quick Launch defaults
      for the logged in user"
      :description "Get information for all of the user-defined Quick Launch
      defaults for the logged-in user. Includes all of the info returned by
      the endpoint that lists individual user defaults"
      (ok (analyses/get-all-quicklaunch-user-defaults)))

    (PATCH "/:id" [id]
      :body        [update UpdateQuickLaunchUserDefault]
      :return      QuickLaunchUserDefault
      :summary     "Edit a user-defined Quick Launch default"
      :description "Edit a user-defined Quick Launch default. Note that most or
      all of the fields in the JSON body are optional"
      (ok (analyses/update-quicklaunch-user-default id update)))

    (POST "/" []
      :body        [new NewQuickLaunchUserDefault]
      :return      QuickLaunchUserDefault
      :summary     "Set a user-defined Quick Launch default"
      :description "Set a user-defined Quick Launch default for the logged-in
      user"
      (ok (analyses/add-quicklaunch-user-default new)))

    (DELETE "/:id" [id]
      :return      DeletionResponse
      :summary     "Delete a user-defined Quick Launch default"
      :description "Delete a user-defined Quick Launch default. Does not delete
      the Quick Launch or the global Quick Launch default"
      (ok (analyses/delete-quicklaunch-user-default id)))))

(defn secured-quicklaunch-global-defaults-routes
  []
  (context "/quicklaunch/defaults/global" []
    :tags ["analyses-quicklaunch-global-defaults"]

    (GET "/:id" [id]
      :return      QuickLaunchGlobalDefault
      :summary     "Get information for a globally-defined Quick Launch default"
      :description "Get information for a globally-defined Quick Launch default.
      These are the ones defined by the app creator"
      (ok (analyses/get-quicklaunch-global-default id)))

    (GET "/" []
      :return      [QuickLaunchGlobalDefault]
      :summary     "Get information for all of the globally-defined Quick Launch
      defaults"
      :description "Get information for all of the globally-defined Quick Launch
      defaults. Includes all of the info returned by the endpoint that lists
      individual user defaults"
      (ok (analyses/get-all-quicklaunch-global-defaults)))

    (PATCH "/:id" [id]
      :body        [update UpdateQuickLaunchGlobalDefault]
      :return      QuickLaunchGlobalDefault
      :summary     "Edit a globally-defined Quick Launch default"
      :description "Edit a globally-defined Quick Launch default. Note that most
      or all of the fields in the JSON body are optional"
      (ok (analyses/update-quicklaunch-global-default id update)))

    (POST "/" []
      :body        [new NewQuickLaunchGlobalDefault]
      :return      QuickLaunchGlobalDefault
      :summary     "Set a globally-defined Quick Launch default"
      :description "Set a globally-defined Quick Launch default"
      (ok (analyses/add-quicklaunch-global-default new)))

    (DELETE "/:id" [id]
      :return      DeletionResponse
      :summary     "Delete a globally-defined Quick Launch default"
      :description "Delete a globally-defined Quick Launch default"
      (ok (analyses/delete-quicklaunch-global-default id)))))
