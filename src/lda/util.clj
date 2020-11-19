(ns lda.util
  (:use [clojure.java.io]))

(def topic-num 768)

(defn -for-stoplist [f]
  (with-open [rdr (reader (resource "stops.txt"))]
    (doseq [x (line-seq rdr)] (f x)))
  (with-open [rdr (reader (resource "punc.txt"))]
    (doseq [x (line-seq rdr)] (f x)))
  (with-open [rdr (reader (resource "va.txt"))]
    (doseq [x (line-seq rdr)] (f x))))

(def stoplist
  (let [stopmap (atom #{})]
    (-for-stoplist (fn [x] (swap! stopmap conj x)))
    @stopmap))

(defn make-pipes []
  (cc.mallet.pipe.SerialPipes. (doto (java.util.ArrayList.)
    (.add (lancelot.FilterPipe. (java.util.HashSet. stoplist)))
    (.add (cc.mallet.pipe.Target2Label.))
    (.add (cc.mallet.pipe.CharSequence2TokenSequence.
      (cc.mallet.util.CharSequenceLexer. (java.util.regex.Pattern/compile "[^\\s]+"))))
    (.add (cc.mallet.pipe.TokenSequence2FeatureSequence.)))))

(defn make-instances [pipes]
  (cc.mallet.types.InstanceList. pipes))

