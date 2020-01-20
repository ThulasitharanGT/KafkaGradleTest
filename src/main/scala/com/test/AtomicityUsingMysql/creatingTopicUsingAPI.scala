package com.test.AtomicityUsingMysql

import java.util.Properties

import kafka.admin.RackAwareMode
import kafka.zk.{AdminZkClient, KafkaZkClient}
import org.apache.kafka.common.utils.Time


object creatingTopicUsingAPI {

  def getKafkaZookeeperClientConnection(zookeeperIp:String, isSecure:Boolean, sessionTimeOutMilliSecs:Int, connectionTimeOutMilliSecs:Int, maxInFlightRequests:Int, time:Time, metricGroup:String, metricType:String):KafkaZkClient=KafkaZkClient.apply(zookeeperIp,isSecure,sessionTimeOutMilliSecs,connectionTimeOutMilliSecs,maxInFlightRequests,time,metricGroup,metricType)
  def getAdminZookeeperClientConnection(zkKafkaClient:KafkaZkClient):AdminZkClient=new AdminZkClient(zkKafkaClient)
  def createTopicWithDetails(adminZKClientConnection:AdminZkClient,topicName:String,topicPartitions:Int,topicReplicationFactor:Int,topicConfig:Properties)=adminZKClientConnection.createTopic(topicName,topicPartitions,topicReplicationFactor,topicConfig,RackAwareMode.Disabled)
  def getTopicProperties(adminZKClientConnection:AdminZkClient,topicName:String)=adminZKClientConnection.getAllTopicConfigs().get(topicName) // gives an option [Some] means topic exists option [none] means topic does not exists
  def checkIfTopicExists(zkKafkaClient:KafkaZkClient,topicName:String)=zkKafkaClient.topicExists(topicName) // true if  exists and false if topic does not exists
  def getAllTopic(zkKafkaClient:KafkaZkClient) = zkKafkaClient.getAllTopicsInCluster
  def main(args:Array[String]):Unit={
    val inputMap:collection.mutable.Map[String,String]= collection.mutable.Map[String,String]()
    for (arg <- args)
    {
      val keyPart=arg.split("=",2)(0)
      val valuePart=arg.split("=",2)(1)
      inputMap.put(keyPart,valuePart)
    }
 // for zookeeper connection
    val zookeeperIp=inputMap("zookeeperIp")
    val isSecure=inputMap("isSecure").toBoolean
    val sessionTimeOutMilliSecs=inputMap("sessionTimeOutMilliSecs").toInt
    val connectionTimeOutMilliSecs=inputMap("connectionTimeOutMilliSecs").toInt
    val maxInFlightRequests=inputMap("maxInFlightRequests").toInt
    lazy val time=Time.SYSTEM
    val metricGroup=inputMap("metricGroup")
    val metricType=inputMap("metricType")
    /*
    val zookeeperIp="localhost:3039"
    val isSecure=false
    val sessionTimeOutMilliSecs=200000
    val connectionTimeOutMilliSecs=15000
    val maxInFlightRequests=20
    lazy val time=Time.SYSTEM
    val metricGroup="myGroup"
    val metricType="myType"
     */
    val zkKafkaClient=getKafkaZookeeperClientConnection(zookeeperIp,isSecure,sessionTimeOutMilliSecs,connectionTimeOutMilliSecs,maxInFlightRequests,time,metricGroup,metricType)
    println("Got Zookeeper Kafka Client Connection")
    val adminZKClientConnection=getAdminZookeeperClientConnection(zkKafkaClient)
    println("Got Zookeeper admin Client Connection")
    //topic details
    val topicName=inputMap("topicName")
    val topicPartitions=inputMap("topicPartitions").toInt
    val topicReplicationFactor=inputMap("topicReplicationFactor").toInt
    /*
    val topicName="CarSensor"
    val topicPartitions=10
    val topicReplicationFactor=3
     */
    val topicConfig:Properties=new Properties()

    /*
    adminZKClientConnection.createTopic(topicName,topicPartitions,topicReplicationFactor,topicConfig,RackAwareMode.Disabled)

     */
    createTopicWithDetails(adminZKClientConnection,topicName,topicPartitions,topicReplicationFactor,topicConfig)
    checkIfTopicExists(zkKafkaClient,topicName) match {case true => println("Topic created in cluster"); case _ =>  println("Topic not created in cluster") }
    getTopicProperties(adminZKClientConnection,topicName)
    val allTopicInClusterSeq=getAllTopic(zkKafkaClient)
    for (topic <- allTopicInClusterSeq)
      println(topic)
  }

}
