package com.test

import java.util._
import java.io._
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.serialization.Deserializer
import scala.collection.JavaConverters._
import scala.concurrent.duration._


object ConsumerExamplePropertiesFile {




    def main(args: Array[String]): Unit = {
      val T_Name = "CarSensor"
      val T_Group_Name = "CarSensorGroup"
      val props = new Properties()

      var propertiesStream: InputStream = null
      try {
        val userDir=System.getProperty("user.dir")
        //print(userDir)
        propertiesStream=  new FileInputStream(userDir+"/src/main/scala/com/properties/consumerExampleTest.properties")
        props.load(propertiesStream)
        val Kafka_Consumer = new KafkaConsumer[String, String](props)
        Kafka_Consumer.subscribe(Arrays.asList(T_Name))
        while (true) {
          val Consumer_Record = Kafka_Consumer.poll(100) //ConsumerRecords Object
          for (i <- Consumer_Record.asScala) {
            println("Supplier id = " + String.valueOf(i.value()) + " Supplier name = " + i.key())
          }
        }

      }
    }

}
