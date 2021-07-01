(ns lda.inferer
  (:use [lda.util]))

(def logger (org.slf4j.LoggerFactory/getLogger (.getClass java.lang.Class)))

(def pipesfile "pipes.pip")
(def modelfile "lda.mdl")

(defn deserialize-obj [filename]
  (with-open [inp (-> (java.io.File. filename) java.io.FileInputStream. java.util.zip.GZIPInputStream. java.io.ObjectInputStream.)]
    (.readObject inp)))

(defonce pipes (atom nil))

(defonce lda (atom nil))

(defn show []
  (.printTopWords @lda java.lang.System/out 256 true))

(defn infer [text]
  (.info logger (subs (.toString text) 0 (min 20 (.length text))))
  (let [inst (cc.mallet.types.Instance. text text ".txt" ".txt")
        iter (.iterator (seq [inst]))
        processed (.newIteratorFrom @pipes iter)
        piped (.next processed)
        result (seq (.getSampledDistribution (.getInferencer @lda) piped 20 1 5))]
      result))

