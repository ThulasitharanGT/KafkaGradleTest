package com.test.AtomicityUsingMysql

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object SensorProducer {
  def main(args: Array[String]): Unit = {
    val T_Name = "CarSensor"
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("partitioner.class", "com.test.AtomicityUsingMysql.SensorPartitioner")
    props.put("stability.sensor.name", "SCS")
    props.put("tyre.sensor.name", "Tyre")
    val Kafka_Producer = new KafkaProducer[String, String](props)
    for (i <- 1 to 100) {
      Kafka_Producer.send(new ProducerRecord[String, String](T_Name, "TCS" + i, "500" + i))
    }
    for (i <- 1 to 100) {
      Kafka_Producer.send(new ProducerRecord[String, String](T_Name, "SCS", "500" + i))
    }
    for (i <- 1 to 100) {
      Kafka_Producer.send(new ProducerRecord[String, String](T_Name, "Tyre", "500" + i))
    }
    Kafka_Producer.close()
    println("Sensor Producer Completed")

  }
}
