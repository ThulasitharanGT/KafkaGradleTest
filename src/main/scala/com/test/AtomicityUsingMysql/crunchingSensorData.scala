package com.test.AtomicityUsingMysql

import com.util.SparkOpener
import io.delta.tables.DeltaTable
object crunchingSensorData extends SparkOpener{
  val spark=SparkSessionLoc("Crunching Kafka")

  def main(args: Array[String]): Unit = {
    val df=spark.read.load("hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/carSensorBronze")
    df.write.mode("overwrite").format("delta").partitionBy("date","topic","partition","key").save("hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/carSensorSilver")
  }

}
