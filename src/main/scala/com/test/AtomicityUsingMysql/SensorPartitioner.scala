package com.test.AtomicityUsingMysql

import java.util._
import java.util.concurrent.ThreadLocalRandom

import org.apache.kafka.clients.producer._
import org.apache.kafka.common._
import org.apache.kafka.common.record._

class SensorPartitioner extends Partitioner{

  var stabilitySensorName:String=null
  var tyreSensorName:String=null

  override def configure(configs: Map[String, _]): Unit = {
    stabilitySensorName=configs.get("stability.sensor.name").toString()
    tyreSensorName=configs.get("tyre.sensor.name").toString()
  }

  override def partition(topic: String, key: Any, keyBytes: Array[Byte], value: Any, valueBytes: Array[Byte], cluster: Cluster): Int =
  {
    val PartitionsList=cluster.partitionsForTopic(topic)
    val NumPartitions=PartitionsList.size()
    val sptrac = math.abs(NumPartitions*0.3).toInt // 0 to 2 is tcs
    val sptyre = math.abs(NumPartitions*0.7).toInt // 3 to 7 is tyre , rest goes to normal
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

  key match
      {
    case value if value.equals(stabilitySensorName) =>    ThreadLocalRandom.current().nextInt(0,sptrac-1) match {case value if value >=0 &&  value <=sptrac-1 => p=value case _ =>  println("Tcs partition assignment error") }
    case value if value.equals(tyreSensorName) =>     ThreadLocalRandom.current().nextInt(sptrac,sptyre-1)  match {case value if value >=sptrac &&  value <=sptyre-1 => p=value case _ =>  println("tyre partition assignment error") }
    case _ =>  ThreadLocalRandom.current().nextInt(sptyre,NumPartitions-1)  match {case value if value >=sptyre &&  value <=NumPartitions-1 => p=value case _ =>  println("other partition assignment error") }
  }
   */

    key match
    {
      case value if value.equals(stabilitySensorName) =>   ThreadLocalRandom.current().nextInt(0,sptrac) match {case value if value >=0 &&  value <=sptrac-1 => p=value case _ =>  println("Tcs partition assignment error") }
      case value if value.equals(tyreSensorName) =>     ThreadLocalRandom.current().nextInt(sptrac,sptyre)  match {case value if value >=sptrac &&  value <=sptyre-1 => p=value case _ =>  println("tyre partition assignment error") }
      case _ =>  ThreadLocalRandom.current().nextInt(sptyre,NumPartitions)  match {case value if value >=sptyre &&  value <=NumPartitions-1 => p=value case _ =>  println("other partition assignment error") }
    }

    println("Key = "+key.toString+" Partition = "+ p)
    p
  }


  override def close(): Unit = {}
}
