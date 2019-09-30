(ns hello-crud-api.core
  (:require [toucan.db :as db]
            [toucan.models :as models]
            [ring.adapter.jetty :refer [run-jetty]]
            [compojure.api.sweet :refer [api routes]]
            [hello-crud-api.perfil-handler :refer [perfil-routes]])
  (:gen-class))

(def db-spec
  {:dbtype "postgres"
   :dbname "hellocrud"
   :user "postgres"})

(def swagger-config
  {:ui "/swagger"
   :spec "/swagger.json"
   :options {:ui {:validatorUrl nil}
             :data {:info {:version "1.0.0", :title "Restful CRUD API"}}}})

(def app (api {:swagger swagger-config} (apply routes perfil-routes)))

(defn -main
  [& args]
  (db/set-default-db-connection! db-spec)
  (models/set-root-namespace! 'hello-crud-api.models)
  (run-jetty app {:port 3001}))
