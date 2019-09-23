(ns hello-crud-api.perfil-handler
  (:require
    [schema.core :as s]
    [hello-crud-api.string-validation :as validation]
    [compojure.api.sweet :refer [POST]]
    [hello-crud-api.models.perfil :refer [Perfil]]
    [toucan.db :as db]
    [ring.util.http-response :refer [created ok]]))

; Esquema

(s/defschema PerfilRequestSchema
             {:nome s/Str})

; Rota
(defn id->created [id]
  (created (str "/perfis/" id) {:id id}))

(defn create-perfil-handler [novo-perfil]
  (->> novo-perfil
       (db/insert! Perfil)
       :id
       id->created))

(def perfil-routes
  [(POST "/perfis" []
         :body [create-perfil-req PerfilRequestSchema]
         (create-perfil-handler create-perfil-req))])