package com.test.AtomicityUsingMysql

import com.util.SparkOpener
import org.apache.spark.sql.functions._

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

      val query = df.withColumn("date",lit("2019-12-27")).writeStream.outputMode("append").format("parquet").option("checkpointLocation","hdfs://localhost/user/raptor/kafka/temp/checkpoint").option("path","hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/carSensorBronze").partitionBy("key","date","partition").start()

      /* local
      val df = spark.readStream.format("kafka").option("kafka.bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094").option("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer").option("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer").option("subscribe", "CarSensor").option("startingOffsets", "earliest").load()

      val query = df.writeStream.outputMode("append").format("parquet").option("checkpointLocation","file:///home/raptor/kafka/temp/checkpoint").option("path","file:///home/raptor/kafka/temp/output/kafkaDeltaTableDump/carSensorBronze").partitionBy("key").start()
      */
      query.awaitTermination()

      //// runs in spark 2.4.4 due to 3.2.10 jackson and json jars not supported for scala 2.12
      //
      //cd /home/raptor/IdeaProjects/KafkaGradleTest/build/libs/
      //
      // spark submit use spark 2.4.4
      //spark-submit --class com.test.AtomicityUsingMysql.testScalaSparkKafkaRead --deploy-mode client --master yarn --num-executors 1 --executor-memory 1g --executor-cores 2 --driver-memory 1g --driver-cores 1  --packages org.apache.spark:spark-sql-kafka-0-10_2.12:2.4.4,com.fasterxml.jackson.core:jackson-databind:2.10.1,com.fasterxml.jackson.core:jackson-core:2.10.1,org.codehaus.jackson:jackson-core-asl:1.9.13,org.codehaus.jackson:jackson-mapper-asl:1.9.13,com.fasterxml.jackson.core:jackson-annotations:2.10.1,com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.10.1,com.fasterxml.jackson.module:jackson-module-scala_2.12:2.10.1,com.fasterxml.jackson.module:jackson-module-jaxb-annotations:2.10.1,org.json4s:json4s-jackson_2.12:3.5.3,com.twitter:parquet-jackson:1.6.0,org.codehaus.jackson:jackson-jaxrs:1.9.13,org.codehaus.jackson:jackson-xc:1.9.13,com.fasterxml.jackson.module:jackson-module-paranamer:2.10.1,com.google.protobuf:protobuf-java:3.11.1,org.apache.htrace:htrace-core:3.1.0-incubating,commons-cli:commons-cli:1.4  KafkaGradleTest-1.0-SNAPSHOT-all.jar
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
