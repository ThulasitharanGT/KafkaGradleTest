package com.test.AtomicityUsingMysql

import java.io.FileInputStream
import java.sql.{DriverManager, ResultSet}
import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import org.apache.kafka.common.TopicPartition

object AtomicityUpdateFromMysqlDb
{
  val userDir=System.getProperty("user.dir")
  val propertiesStream = new FileInputStream(userDir+"/src/main/scala/com/properties/Mysql_auth.properties")
  val mysqlProps= new Properties()
  mysqlProps.load(propertiesStream)
  val db="kafka_db"
  val Mysql_connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db+"?useLegacyDatetimeCode=false&serverTimezone=UTC", mysqlProps)

  def saveAndCommit(Kafka_consumer:KafkaConsumer[String,String],Consumer_record:ConsumerRecord[String,String]):Unit =
{
  try {

     Class.forName("com.mysql.jdbc.Driver")
     Mysql_connection.setAutoCommit(false)
     val insertSql="insert into "+db+".tss_data values ('"+Consumer_record.key+"','"+Consumer_record.value+ "',"+Consumer_record.partition()+") "
     val UpdateSql="update "+db+".tss_offset set offset="+(Consumer_record.offset+1)+" where Topicname= '"+Consumer_record.topic+"' and partition_id="+Consumer_record.partition()+" "
     println("Performing Query --->>> ")
     println(insertSql)
     val InsertSqlStatement= Mysql_connection.prepareStatement(insertSql)
     InsertSqlStatement.executeUpdate()
     println("Performed Query --->>> ")
     println(insertSql)

     println("Performing Query --->>> ")
     println(UpdateSql)
     val UpdateSqlStatement = Mysql_connection.prepareStatement(UpdateSql)
     UpdateSqlStatement.executeUpdate()
     println("Performed Query --->>> ")
     println(UpdateSql)
     Mysql_connection.commit()

  }
}


  def getOffsetFromDB(Topic_Partition : TopicPartition):String =
  {
    var Partition_Offset : String=null
      try {

      Class.forName("com.mysql.jdbc.Driver")
      Mysql_connection.setAutoCommit(false)
      var SelectSql="select offset from "+db+".tss_offset where  Topicname = '"+Topic_Partition.topic()+"' and partition_id="+Topic_Partition.partition()
      println("Performing Query --->>> ")
      println(SelectSql)
      val Partition_Statement = Mysql_connection.createStatement()
      val Partition_Statement_resultSet:ResultSet = Partition_Statement.executeQuery(SelectSql)
      if(Partition_Statement_resultSet.next()){
        Partition_Offset = Partition_Statement_resultSet.getString("offset");
      }
      //  Partition_Offset = Partition_Statement_resultSet.getString("offset")
      println("Performed Query --->>> ")
      println(SelectSql)
      println(Partition_Offset)
      Mysql_connection.commit()
      Partition_Offset.toString
    }
    catch
      {
        case e:Exception  =>
          {
            e.printStackTrace()
            Partition_Offset
          }
      }
    finally
    {
      Partition_Offset
    }
  }

  def closeConnectionMysql():Boolean=
  if(Mysql_connection.close()==null)
    {
      true
    }
  else
    {
      false
    }

}
