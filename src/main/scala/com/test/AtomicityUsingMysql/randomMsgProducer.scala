package com.test.AtomicityUsingMysql

import java.util.Properties
import java.util.concurrent.ThreadLocalRandom

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}


object randomMsgProducer {
  val listOfChars = ('a' to 'z') ++ ('A' to 'Z')

  def randomRecordGenerator(topicName: String, kafkaProducer: KafkaProducer[String, String], key: String, valueCharLength: Int,partitionValue:String): Unit = kafkaProducer.send(new ProducerRecord[String, String](topicName, key, randomStringGenerator(valueCharLength)+"~"+partitionValue))
  def randomRecordGenerator(topicName: String, kafkaProducer: KafkaProducer[String, String], key: String, valueCharLength: Int,partitionValue:String,raceTrack:String,runType:String): Unit = kafkaProducer.send(new ProducerRecord[String, String](topicName, key, randomStringGenerator(valueCharLength)+"~"+partitionValue+"~"+raceTrack+"~"+runType))
  def randomRecordGenerator(topicName: String, kafkaProducer: KafkaProducer[String, String], key: String, valueCharLength: Int): Unit = kafkaProducer.send(new ProducerRecord[String, String](topicName, key, randomStringGenerator(valueCharLength)))

  def randomStringGenerator(tempStringLength: Int) = {
    var tempString: String = null
    for (i <- 1 to tempStringLength)
      if (tempString == null)
        tempString = listOfChars(ThreadLocalRandom.current().nextInt(0, listOfChars.size)).toString
      else
        tempString = tempString + listOfChars(ThreadLocalRandom.current().nextInt(0, listOfChars.size)).toString
    tempString
  }

  def main(args: Array[String]): Unit = {
    val inputMap:collection.mutable.Map[String,String]=collection.mutable.Map[String,String] ()
    for (arg <- args)
      {
        val keyPart=arg.split("=",2)(0)
        val valPart=arg.split("=",2)(1)
        inputMap.put(keyPart,valPart)
      }
    val key=inputMap("key") // Tyre(3,4,5,6) or SCS (0,1,2) or any (7,8,9) -- total 10 partitions
    val messageLength=inputMap("messageLength").toInt
    val numOfRecords=inputMap("numOfRecords").toInt
  //  val topicName ="CarSensor"
    val topicName =inputMap("topicName")
    val keySerializer=inputMap("keySerializer")
    val valueSerializer=inputMap("valueSerializer")
    val bootStrapServer=inputMap("bootStrapServer")
    import scala.util.Try
    var partitionValue=""
    Try{inputMap("partitionValue")}.isSuccess match {case true => partitionValue=inputMap("partitionValue") case _ => {println("partition value not passed");partitionValue="0" }}
   // import scala.util.Success
    var raceTrack=""
    Try{inputMap("raceTrack")}.isFailure match {case true =>  {println("raceTrack value not passed");raceTrack="0" } case _ => raceTrack=inputMap("raceTrack")}
    //Success(inputMap("raceTrack")) match {case Success(mapVal) => raceTrack=inputMap("raceTrack")  case _ =>{ println("raceTrack value not passed");raceTrack="0"} }
   // import scala.util.Failure
    var runType=""
    Try{inputMap("runType")}.isSuccess match {case  true =>runType=inputMap("runType")   case _ => {println("runType value not passed");runType="0" } }

    // Failure(inputMap("raceTrack")) match {case Failure(mapVal) => raceTrack=inputMap("raceTrack")  case _ =>{ println("raceTrack value not passed");raceTrack="0"} }

    val props = new Properties()
    //props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
    //props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    // props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    props.put("bootstrap.servers", bootStrapServer)
    props.put("key.serializer", keySerializer)
    props.put("value.serializer", valueSerializer)
    /*props.put("partitioner.class", "com.test.AtomicityUsingMysql.SensorPartitioner")  //test
    props.put("stability.sensor.name", "check1") //test
    props.put("tyre.sensor.name", "check2") //test */
  /* props.put("partitioner.class", "com.test.AtomicityUsingMysql.SensorPartitioner")
    props.put("stability.sensor.name", "SCS")
    props.put("tyre.sensor.name", "Tyre")*/
    val Kafka_Producer = new KafkaProducer[String, String](props)
    //for (i <- 1 to 100)
      //randomRecordGenerator(topicName, Kafka_Producer, "TCS", 10)
      for (i <- 1 to numOfRecords)
       //randomRecordGenerator(topicName, Kafka_Producer, key, messageLength) // this is for testing
        randomRecordGenerator(topicName, Kafka_Producer, key, messageLength,partitionValue,raceTrack,runType) //current new
       // randomRecordGenerator(topicName, Kafka_Producer, key, messageLength,partitionValue) // use for old project

  }
}
//randomRecordGenerator("CarSensor", Kafka_Producer, "TCS", 10)