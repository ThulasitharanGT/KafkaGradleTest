jarName=KafkaGradleTest-1.0-SNAPSHOT-all.jar
bootstrapServers=localhost:9092,localhost:9093,localhost:9094 
topicName=CarSensor 
keySerializer=org.apache.kafka.common.serialization.StringSerializer 
valueSerializer=org.apache.kafka.common.serialization.StringSerializer 
checkPointPath=hdfs://localhost/user/raptor/kafka/temp/checkpoint/ 
bronzeTablePath=hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/carSensorStreamedData 
streamTimeOutSeconds=50000

#jarName=$1
#bootstrapServers=$2 
#topicName=$3 
#keySerializer=$4
#valueSerializer=$5 
#checkPointPath=$6
#bronzeTablePath=$7 
#streamTimeOutSeconds=$8


cd /home/raptor/IdeaProjects/KafkaGradleTest/build/libs/

spark-submit --class com.test.AtomicityUsingMysql.testScalaSparkKafkaRead  --num-executors 1 --executor-cores 2 --executor-memory 1g --driver-memory 1g --driver-cores 1 --packages org.apache.kafka:kafka-clients:2.3.1,org.apache.kafka:kafka_2.12:2.3.1,org.apache.spark:spark-sql-kafka-0-10_2.12:2.4.4 $jarName bootstrapServers=$bootstrapServers topicName=$topicName keySerializer=$keySerializer valueSerializer=$valueSerializer checkPointPath=$checkPointPath bronzeTablePath=$bronzeTablePath streamTimeOutSeconds=$streamTimeOutSeconds


#spark-submit --class com.test.AtomicityUsingMysql.testScalaSparkKafkaRead  --num-executors 1 --executor-cores 2 --executor-memory 1g --driver-memory 1g --driver-cores 1 --packages org.apache.kafka:kafka-clients:2.3.1,org.apache.kafka:kafka_2.12:2.3.1,org.apache.spark:spark-sql-kafka-0-10_2.12:2.4.4 KafkaGradleTest-1.0-SNAPSHOT-all.jar bootstrapServers=localhost:9092,localhost:9093,localhost:9094 topicName=CarSensor keySerializer=org.apache.kafka.common.serialization.StringSerializer valueSerializer=org.apache.kafka.common.serialization.StringSerializer checkPointPath=hdfs://localhost/user/raptor/kafka/temp/checkpoint/ bronzeTablePath=hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/carSensorStreamedData streamTimeOutSeconds=50000

# sh kafkaToDumpPath.sh KafkaGradleTest-1.0-SNAPSHOT-all.jar localhost:9092,localhost:9093,localhost:9094 CarSensor org.apache.kafka.common.serialization.StringSerializer org.apache.kafka.common.serialization.StringSerializer hdfs://localhost/user/raptor/kafka/temp/checkpoint/ hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/carSensorStreamedData 50000
