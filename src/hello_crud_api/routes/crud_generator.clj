(ns hello-crud-api.routes.crud-generator
  (:require
    [schema.core :as s]
    [compojure.api.sweet :refer [POST PUT GET DELETE]]
    [toucan.db :as db]
    [ring.util.http-response :refer [created ok not-found]]))

(defn id->created [route-name id]
  (created (str "/" route-name "/" id) {:id id}))

(defn create-model-route [route-name]
  (str "/" route-name))

(defn update-model-route [route-name]
  (str "/" route-name "/:id"))

(defn list-model-route [route-name]
  (create-model-route route-name))

(defn read-model-route [route-name]
  (update-model-route route-name))

(defn delete-model-route [route-name]
  (update-model-route route-name))

(defn create-model-handler [route-name toucan-model novo-model]
  (->> novo-model
       (db/insert! toucan-model)
       :id
       (id->created route-name)))

(defn update-model-handler [toucan-model id update-model-request]
  (db/update! toucan-model id update-model-request)
  (ok))

(defn list-model-handler [toucan-model]
  (->> (db/select toucan-model)
       (ok)))

(defn read-response [model]
  (if model
    (ok model)
    (not-found)))

(defn read-model-handler [toucan-model id]
  (-> (toucan-model id)
      (read-response)))

(defn delete-model-handler [toucan-model id]
  (db/delete! toucan-model :id id)
  (ok))

(defn new-crud [name body-schema toucan-model]
  [(POST (create-model-route name) []
     :body [create-model-request body-schema]
     (create-model-handler name toucan-model create-model-request))

   (PUT (update-model-route name) []
     :body [update-model-request body-schema]
     :path-params [id :- s/Int]
     (update-model-handler toucan-model id update-model-request))

   (GET (list-model-route name) []
     (list-model-handler toucan-model))

   (GET (read-model-route name) []
     :path-params [id :- s/Int]
     (read-model-handler toucan-model id))

   (DELETE (delete-model-route name) []
     :path-params [id :- s/Int]
     (delete-model-handler toucan-model id))])