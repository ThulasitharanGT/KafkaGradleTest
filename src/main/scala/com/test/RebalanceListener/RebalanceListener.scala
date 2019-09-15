package com.test.RebalanceListener

import java.util._

import org.apache.kafka.clients.consumer._
import org.apache.kafka.common._

import scala.collection.JavaConverters._

class RebalanceListener extends ConsumerRebalanceListener{

  var Kafka_Consumer:KafkaConsumer[String,String]=null
  var currentOffsetsMap:HashMap[TopicPartition,OffsetAndMetadata]=null

  def apply(Kafka_Consumer:KafkaConsumer[String,String]) :Unit=
  {
  this.Kafka_Consumer=Kafka_Consumer
  }

  def addOffset(topic:String,partition:Int,offset:Long):Unit=
  {
   currentOffsetsMap.put(new TopicPartition(topic,partition),new OffsetAndMetadata(offset,"Commit"))
  }

  override def onPartitionsRevoked(partitions: Collection[TopicPartition]): Unit = {
    println("Following Partitions are Revoked ....")
    for(i <- partitions.asScala)
    {
      println(i.partition() + ",")
    }
    println("Following Partitions are Commited till now  ....")
    for(i <- currentOffsetsMap.keySet.asScala)
    {
      println(i.partition())
    }
  }

  override def onPartitionsAssigned(partitions: Collection[TopicPartition]): Unit =  {
    println("Following Partitions are Assigned ....")
    for(i <- partitions.asScala)
      {
        println(i.partition() + ",")
      }
    println("Following Partitions are Commited till now ....")
    for(i <- currentOffsetsMap.keySet.asScala)
    {
      println(i.partition())
    }
    Kafka_Consumer.commitSync(currentOffsetsMap)
    currentOffsetsMap.clear()
  }
}
