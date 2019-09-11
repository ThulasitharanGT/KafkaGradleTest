package com.test

import java.util._
import java.util.concurrent.ThreadLocalRandom
import org.apache.kafka.clients.producer._
import org.apache.kafka.common._
import org.apache.kafka.common.record._

class SensorPartitioner extends Partitioner{

  var tractionSensorName:String=null
  var tyreSensorName:String=null

  override def configure(configs: Map[String, _]): Unit = {
    tractionSensorName=configs.get("traction.sensor.name").toString()
    tyreSensorName=configs.get("tyre.sensor.name").toString()
  }

  override def partition(topic: String, key: Any, keyBytes: Array[Byte], value: Any, valueBytes: Array[Byte], cluster: Cluster): Int =
  {
    val PartitionsList=cluster.partitionsForTopic(topic)
    val NumPartitions=PartitionsList.size()
    val sptrac = math.abs(NumPartitions*0.3).toInt
    val sptyre = math.abs(NumPartitions*0.7).toInt
    var p:Int =0
 if (keyBytes==null || !(key.isInstanceOf[String]) )
      {
        throw new InvalidRecordException("All Records must have Sensor name as key")
      }
  /* if (key.equals(SensorName)) {
      p = (Utils.toPositive(Utils.murmur2(valueBytes)) % sp).toInt
    }
    else
      {
       p = (Utils.toPositive(Utils.murmur2(keyBytes)) % (NumPartitions-sp)+ sp).toInt
      }

    println("Key = "+key.toString+" Partition = "+ p)
 */
  key match
      {
    case value if value.equals(tractionSensorName) =>   p =  ThreadLocalRandom.current().nextInt(0,sptrac-1)
    case value if value.equals(tyreSensorName) =>   p =  ThreadLocalRandom.current().nextInt(sptrac,sptyre-1)
    case _ =>   p =  ThreadLocalRandom.current().nextInt(sptyre,NumPartitions-1)
  }
    println("Key = "+key.toString+" Partition = "+ p)
    p
  }


  override def close(): Unit = {}
}
