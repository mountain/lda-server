(defproject lda-server "0.1.0"
  :description "A LDA server"
  :aot :all
  :main lda.app

  :resource-paths ["conf" "resources"]
  :filespecs [{:type :path :path "conf/punc.txt"}
              {:type :path :path "conf/stops.txt"}
              {:type :path :path "conf/va.txt"}
              {:type :path :path "conf/log4j.properties"}]
  :auto-clean false

  :source-paths ["src"]
  :java-source-paths ["java"]

  :repositories [["bintray" "https://jcenter.bintray.com/"]]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.slf4j/slf4j-api "1.7.30"]
                 [org.slf4j/slf4j-log4j12 "1.7.30"]
                 [org.apache.logging.log4j/log4j-core "2.14.0"]
                 [org.javatuples/javatuples "1.2"]
                 [cc.mallet/mallet "2.0.7"]
                 [clj-time "0.15.2"]
                 [http-kit "2.5.0"]
                 [compojure "1.6.2"]
                 [ring/ring-json "0.5.0"]
                 [ring/ring-codec "1.1.2"]]

  :uberjar-name "lda-server-standalone.jar"

  :javac-options ["--release" "11"]
  :jvm-opts ["-Xmx3g"])
