(ns list-objects
  (:require [cognitect.aws.client.api :as aws]
            [clojure.core.protocols :as p]
            [datafy])
  (:gen-class
    :methods [^:static [handler [java.util.Map, com.amazonaws.services.lambda.runtime.Context] java.util.Map]]))

(def client {:s3 (aws/client {:api :s3 :region :ap-northeast-2})})

(defn -handler [input, context]
  (let [{:keys [username prefix]} (p/datafy input)]
    (let [response (aws/invoke
                     (:s3 client)
                     {:op      :ListObjectsV2
                      :request {:Bucket (System/getenv "AWS_S3_BUCKET_NAME")
                                :Prefix (format "users/%s/%s" username prefix)
                                :Delimiter "/"}})]
      {:Folders (or (:CommonPrefixes response) [])
       :Images (or (:Contents response) [])})))