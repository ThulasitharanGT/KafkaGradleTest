package com.test.AtomicityUsingMysql
// use SparkLearning Project --> building in 244 is not okay with 240 spark submit
import com.util.SparkOpener
import io.delta.tables.DeltaTable
import sys.process._
object pushingStreamedDataToDeltaBronze extends SparkOpener{
  val spark=SparkSessionLoc("writing Kafka dump to delta table")

  def main(args: Array[String]): Unit = {
    val inputMap:collection.mutable.Map[String,String]=collection.mutable.Map[String,String]()
    for(arg <- args)
      {
        val keyPart=arg.split("=",2)(0)
        val valPart=arg.split("=",2)(1)
        inputMap.put(keyPart,valPart)
      }

    val basePath=inputMap("basePath")//hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/
    val sourceTable=inputMap("sourceTable")//carSensorStreamedData
    val datePartitionValue=inputMap("datePartitionValue")//2019-12-20
    val bronzeTable=inputMap("bronzeTable")//carSensorBronze
    val dfSource= spark.read.load(basePath+sourceTable+"/date="+datePartitionValue+"/")
    val result="hdfs dfs -ls "+basePath+bronzeTable
    result! match
      {
      case 0 => dfSource.write.mode("overwrite").format("delta").option("spark.sql.sources.partitionOverwriteMode","dynamic").partitionBy("date","topic","partition","key").save(basePath+bronzeTable)
      case _ =>{dfSource.write.mode("append").partitionBy("date","topic","partition","key").save(basePath+bronzeTable);DeltaTable.convertToDelta(spark,"parquet.`"+basePath+bronzeTable+"`","date string,topic string, partition integer, key string")}
    }
    spark.close
    }
}
