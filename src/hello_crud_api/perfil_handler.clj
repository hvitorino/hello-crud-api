(ns hello-crud-api.perfil-handler
  (:require
    [schema.core :as s]
    [hello-crud-api.models.perfil :refer [Perfil]]
    [hello-crud-api.routes.crud-generator :refer [new-crud]]))

(s/defschema PerfilRequestSchema
  {:nome s/Str})

(def perfil-routes
  (new-crud "perfis" PerfilRequestSchema Perfil))

