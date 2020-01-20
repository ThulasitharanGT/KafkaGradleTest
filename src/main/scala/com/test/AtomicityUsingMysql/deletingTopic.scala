package com.test.AtomicityUsingMysql

import kafka.zk.{AdminZkClient, KafkaZkClient}
import org.apache.kafka.common.utils.Time

object deletingTopic {
  def getKafkaZookeeperClientConnection(zookeeperIp:String, isSecure:Boolean, sessionTimeOutMilliSecs:Int, connectionTimeOutMilliSecs:Int, maxInFlightRequests:Int, time:Time, metricGroup:String, metricType:String):KafkaZkClient=KafkaZkClient.apply(zookeeperIp,isSecure,sessionTimeOutMilliSecs,connectionTimeOutMilliSecs,maxInFlightRequests,time,metricGroup,metricType)
  def getAdminZookeeperClientConnection(zkKafkaClient:KafkaZkClient):AdminZkClient=new AdminZkClient(zkKafkaClient)
  def getTopicProperties(adminZKClientConnection:AdminZkClient,topicName:String)=adminZKClientConnection.getAllTopicConfigs().get(topicName) // gives an option [Some] means topic exists option [none] means topic does not exists
  def checkIfTopicExists(zkKafkaClient:KafkaZkClient,topicName:String)=zkKafkaClient.topicExists(topicName) // true if  exists and false if topic does not exists
  def deleteTopic(adminZKClientConnection:AdminZkClient,topicName:String)=adminZKClientConnection.deleteTopic(topicName)

  def main(args: Array[String]): Unit = {
    val topicName="CarSensor"
    // for zookeeper connection
    val zookeeperIp="localhost:3039"
    val isSecure=false
    val sessionTimeOutMilliSecs=200000
    val connectionTimeOutMilliSecs=15000
    val maxInFlightRequests=20
    lazy val time=Time.SYSTEM
    val metricGroup="myGroup"
    val metricType="myType"
    val zkKafkaClient=getKafkaZookeeperClientConnection(zookeeperIp,isSecure,sessionTimeOutMilliSecs,connectionTimeOutMilliSecs,maxInFlightRequests,time,metricGroup,metricType)
    val adminZKClientConnection=getAdminZookeeperClientConnection(zkKafkaClient)
    deleteTopic(adminZKClientConnection,topicName)
    checkIfTopicExists(zkKafkaClient,topicName)
  }

}

/*

plan .

 create a topic
Dump random data in that topic using random msg producer,
read it using spark streaming (partition by some date(Kill after pushing for one date and read data with other date in CLI)
read where the spark writes the data (completed date) )
push that to a bronze delta table
and to silver and gold

 */
