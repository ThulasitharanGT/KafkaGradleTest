package com.dockerTemp

import com.util.SparkOpener

object readingCsvFileForDocker extends SparkOpener{
  val spark=SparkSessionLoc("temp")
  def main(args: Array[String]): Unit = {
    val df=spark.read.csv(args(0).toString)
    df.show(100,false)
  }
}