(ns terrain.routes.schemas.qms
  (:require [common-swagger-api.schema :refer [PagingParams describe ->optional-param]]
            [schema.core :as s :refer [defschema Any optional-key maybe]])
  (:import [java.util UUID]))

(def GetAllPlansSummary "Returns a list of all plans registered in QMS")
(def GetAllPlansDescription "Returns a list of all plans registered in QMS. New plans may be registered at run-time through QMS itself")
(def GetPlanSummary "Returns details about a single plan in QMS")
(def GetPlanDescription "Returns details about a single plan in QMS. Plan is referenced by its UUID")
(def GetResourceTypesSummary "List Resource Types")
(def GetResourceTypesDescription "Returns a list of all resource types registered in QMS")
(def GetSubscriptionSummary "Returns details about the subscription")
(def GetSubscriptionDescription "Returns details about the subscription, including quota and usage information")
(def UpdateSubscriptionQuotaSummary "Updates a quota in the user's current subscription")
(def UpdateSubscriptionQuotaDescription "Updates the user's quota for the specified resource type")
(def UpdateSubscriptionSummary "Changes the user's subscription plan to another one in QMS")
(def UpdateSubscriptionDescription "Changes the user's subscription plan to another one in QMS. New plan is referenced by name")
(def GetUserUsagesSummary "Returns a list of resource usages generated by the user")
(def GetUserUsagesDescription "Returns a list of resource usages generated by the user")
(def UpdateUsageSummary "Updates resource usage totals for a user")
(def UpdateUsageDescription "Updates resource usage totals for a user")
(def CreateSubscriptionsSummary "Bulk Subscription Creation")
(def CreateSubscriptionsDescription "Creates multiple subscriptions in one request")
(def ListSubscriptionsSummary "List Subscriptions")
(def ListSubscriptionsDescription "Lists existing subscriptions")
(def AddAddonSummary "Adds an available addon")
(def AddAddonDescription "Adds an available addon that can be applied to a user's subscription")
(def ListAddonsSummary "Lists available add-ons")
(def ListAddonsDescription "Lists the add-ons that can be applied to a user's subscription")
(def UpdateAddonSummary "Updates an add-on")
(def UpdateAddonDescription "Updates an available add-on that can be applied to a user's subscription")
(def DeleteAddonSummary "Deletes an add-on")
(def DeleteAddonDescription "Deletes an add-on that was available to be applied to a user's subscription")
(def GetSubscriptionAddonSummary "Returns a single subscription add-on")
(def GetSubscriptionAddonDescription "Returns a subscription add-on by its UUID. This has already been applied to the subscription")
(def AddSubscriptionAddonSummary "Adds an add-on to a subscription")
(def AddSubscriptionAddonDescription "Adds an add-on to a subscription, increasing the quota for the resource type in the add-on")
(def ListSubscriptionAddonsSummary "Returns the add-ons applied to a subscription")
(def ListSubscriptionAddonsDescription "Returns descriptions of all of the add-ons that have been applied to the indicated subscription")
(def UpdateSubscriptionAddonSummary "Updates a subscription add-on")
(def UpdateSubscriptionAddonDescription "Updates a subscription add-on with the values contained within")
(def DeleteSubscriptionAddonSummary "Delete a subscription add-on")
(def DeleteSubscriptionAddonDescription "Delete a subscription add-on, decreasing the quota for the resource type in the add-on by the amount provided by the add-on")

(def PlanID (describe (maybe UUID) "The UUID assigned to a plan in QMS"))
(def PlanName (describe String "The name of the plan"))
(def PlanQuotaDefaultId (describe (maybe UUID) "The UUIID assigned to the plan quota default"))
(def QMSUserID (describe (maybe UUID) "The user's UUID assigned by QMS"))
(def QuotaID (describe (maybe UUID) "The UUID for the quota in QMS"))
(def ResourceID (describe (maybe UUID) "The UUID assigned to a resource type record"))
(def UsageID (describe (maybe UUID) "The UUID assigned to a user's usage record for a resource"))
(def Username (describe String "A user's username"))
(def ResourceTypeName (describe String "The name of the resource type"))
(def AddonID (describe UUID "The UUID assigned to an add-on"))
(def SubscriptionID (describe UUID "The UUID assigned to a subscription"))
(def SubscriptionAddonID (describe UUID "The UUID assigned to a subscription add-on"))

(defschema SuccessResponse
  {(optional-key :result) (describe (maybe Any) "The result of the response")
   (optional-key :error)  (describe (maybe String) "The error string")
   :status                (describe String "The status of the response")})

(defschema ResourceType
  {:id         ResourceID
   :name       ResourceTypeName
   :unit       (describe String "The unit of the resource type")
   :consumable (describe Boolean "True if using the resource consumes it permanently")})

(defschema ResourceTypesResponse
  {:result (describe [ResourceType] "The list of resource types")
   :status (describe String "The status of the response")})

(defschema Usage
  {:id                              UsageID
   :usage                           (describe Double "The usage value")
   :resource_type                   ResourceType
   (optional-key :last_modified_at) (describe (maybe String) "The time the usage record was last modified")})

(defschema UsagesResponse
  {(optional-key :result) (describe (maybe [Usage]) "The list of usages")
   (optional-key :error)  (describe (maybe String) "The error message")
   :status                (describe String "The status of the response")})

(defschema AddUsage
  {:username      Username
   :resource_name (describe String "The name of the resource that was used")
   :usage_value   (describe Double "The usage value")
   :update_type   (describe String "The update type")})

(defschema QMSUser
  {(optional-key :uuid)      QMSUserID
   (optional-key :id)        QMSUserID
   (optional-key :username) (describe String "The user's username in QMS")})

(defschema PlanQuotaDefault
  {(optional-key :id)            PlanQuotaDefaultId
   (optional-key :quota_value)   (describe Double "The quota's default value")
   (optional-key :resource_type) ResourceType})

(defschema Plan
  {(optional-key :id)                  PlanID
   (optional-key :uuid)                PlanID
   (optional-key :name)                (describe String "The name of the plan in QMS")
   (optional-key :description)         (describe String "The description of the plan")
   (optional-key :plan_quota_defaults) (describe [PlanQuotaDefault] "The list of default values for the quotas")})

(defschema PlanListResponse
  {(optional-key :result) (describe (maybe [Plan]) "The list of plans")
   (optional-key :error)  (describe (maybe String) "The error message")
   :status                (describe String "The status of the response")})

(defschema PlanResponse
  {(optional-key :result) (describe (maybe Plan) "The plan")
   (optional-key :error)  (describe (maybe String) "The error message")
   :status                (describe String "The status of the response")})

(defschema Quota
  {:id                              QuotaID
   :quota                           (describe Double "The value associated with the quota")
   :resource_type                   ResourceType
   (optional-key :last_modified_at) (describe (maybe String) "The time the quota was last modified.")})

(defschema QuotaValue
  {:quota (describe Double "The resource usage limit")})

(defschema Subscription
  {(optional-key :uuid)                   (describe (maybe UUID) "The UUID assigned to a user's plan")
   (optional-key :id)                   (describe (maybe UUID) "The UUID assigned to a user's plan")
   (optional-key :effective_start_date) (describe (maybe String) "The date the user's plan takes effect")
   (optional-key :effective_end_date)   (describe (maybe String) "The date the user's plan ends")
   (optional-key :user)                 QMSUser
   (optional-key :plan)                 Plan
   (optional-key :quotas)               (describe (maybe [Quota]) "The list of quotas associated with the user's plan")
   (optional-key :usages)               (describe (maybe [Usage]) "The list of usages associated with the user's plan")
   (optional-key :paid)                 (describe (maybe Boolean) "True if the user paid for the subsciption")})

(defschema SubscriptionPlanResponse
  {(optional-key :result) (describe (maybe Subscription) "The user's plan")
   (optional-key :error)  (describe (maybe String) "The error message")
   :status                (describe String "The status of the response")})

(defschema SubscriptionResponse
  (merge
   Subscription
   {(optional-key :failure_reason)
    (describe (maybe String) "The reason for the failure if the subscription couldn't be created")

    (optional-key :new_subscription)
    (describe (maybe Boolean) "True if the subscription was created as part of this request")}))

(defschema BulkSubscriptionResponse
  {(optional-key :result) (describe (maybe [SubscriptionResponse]) "The response for each of the subscription requests")
   (optional-key :error)  (describe (maybe String) "The error message if the request could not be completed")
   :status                (describe String "The status of the request")})

(defschema SubscriptionUpdateResponse
  {(optional-key :result) (describe (maybe SubscriptionResponse) "The response for the subscription update request")
   (optional-key :error)  (describe (maybe String) "The error message if the request could not be completed")
   (optional-key :status) (describe (maybe String) "The status of the request")})

(defschema SubscriptionRequest
  {(optional-key :username)  (describe String "The username to associate with the subscription")
   (optional-key :plan_name) (describe String "The name of the plan to associate with the subscription")
   :paid                     (describe Boolean "True if the user paid for the subscription")
   (optional-key :periods)   (describe Integer "The number of subscription periods")
   (optional-key :end_date)  (describe String "The end date of the subscription.")})

(defschema SubscriptionRequests
  {(optional-key :subscriptions) (describe (maybe [SubscriptionRequest]) "The list of subscription requests")})

(defschema BulkSubscriptionParams
  {(optional-key :force)
   (describe String "True if the subscription should be created even if the user already has a higher level plan")})

;; The :periods parameter uses s/Int so that we can get automatic coercion.
(defschema AddSubscriptionParams
  {:paid                    (describe Boolean "True if the user paid for the subscription")
   (optional-key :periods)  (describe s/Int "The number of subscription periods")
   (optional-key :end-date) (describe String "The end date of the subscription")})

(defschema ServiceAccountAddSubscriptionParams
  (->optional-param AddSubscriptionParams :paid))

(defschema ListSubscriptionsParams
  (merge PagingParams
         {(optional-key :search)
          (describe String "The username substring to search for in the listing")}))

(defschema SubscriptionListing
  {:subscriptions (describe [Subscription] "The subscription listing")
   :total         (describe Integer "The total number of matching subscriptions")})

(defschema SubscriptionListingResponse
  {(optional-key :result) (describe (maybe SubscriptionListing) "The subscription listing")
   (optional-key :error)  (describe (maybe String) "The error message if the request could not be completed")
   :status                (describe String "The status of the request")})

(defschema NATSResourceType
  {(optional-key :uuid) ResourceID
   :name                ResourceTypeName
   :unit                (describe String "The unit of the resource type")
   :consumable          (describe Boolean "True if using the resource consumes it permanently")})

(defschema ResourceTypeForAddonUpdate
  {:uuid       (describe UUID "The UUID of the new resource type associated with the add-on")
   :consumable (describe Boolean "True if using the resource consumes it permanently")})

(defschema ResourceTypeForAddonDeletion
  {(optional-key :uuid)       (describe (maybe String) "The UUID of the resource type associated with the add-on being deleted. Probably blank")
   (optional-key :name)       (describe (maybe String) "The name of the resource type associated with the add-on being deleted. Probably blank")
   (optional-key :unit)       (describe (maybe String) "The unit of the resource type assciated wiht the add-on being deleted. Probably blank")
   (optional-key :consumable) (describe (maybe Boolean) "True if using the resource consumes it permanently. Probably blank")})

(defschema AddOn
  {(optional-key :uuid) (describe UUID "The UUID for the add-on")
   :name                (describe String "The name of the add-on")
   :description         (describe String "The description of the add-on")
   :default_amount      (describe Double "The amount of the resource provided by the add-on")
   :default_paid        (describe Boolean "Whether the add-on needs to be paid for")
   :resource_type       (describe NATSResourceType "The resource type the add-on provides more of")})

(defschema UpdateAddon
  {:uuid                         (describe UUID "The UUID of the add-on being updated")
  (optional-key :name)           (describe String "The new name for the add-on being updated")
  (optional-key :description)    (describe String "The new description fopr the add-on being updated")
  (optional-key :default_amount) (describe Double "The new amount provided by the add-on being updated")
  (optional-key :default_paid)   (describe Boolean "Whether the add-on needs to be paid for")
  (optional-key :resource_type)   ResourceTypeForAddonUpdate})

(defschema DeletedAddon
  {:uuid (describe UUID "The UUID of the add-on that was deleted")
  (optional-key :name)           (describe (maybe String) "The name of the deleted addon. Probably blank")
  (optional-key :description)    (describe (maybe String) "The descriiption of the deleted addon. Probably blank")
  (optional-key :default_amount) (describe (maybe Double) "The default amount of the deleted addon. Probably blank")
  (optional-key :default_paid)   (describe (maybe Boolean) "Whether the add-on needs to be paid for. Probably blank")
  (optional-key :resource_type)  (maybe ResourceTypeForAddonDeletion)})

(defschema AddonResponse
  {:addon (describe AddOn "The returned add-on")})

(defschema DeletedAddonResponse
  {:addon DeletedAddon})

(defschema AddonListResponse
  {:addons (describe [AddOn] "The returned list of add-ons")})

(defschema SubscriptionAddon
  {(optional-key :uuid) (describe UUID "The UUID for the subscribed add-on")
   :addon               (describe AddOn "The add-on applied to the subscription")
   :subscription        (describe Subscription "The subscription the add-on was applied to")
   :amount              (describe Double "The amount of the resource type provided by the add-on that was actually applied to the subscription")
   :paid                (describe Boolean "Whether the add-on needs/needed to be paid for")})

(defschema AddonIDBody
  {:uuid AddonID})

(defschema SubscriptionAddonResponse
  {:subscription_addon (describe SubscriptionAddon "The returned subscription add-on")})

(defschema UpdateSubscriptionAddon
  {(optional-key :amount) (describe Double "The new amount of the resource provided by the subscription add-on")
   (optional-key :paid)   (describe Boolean "Sets whether the subscription add-on needs to be charged for")})

(defschema SubscriptionAddonListResponse
  {:subscription_addons (describe [SubscriptionAddon] "The returned list of subscription add-ons")})
