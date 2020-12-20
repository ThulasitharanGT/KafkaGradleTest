package com.test


import java.util.{Properties,Arrays}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.header.internals.RecordHeader
import java.util.concurrent.ThreadLocalRandom

object simpleProducerWithHeader {
  def main (args: Array[String]) :Unit = {
    val tName = "TopicTest"
    val tKey = "key-1"
    val tValue = "Value"
    val headerKey="header_key"
    val headerVal="header_value"
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val Kafka_Producer = new KafkaProducer[String,String](props)
 //   val headers = Arrays.asList(new ReordHeader(headerKey, headerVal.getBytes()))
    val pRecord = new ProducerRecord[String,String](tName, null, tKey, tValue)
    /*
    val streamOneFilterSet="fp1,fp2,fp3"
    val streamTwoFilterSet="q1,q2,q3"

    val pRecord = new ProducerRecord[String,String](tName, null, tKey, tValue)
    pRecord.headers().add(new RecordHeader("fp1", "freePractice1".getBytes()))
    Kafka_Producer.send(pRecord)

    val pRecord = new ProducerRecord[String,String](tName, null, tKey, tValue)
    pRecord.headers().add(new RecordHeader("q1", "quali1".getBytes()))
    Kafka_Producer.send(pRecord)

    val pRecord = new ProducerRecord[String,String](tName, null, tKey, tValue)
    pRecord.headers().add(new RecordHeader("fp2", "freePractice2".getBytes()))
    Kafka_Producer.send(pRecord)

    val pRecord = new ProducerRecord[String,String](tName, null, tKey, tValue)
    pRecord.headers().add(new RecordHeader("fp3", "freePractice3".getBytes()))
    Kafka_Producer.send(pRecord)

    val pRecord = new ProducerRecord[String,String](tName, null, tKey, tValue)
    pRecord.headers().add(new RecordHeader("q2", "quali2".getBytes()))
    Kafka_Producer.send(pRecord)

    val pRecord = new ProducerRecord[String,String](tName, null, tKey, tValue)
    pRecord.headers().add(new RecordHeader("q3", "quali3".getBytes()))
    Kafka_Producer.send(pRecord)

     */
    pRecord.headers().add(new RecordHeader(headerKey, headerVal.getBytes()))
    pRecord.headers().add(new RecordHeader(s"${headerKey}2",s"${headerVal}2".getBytes()))
    Kafka_Producer.send(pRecord)
/*    for (i <- 1 to 10 )
      Kafka_Producer.send(randomProducerRecordGenerator(tName,4,5,i))
      */
    Kafka_Producer.close()
    println ("Final Line")

  }
  val stringList= ('a' to 'z') ++ ('A' to 'Z')
  //val numberLongList:Seq[Long]= 1L to 999999999999999999L // wont work, The Integer size being passed to unapply/(retrieve value from) the seq may cause issue because we have more index than an integer's max value
  val numberIntList= 1 to 999999999

  def randomStringGenerator(size:Int)={
    var outputString=""
    for (i <- 1 to size)
        outputString=outputString+stringList(ThreadLocalRandom.current().nextInt(1,stringList.size))
    outputString
  }
  def randomIntegerGenerator=numberIntList(ThreadLocalRandom.current().nextInt(1,numberIntList.size-1))
 // def randomLongGenerator={}
  //  val numberLongListTemp=numberLongList.slice(ThreadLocalRandom.current.nextLong(1,numberLongList.size -1),ThreadLocalRandom.current.nextLong(1,numberLongList.size -1))
  // The Integer size being passed to unapply/(retrieve value from) the seq may cause issue because we have more index than an integer's max value

  // 0 adds number only to key, 1 adds number to key and value, 2 adds key only to value
  def randomProducerRecordGenerator(topicName:String,keySize:Int,valueStringSize:Int,wantInteger:Int)=  new ProducerRecord[String,String](topicName,null,wantInteger match {case value if (value == 0 || value ==1) => s"${randomStringGenerator(keySize-1)}${randomIntegerGenerator}" case _ =>randomStringGenerator(keySize)},wantInteger match {case value if (value == 1 || value == 2) => s"${randomStringGenerator(valueStringSize-1)}${randomIntegerGenerator}" case _ =>randomStringGenerator(valueStringSize)})

}
