package com.tonybeltramelli.desktop.core.classifier

import scala.collection.mutable.{Map => MutMap}
import scala.collection.mutable.ListBuffer
import scala.util.Random

class LogisticRegression extends AClassifier
{
  private val _classifiers: MutMap[String, BinaryLinearClassifier] = MutMap() //class name -> binary classifier
  private val _THRESHOLD = 0.5
  
  override def train(topic: String)
  {
    val bc = new BinaryLinearClassifier

    for(docIndex <- _getRandomDocuments(topic).par)
    {
      val doc = _documents(docIndex)
      val isRelated = doc._3.contains(topic)
      
      bc.train(doc._1.map(f => f._1 -> _inverseFreq(f._1)), isRelated)
    }
   
    _classifiers += topic -> bc
  }
  
  override def apply(tokens: List[String]) =
  {
    val documentFeatures = _getTermFreq(tokens).map(f => f._1 -> (f._2.toDouble + _inverseFreq.getOrElse(f._1, 0.0)))
      
    val results = _classifiers.map(bc => (bc._1, bc._2.getProb(documentFeatures))).filter(_._2 >= _THRESHOLD).toSeq.sortWith(_._2 > _._2)
    
    results.map(_._1).toSet
  }
  
  private def _getRandomDocuments(trueTopic: String) =
  {
    val random = new Random
    var documents = _classesToDoc(trueTopic)
    
    var i = documents.size * 3
    i = if(i > _documentCounter) _documentCounter else i
    
    while(i > 0)
    {
      documents = documents + random.nextInt(_documents.size)
      i -= 1
    }
    
    documents
  }
}