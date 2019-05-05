package com.test

import java.util._

import org.apache.kafka.clients.producer._

class My_ProducerCallback extends Callback{
  override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit =
  {
    if (exception !=null)
      {
        println("Asynchronous Message pass failed due to exception")
        println(exception.printStackTrace())
      }
    else
    {
      println("Asynchronous Message pass succedded ")
      println(exception.printStackTrace())
    }
  }
}
