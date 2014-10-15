package com.tonybeltramelli.desktop

import scala.io.Source
import com.tonybeltramelli.desktop.core.QueryProcessor
import com.tonybeltramelli.desktop.util.Helper
import ch.ethz.dal.tinyir.io.TipsterStream
import ch.ethz.dal.tinyir.processing.Tokenizer
import com.tonybeltramelli.desktop.core.scoring.AScoring
import com.tonybeltramelli.desktop.core.scoring.TermBasedScoring
import com.tonybeltramelli.desktop.core.scoring.LanguageBasedScoring
import com.github.aztek.porterstemmer.PorterStemmer

object Main {
	def main(args: Array[String])
	{
	  new Main(args(0).toBoolean, args.slice(1, args.length).toList)
	}
}

class Main
{	
	def this(useLanguageModel: Boolean, queries: List[String])
	{
	  this
	  
	  var qu = queries
	  var topics : List[(String, Int)] = null
	  
	  if(queries.length == 0)
	  {
		  topics = _getTopics
		  qu = topics.map(_._1)
	  }
	  
	  val time = System.nanoTime()
	  
	  val tipster = new TipsterStream(Helper.ZIP_PATH)	  
	  val queriesTokens = qu.map(q => Tokenizer.tokenize(q)).map(t => _stemTokens(t)).zipWithIndex
	  
	  println("time 0 : " + (System.nanoTime() - time) / 1000000000.0 + " seconds")
	  
	  val documents = tipster.stream.take(1000)
	  val collection = documents.map(doc => (doc.name, _stemTokens(doc.tokens)))
	  
	  var qp : QueryProcessor = null
	  
	  val scoringModel: AScoring = if(!useLanguageModel) new TermBasedScoring() else new LanguageBasedScoring()
	  
	  for(query <- queriesTokens)
	  {
	    qp = null
	    qp = new QueryProcessor(query, collection, topics, scoringModel)
	  }
	  
	  println("time 1 : " + (System.nanoTime() - time) / 1000000000.0 + " seconds")
	}
	
	private def _getTopics : List[(String, Int)] =
	{
	  val lines = Source.fromFile(Helper.TOPIC_PATH).getLines
	  val topicsTitle = lines.filter(l => l.contains("<title>")).map(l => l.split(":")(1).trim.toLowerCase)
	  val topicsNumber = lines.filter(l => l.contains("<num>")).map(l => l.split(":")(1).trim.toInt)
	  
	  topicsTitle.zip(topicsNumber).toList
	}
	
	private def _stemTokens(list: List[String]) : List[String] = 
	{
	  list.map(t => t.toLowerCase()).map(PorterStemmer.stem(_))
	}
}