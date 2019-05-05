package com.test

import java.util._

import scala.collection.JavaConverters._
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.TopicPartition

object ConsumerAtomicUpdateUsingMysqlDb {
  def main (args: Array[String]) :Unit = {
    var Record_count:Int =0
    val T_Name = "CarSensor"
    val T_Group_Name = "CarSensorGroup"
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
    props.put("group.id",T_Group_Name)
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("enable.auto.commit", "false")
    var Kafka_Consumer : KafkaConsumer[String, String]= null
    try {
      Kafka_Consumer = new KafkaConsumer[String, String](props)
      val Topic_Partition_0= new TopicPartition(T_Name,0)
      val Topic_Partition_1= new TopicPartition(T_Name,1)
      val Topic_Partition_2= new TopicPartition(T_Name,2)
      Kafka_Consumer.assign(Arrays.asList(Topic_Partition_0,Topic_Partition_1,Topic_Partition_2))
      println("Current Position of partition 0 = "+Kafka_Consumer.position(Topic_Partition_0))
      println("Current Position of partition 1 = "+Kafka_Consumer.position(Topic_Partition_1))
      println("Current Position of partition 2 = "+Kafka_Consumer.position(Topic_Partition_2))
      Kafka_Consumer.seek(Topic_Partition_0,AtomicityUpdateFromMysqlDb.getOffsetFromDB(Topic_Partition_0).toLong)
      Kafka_Consumer.seek(Topic_Partition_1,AtomicityUpdateFromMysqlDb.getOffsetFromDB(Topic_Partition_1).toLong)
      Kafka_Consumer.seek(Topic_Partition_2,AtomicityUpdateFromMysqlDb.getOffsetFromDB(Topic_Partition_2).toLong)
      println("Current Position of partition 0 = "+Kafka_Consumer.position(Topic_Partition_0))
      println("Current Position of partition 1 = "+Kafka_Consumer.position(Topic_Partition_1))
      println("Current Position of partition 2 = "+Kafka_Consumer.position(Topic_Partition_2))

     do {
        val Consumer_Record = Kafka_Consumer.poll(100) //ConsumerRecords Object
        println("Recods Polled : "+Consumer_Record.count())
        Record_count=Consumer_Record.count()
        for (i <- Consumer_Record.asScala)
        {
          println("Supplier id = " + String.valueOf(i.value()) + " Supplier name = " + i.key())
          AtomicityUpdateFromMysqlDb.saveAndCommit(Kafka_Consumer,i)
        }
      }while (Record_count>0)
    }
    finally
    {
      Kafka_Consumer.close()
    }


  }
}
