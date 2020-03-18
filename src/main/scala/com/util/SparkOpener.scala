package com.util

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

trait SparkOpener
  {

    def SparkSessionLoc(name:String):SparkSession={
  /*  val conf=new SparkConf().setAppName(name +"Local" ).setMaster("local")
      conf.set("spark.testing.memory","571859200").set("spark.ui.enabled","true").set("spark.driver.host","localhost").set("spark.sql.parquet.binaryAsString","true").set("spark.sql.avro.binaryAsString","true")
     // System.setProperty("hadoop.home.dir",PathConstants.WINUTILS_EXE_PATH)
*/
      val conf=new SparkConf().setAppName(name +"Local" )
      conf.set("spark.sql.parquet.binaryAsString","true").set("spark.sql.avro.binaryAsString","true")
      SparkSession.builder().config(conf).getOrCreate()
    }
  }

