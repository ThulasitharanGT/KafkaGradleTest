package com.test
import java.util._
import java.io._

import org.apache.kafka.clients.producer._

import scala.collection.JavaConverters._
import org.apache.kafka.common.Cluster
import org.apache.kafka.common.record.InvalidRecordException
import org.apache.kafka.common.utils.Utils

object SensorProducerPropertiesFile {

  // The objective here is to have 10 partitions in a topic.
  // imagine many sensors are sending data to this topic and you want to dedicate 3 partitions to a particular sensor

    def main(args: Array[String]): Unit = {
      val T_Name = "CarSensor"
      val props = new Properties()
      var propertiesStream:InputStream=null

      try {
        val userDir=System.getProperty("user.dir")
        propertiesStream=new FileInputStream(userDir+"/src/main/scala/com/properties/sensorProducerTest.properties")
        props.load(propertiesStream)
        val Kafka_Producer = new KafkaProducer[String, String](props)
        for (i <- 1 to 10) {
          //  println(i)
          Kafka_Producer.send(new ProducerRecord[String, String](T_Name, "SSP" + i, "500" + i))
          //  println(i)

        }
        for (i <- 1 to 10) {
          Kafka_Producer.send(new ProducerRecord[String, String](T_Name, "TSS", "500" + i))
        }

        Kafka_Producer.close()
      }
      println("Sensor Producer Completed")

    }


}
