
- Parses a document collection in a single-pass streaming fashion,
- Handles multiple queries simultaneously,
- Allow choice of different language models (at least one term-based model and one language model),
- For each query, it should output the top n results and calculate per-query (e.g., AP) and global quality metrics (e.g., MAP),
- To measure the quality of your system's output, we make available a set of 50 topics (#51-100). For the first 40 of those, you have a set of human relevance judgements (qrels). The remaining 10 topics will be used to finally gauge the quality of your system on unseen queries. 



https://github.com/mcartright/julien/blob/master/src/test/scala/julien/retrieval/JelinekMercerSpec.scala
https://lucene.apache.org/core/4_0_0/core/org/apache/lucene/search/similarities/LMJelinekMercerSimilarity.html
https://searchcode.com/codesearch/view/25295694/
http://mkaz.com/2011/06/28/how-to-use-scala-and-lucene-to-create-a-basic-search-application/


TinyIR.jar :

ch/ethz/dal/tinyir/
            processing/
                StringDocument.scala
                Document.scala
                Tokenizer.scala
                XMLDocument.scala
                SaxParsing.scala
                StopWords.scala
                TipsterParse.scala
            io/
                ZipDirStream.scala
                DirStream.scala
                ParsedXMLStream.scala
                TipsterStream.scala
                ZipStream.scala
                DocStream.scala
            lectures/
                PrecisionRecall.scala
                TipsterGroundTruth.scala
            alerts/
                AlertsTipster.scala
                Alerts.scala
                Query.scala
            util/
                StopWatch.scala
            indexing/
                FreqIndex.scala
                SimpleIndex.scala
                InvertedIndex.scala

com/github/aztek/porterstemmer/PorterStemmer.scala