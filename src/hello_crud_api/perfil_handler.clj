(ns hello-crud-api.perfil-handler
  (:require
    [schema.core :as s]
    [compojure.api.sweet :refer [POST PUT GET DELETE]]
    [hello-crud-api.models.perfil :refer [Perfil]]
    [toucan.db :as db]
    [ring.util.http-response :refer [created ok not-found]]))

; Esquema

(s/defschema PerfilRequestSchema
  {:nome s/Str})

; Rota
(defn id->created [id]
  (created (str "/perfis/" id) {:id id}))

(defn perfil->response [perfil]
  (if perfil
    (ok perfil)
    (not-found)))

(defn create-perfil-handler [novo-perfil]
  (->> novo-perfil
       (db/insert! Perfil)
       :id
       id->created))

(defn read-perfil-handler [id]
  (->> id
       (Perfil)
       (perfil->response)))

(defn list-perfil-handler []
  (->> (db/select Perfil)
       (ok)))

(defn update-perfil-handler [id update-perfil-req]
  (db/update! Perfil id update-perfil-req)
  (ok))

(defn delete-perfil-handler [id]
  (db/delete! Perfil :id id)
  (ok))

(def perfil-routes
  [(POST "/perfis" []
     :body [create-perfil-req PerfilRequestSchema]
     (create-perfil-handler create-perfil-req))

   (GET "/perfis" []
     (list-perfil-handler))

   (GET "/perfis/:id" []
     :path-params [id :- s/Int]
     (read-perfil-handler id))

   (PUT "/perfis/:id" []
        :body [update-perfil-req PerfilRequestSchema]
        :path-params [id :- s/Int]
        (update-perfil-handler id update-perfil-req))

   (DELETE "/perfis/:id" []
     :path-params [id :- s/Int]
     (delete-perfil-handler id))])