(ns hello-crud-api.string-validation
  (:require [clojure.string :as str]))

(def non-blank? (complement str/blank?))

(defn max-length? [length text]
  (<= (count text) length))