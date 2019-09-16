package com.test.AtomicityUsingMysql

import sys.process._
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import org.apache.spark.sql.{DataFrame, SparkSession}

object AtomicityUpdateFromSpark {

  val basePath="/home/raptor/Softwares/study/KafkaSave/"

  def fileCheckAndDfCreation(topicName: String,partitionMap:collection.mutable.Map[Int,collection.mutable.Map[String,String]],spark:SparkSession,DFtoSave:DataFrame)={

    var filePath:String=basePath+topicName+"/"

    for (i <- 1 to  (partitionMap.keys.size))
      {
        val tempMap=partitionMap(i)
        val KeyS=tempMap.keys
        for (k <- KeyS)
        filePath=filePath+"/"+k+"="+tempMap(k)
      }

    val filePresent=filePath!

    filePresent match {
      case 0 => DFtoSave.write.mode("overwrite").option("header","true").option("delimiter","|").parquet(filePath)
      case _ => DFtoSave.write.mode("append").option("header","true").option("delimiter","|").parquet(filePath)
    }
  }

  def saveAndCommitSpark(Kafka_consumer:KafkaConsumer[String,String],Consumer_record:ConsumerRecord[String,String],spark:SparkSession,offsetDF:DataFrame,dataDF:DataFrame,topicName:String,partitionMap:collection.mutable.Map[Int,collection.mutable.Map[String,String]]):Unit =
  {
    try
    {

      dataDF.count match
      {
        case value if value >= 100 => fileCheckAndDfCreation(topicName,partitionMap,spark,dataDF)
        case _ => dataDF.union(spark.createDataFrame(Seq((Consumer_record.key,Consumer_record.value,Consumer_record.partition()))).toDF("Messagekey","Messagevalue","partition_id"))
      }

    }
  }

}
