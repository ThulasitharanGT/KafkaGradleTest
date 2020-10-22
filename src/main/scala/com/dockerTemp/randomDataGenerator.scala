package com.dockerTemp

import com.util.SparkOpener
import java.util.concurrent.ThreadLocalRandom

object randomDataGenerator extends SparkOpener{
  val tempNumberSet= 1 to 100000000
  val tempAlphabetSet = ('a' to 'z') ++ ('A' to 'Z')
  def randomInteger = tempNumberSet(ThreadLocalRandom.current().nextInt(0,tempNumberSet.size)).toLong
  def randomString(sizeOfString:Int) = {
    var resultString=""
    for (i <- 1 to sizeOfString)
      resultString=resultString+(tempAlphabetSet(ThreadLocalRandom.current().nextInt(1,tempAlphabetSet.size)))
    resultString
  }
  def randomDataFrameGenerator(spark:org.apache.spark.sql.SparkSession,numOfRecords:Int,stringSize:Int):org.apache.spark.sql.DataFrame={
    var tempArrayBuffer=collection.mutable.ArrayBuffer[tempCaseClassForRandomDataFrame]()
    for (i <- 0 to numOfRecords)
      tempArrayBuffer+= tempCaseClassForRandomDataFrame(randomString(stringSize),randomInteger)
    import spark.implicits._
    tempArrayBuffer.toSeq.toDF
  }
  def randomDataFrameStreamGenerator(spark:org.apache.spark.sql.SparkSession,numOfRecordsPerBatch:Int,stringSize:Int)={
    val startStreamDF = spark.readStream.format("rate").option("rowsPerSecond",s"${numOfRecordsPerBatch}").load.withColumn("randomInt",org.apache.spark.sql.functions.lit(randomInteger)).withColumn("randomString",org.apache.spark.sql.functions.lit(randomString(stringSize)))
    startStreamDF.writeStream/*.outputMode("append")*/.format("console").option("checkpointLocation",s"file:///user/tmp/stream/checkpoint_${spark.sparkContext.applicationId}/").foreachBatch(
      (batchDF:org.apache.spark.sql.DataFrame,tmpLong:Long)=> {
      import java.text.SimpleDateFormat
      import java.util.Date
      import org.apache.spark.sql.functions._
      val tempDateFormat= new SimpleDateFormat("yyyyMMddHHmmss")
      val dateValue=new Date()
        batchDF.withColumn("appTimeFormat",lit(tempDateFormat.format(dateValue))).withColumn("appID",lit(spark.sparkContext.applicationId)).withColumn("longValueIn",lit(tmpLong)).show(false)
    }).start.awaitTermination
  }
  def main(args:Array[String]):Unit={
    val spark=SparkSessionLoc("tempSession")
    val inputMap=collection.mutable.Map[String,String]()
   for (arg <- args)
     {
       val keyPart=arg.split("=",2)(0)
       val valPart=arg.split("=",2)(1)
       inputMap.put(keyPart,valPart)
     }
    val numberOfRecordsPerBatch=inputMap("numberOfRecordsPerBatch").toInt
    val sizeOfString=inputMap("sizeOfString").toInt
    randomDataFrameStreamGenerator(spark,numberOfRecordsPerBatch,sizeOfString)
    //randomDataFrameGenerator(spark,numberOfRecords,sizeOfString).show(false)
  }
}
