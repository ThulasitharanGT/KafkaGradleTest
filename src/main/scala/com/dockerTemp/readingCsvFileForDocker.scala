package com.dockerTemp

import com.util.SparkOpener

object readingCsvFileForDocker extends SparkOpener{
  val spark=SparkSessionLoc("temp")
  def main(args: Array[String]): Unit = {
    val filePath=args(0)
    val showInteger=args(1).toInt
    val df=spark.read.csv(filePath)
    df.show(showInteger,false)
  }
}
