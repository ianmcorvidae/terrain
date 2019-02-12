(ns terrain.routes.schemas.collaborator
  (:use [common-swagger-api.schema :only [describe]]
        [schema.core :only [defschema optional-key]])
  (:require [common-swagger-api.schema.groups :as group-schema]
            [common-swagger-api.schema.subjects :as subject-schema]))

(defn group-member [group-descriptor member-descriptor]
  (assoc subject-schema/Subject
    :display_name (describe String (str "The displayable " group-descriptor " " member-descriptor " name"))))

(defn group-members [group-descriptor member-descriptor member-descriptor-plural]
  {:members (describe [(group-member group-descriptor member-descriptor)]
                      (str "The list of " group-descriptor " " member-descriptor-plural))})

;; Collaborator List Schemas

(def CollaboratorListNamePathParam (describe String "The name of the collaborator list"))

(defschema CollaboratorListSearchParams
  {(optional-key :search)
   (describe String "The collaborator list name substring to search for")})

(defschema CollaboratorListRetainPermissionsParams
  {(optional-key :retain-permissions)
   (describe Boolean "Select `true` if removed users should retain permission to shared resources")})

(defschema GetCollaboratorListsResponse (group-schema/group-list "collaborator list" "collaborator lists"))
(defschema AddCollaboratorListRequest (group-schema/base-group "collaborator list"))
(defschema CollaboratorList (group-schema/group "collaborator list"))
(defschema CollaboratorListUpdate
  (select-keys (group-schema/group-update "collaborator list") (map optional-key [:name :description])))
(defschema CollaboratorListStub (group-schema/group-stub "collaborator list"))
(defschema CollaboratorListMembers (group-members "collaborator list" "member" "members"))

;; Team Schemas

(def TeamNamePathParam
  (describe String "The name of the team, including the username prefix (e.g. `username:team-name`)"))

(def TeamRequesterPathParam
  (describe String "The username of the person requesting to join the team"))

(defn team-listing-params [descriptor plural-descriptor]
  {(optional-key :search)
   (describe String (str "The " descriptor " name substring to search for"))

   (optional-key :creator)
   (describe String (str "Only " plural-descriptor " created by the user with this username will be listed if "
                         "specified"))

   (optional-key :member)
   (describe String (str "Only " plural-descriptor " to which the user with this username belongs will be listed "
                         "if specified"))})

(defschema TeamListingParams (team-listing-params "team" "teams"))

(defschema TeamJoinRequest
  {:message (describe String "A brief message to send to the team administrators")})

(defschema TeamJoinDenial
  {:message (describe String "A brief message to send to the person requesting to join the team")})

(defschema TeamListing (group-schema/group-list "team" "teams"))
(defschema AddTeamRequest
  (assoc (select-keys (group-schema/base-group "team") [:name (optional-key :description)])
    (optional-key :public_privileges)
    (describe [group-schema/ValidGroupPrivileges] "Team privileges granted to all DE users")))
(defschema Team (group-schema/group "team"))
(defschema UpdateTeamRequest (select-keys (group-schema/group-update "team") (map optional-key [:name :description])))
(defschema TeamStub (group-schema/group-stub "team"))
(defschema TeamMembers (group-members "team" "member" "members"))

;; Community Schemas

(def CommunityNamePathParam (describe String "The name of the community"))

(defschema CommunityListingParams
  (dissoc (team-listing-params "community" "communities") (optional-key :creator)))

(defschema CommunityListingEntry
  (assoc (group-schema/group "community")
    :member     (describe Boolean "True if the authenticated user belongs to the community")
    :privileges (describe [String] "The privileges the authenticated has for the community")))

(defschema CommunityListing
  {:groups (describe [CommunityListingEntry] "The list of communities in the result set")})

(defschema Community (group-schema/group "community"))

(defschema AddCommunityRequest
  (select-keys (group-schema/base-group "community") [:name (optional-key :description)]))

(defschema UpdateCommunityRequest
  (select-keys (group-schema/group-update "community") (map optional-key [:name :description])))

(defschema UpdateCommunityParams
  {(optional-key :retag-apps)
   (describe Boolean "Set to `true` to cause apps that are associated with the community to be retagged")

   (optional-key :force-rename)
   (describe Boolean "Set to `true` to force the community to be renamed even if apps are associated with it")})

(defschema CommunityStub (group-schema/group-stub "community"))

(defschema CommunityAdmins
  {:members (describe [subject-schema/Subject] "The list of community administrators")})

(defschema CommunityMembers (group-members "community" "member" "members"))

;; Admin Community Schemas

(defschema AdminCommunityListing (group-schema/group-list "community" "communities"))
