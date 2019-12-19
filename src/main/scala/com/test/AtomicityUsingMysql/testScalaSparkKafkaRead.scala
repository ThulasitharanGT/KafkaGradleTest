package com.test.AtomicityUsingMysql

import com.util.SparkOpener

object testScalaSparkKafkaRead extends SparkOpener{

  val spark=SparkSessionLoc("Temp For Kafka")
  val sc = spark.sparkContext
  //sc.setLogLevel("OFF")
  //ALL,DEBUG,ERROR,FATAL,TRACE,WARN,INFO,OFF
  def main(args:Array[String]):Unit={
    val df = spark.readStream.format("kafka").option("kafka.bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094").option("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer").option("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer").option("startingOffsets", "earliest").option("subscribe", "CarSensor").load()  //
    println("-----------------------------------------><----------------------------------------------")
    try {
      //val query = df.writeStream.outputMode("append").format("parquet").option("checkpointLocation","checkpoint").option("path",System.getProperty("user.dir")+"/output/kafka/CarSensor").partitionBy("key").start()
    //  val query = df.writeStream.outputMode("append").format("parquet").option("checkpointLocation",System.getProperty("user.dir")+"/checkpointStream/checkpoint").option("path",System.getProperty("user.dir")+"/output/kafkaTableDump/carSensorBronze").partitionBy("key").start()
      //.format("delta")  - delta does'nt work.. Jackson error came so imported dependency
    //hdfs
    //val df = spark.readStream.format("kafka").option("kafka.bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094").option("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer").option("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer").option("subscribe", "CarSensor").option("startingOffsets", "earliest").load()

      val query = df.writeStream.outputMode("append").format("parquet").option("checkpointLocation","hdfs://localhost/user/raptor/kafka/temp/checkpoint").option("path","hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/carSensorBronze").partitionBy("key").start()

      /* local
      val df = spark.readStream.format("kafka").option("kafka.bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094").option("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer").option("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer").option("subscribe", "CarSensor").option("startingOffsets", "earliest").load()

      val query = df.writeStream.outputMode("append").format("parquet").option("checkpointLocation","file:///home/raptor/kafka/temp/checkpoint").option("path","file:///home/raptor/kafka/temp/output/kafkaDeltaTableDump/carSensorBronze").partitionBy("key").start()
      */
      query.awaitTermination()
    }
    catch  {
      case e:Exception => println(e.printStackTrace())
    }
  }
/*  def main(args: Array[String]): Unit = {
    val df=spark.read.load("/home/raptor/IdeaProjects/KafkaGradleTest/output/Kafka/CarSensor/")
    df.show()
    val newDf=df.selectExpr("CAST(key as STRING)","CAST(value as String)","cast(topic as STRING)","cast (partition as Integer)"
      ,"cast (offset as Integer)","cast (timestamp as timestamp)","cast (timestampType as Integer)")
    newDf.show
    println(newDf.columns.startsWith("t"))

  }*/


  def getByteArrayAsString (valueString:String):String={
    val newVal=valueString.replace("[","").replace("]","")
    val newValFin=newVal.split(" ")
    val finArrayByte=newValFin.map(_.toByte)
    finArrayByte.map(_.toChar).mkString
  }

}
