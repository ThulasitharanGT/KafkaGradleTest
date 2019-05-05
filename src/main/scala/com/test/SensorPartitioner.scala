package com.test

import java.util._
import org.apache.kafka.clients.producer._
import org.apache.kafka.common._
import org.apache.kafka.common.utils._
import org.apache.kafka.common.record._

class SensorPartitioner extends Partitioner{

  var SensorName:String=null

  override def configure(configs: Map[String, _]): Unit = {
    SensorName=configs.get("speed.sensor.name").toString()
  }

  override def partition(topic: String, key: Any, keyBytes: Array[Byte], value: Any, valueBytes: Array[Byte], cluster: Cluster): Int =
  {
    val PartitionsList=cluster.partitionsForTopic(topic)
    val NumPartitions=PartitionsList.size()
    val sp = math.abs(NumPartitions*0.3)
    var p:Int =0
    if (keyBytes==null || !(key.isInstanceOf[String]) )
      {
        throw new InvalidRecordException("All Records must have Sensor name as key")
      }
    if (key.equals(SensorName)) {
      p = (Utils.toPositive(Utils.murmur2(valueBytes)) % sp).toInt
    }
    else
      {
       p = (Utils.toPositive(Utils.murmur2(keyBytes)) % (NumPartitions-sp)+ sp).toInt
      }
    println("Key = "+key.toString+" Partition = "+ p)
    p
  }


  override def close(): Unit = {}
}
