package com.test

import java.util._
import org.apache.kafka.clients.producer._

import scala.collection.JavaConverters._
import org.apache.kafka.common.Cluster
import org.apache.kafka.common.record.InvalidRecordException
import org.apache.kafka.common.utils.Utils

// The objective here is to have 10 partitions in a topic.
// imagine many sensors are sending data to this topic and you want to dedicate 3 partitions to a particular sensor

object SensorProducer {
  def main(args: Array[String]): Unit = {
    val T_Name = "CarSensor"
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("partitioner.class", "com.test.SensorPartitioner")
    props.put("speed.sensor.name", "TSS")
    val Kafka_Producer = new KafkaProducer[String, String](props)
    for (i <- 1 to 100) {
      //  println(i)
      Kafka_Producer.send(new ProducerRecord[String, String](T_Name, "SSP" + i, "500" + i))
      //  println(i)

    }
    for (i <- 1 to 100) {
      Kafka_Producer.send(new ProducerRecord[String, String](T_Name, "TSS", "500" + i))
    }
    Kafka_Producer.close()
    println("Sensor Producer Completed")

  }
}


