# lda-server

A LDA server

LDA is a topic modeling techniques invented in 2003, many related packages are supported in many programming languages, for example Gensim in Python and others. But there are few pretrained model and services available. There are several reasons for the unavailability:

* LDA is unsupervised, the topic assignments is different corpus by corpus
* the model file is relative large

We tried our best to provide a standard non-biased corpus, we follow the classification tree(dag) in English Wikipedia to crawle articles category by category from upper to lower in the tree. In this way, it is balanced.


interface
---------

topics/:text

How to run
------------

```bash
docker run -m=9g mountain/lda-server:latest
```

