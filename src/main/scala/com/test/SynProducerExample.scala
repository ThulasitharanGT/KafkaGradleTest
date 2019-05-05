package com.test

import java.util._

import org.apache.kafka.clients.producer._

object SynProducerExample {

  def main (args: Array[String]) :Unit = {

    val Tname = "TopicTest"
    val T_Key = "key-1"
    val T_Value = "Value"
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val Kafka_Producer = new KafkaProducer[String,String](props)
    val P_Record = new ProducerRecord[String,String](Tname, T_Key, T_Value)
    try {
      val m_data = Kafka_Producer.send(P_Record).get()
      println("Message is sent to Partition Number :" + m_data.partition() + " and of offset " + m_data.offset())
      println("Message sent with synchronous connection ")
    }
    catch
    {
      case  e:Exception => {
        e.printStackTrace()
        println("synchronous connection Failed ")
      }

    }
    finally
      {
        Kafka_Producer.close()
      }


    }

}
