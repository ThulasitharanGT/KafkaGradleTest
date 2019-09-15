package com.test.RebalanceListener

import java.util.{Arrays, Properties}

import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConverters._

object ConsumerCommitRebalanceListenerVersion {
  def main (args: Array[String]) :Unit = {

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
      val rebalanceListener=new RebalanceListener()
      rebalanceListener.apply(Kafka_Consumer)
      Kafka_Consumer.subscribe(Arrays.asList(T_Name),rebalanceListener)
      while (true)
      {
        val Consumer_Record = Kafka_Consumer.poll(100) //ConsumerRecords Object
        for (i <- Consumer_Record.asScala)
        {
          println("Supplier id = " + String.valueOf(i.value()) + " Supplier name = " + i.key())
          rebalanceListener.addOffset(i.topic(),i.partition(),i.offset())
        }
      }
    }
    finally
    {
      Kafka_Consumer.close()
    }


  }

}
