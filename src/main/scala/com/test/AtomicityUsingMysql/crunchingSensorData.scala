package com.test.AtomicityUsingMysql

import com.util.SparkOpener
import io.delta.tables.DeltaTable
object crunchingSensorData extends SparkOpener{
  val spark=SparkSessionLoc("writing Kafka dump to delta table")

  def main(args: Array[String]): Unit = {
    val inputMap:collection.mutable.Map[String,String]=collection.mutable.Map[String,String]()
    for(arg <- args)
      {
        val keyPart=arg.split("=",2)(0)
        val valPart=arg.split("=",2)(1)
        inputMap.put(keyPart,valPart)
      }
    val df=spark.read.load("hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/carSensorBronze")
    df.write.mode("overwrite").format("delta").partitionBy("date","topic","partition","key").save("hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/carSensorSilver")
  }

}
