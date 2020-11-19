(ns lda.app
  (:require [lda.inferer :as inferer]
            [clj-time.core :as t]
            [clj-time.local :as l]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [ring.util.codec :refer [url-decode]]
            [clojure.java.io :as io])
  (:use [compojure.route :only [files not-found]]
        [compojure.core :only [defroutes GET POST DELETE ANY context]]
        org.httpkit.server)
  (:gen-class :main true))

(defroutes all-routes
  (GET "/" [] (wrap-json-response (fn [request] (response {:status "ok" :datetime (l/format-local-time (l/local-now) :basic-date-time-no-ms)}))))
  (GET "/topics/:text" [text] (wrap-json-response (fn [request] (response {:status "ok" :topics (inferer/infer (url-decode text))}))))
  (POST "/topicsOf" {:keys [headers params body] :as request} (wrap-json-response (fn [request] (response {:status "ok" :topics (inferer/infer (slurp (io/reader body)))}))))
  (not-found (wrap-json-response (fn [request] (response {:status "failure" :cause "not-found"})))))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main [& args]
  (org.apache.log4j.BasicConfigurator/configure)
  (reset! inferer/lda (inferer/deserialize-obj inferer/modelfile))
  (reset! inferer/pipes (inferer/deserialize-obj inferer/pipesfile))
  (let [cnt (count args)]
    (if (== cnt 0)
      (println "welcome, please issue a subcommand: start, stop, show, infer")
      (case (first args)
        "start" (do (println "start...") (reset! server (run-server #'all-routes {:port 7777})))
        "stop" (stop-server)
        "show" (inferer/show)
        "infer" (println (inferer/infer (second args)))))))
