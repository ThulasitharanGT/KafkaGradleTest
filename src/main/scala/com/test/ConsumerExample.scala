package com.test

import java.util._

import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.serialization.Deserializer
import scala.collection.JavaConverters._
import scala.concurrent.duration._

object ConsumerExample {

  def main(args: Array[String]): Unit = {
    val T_Name = "CarSensor"
    val T_Group_Name = "CarSensorGroup"
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
    props.put("group.id",T_Group_Name)
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    val Kafka_Consumer=new KafkaConsumer[String,String](props)
    Kafka_Consumer.subscribe(Arrays.asList(T_Name))
    while(true)
      {
        val Consumer_Record=Kafka_Consumer.poll(100) //ConsumerRecords Object
        for( i <- Consumer_Record.asScala)
          {
            println("Supplier id = "+String.valueOf(i.value())+ " Supplier name = " +i.key())
          }
      }


  }

}
