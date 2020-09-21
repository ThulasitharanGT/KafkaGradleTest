jarName=KafkaGradleTest-1.0-SNAPSHOT-all.jar
#key=SCS
#messageLength=5
#numOfRecords=100
#topicName=CarSensor
keySerializer=org.apache.kafka.common.serialization.StringSerializer
valueSerializer=org.apache.kafka.common.serialization.StringSerializer
bootStrapServer=localhost:9092,localhost:9093,localhost:9094
#partitionValue=2019-12-20
#raceTrack=Spain
#runType=Q1

#jarName=$1
key=$1
messageLength=$2
numOfRecords=$3
topicName=$4
#keySerializer=$6
#valueSerializer=$7
#bootStrapServer=$8
partitionValue=$5 #date added to message , Not the kafka partition
#numOfRecords=$5
raceTrack=$6 #raceTrack added to message
runType=$7 #runType added to message

cd /home/raptor/IdeaProjects/KafkaGradleTest/build/libs/
# using 2.4.5 ain't working throwing some method not found error
spark-submit --class com.test.AtomicityUsingMysql.randomMsgProducer --num-executors 1 --executor-cores 2 --executor-memory 1g --driver-memory 1g --driver-cores 1 --packages org.apache.kafka:kafka-clients:2.3.1,org.apache.kafka:kafka_2.11:2.3.1,org.apache.spark:spark-sql-kafka-0-10_2.11:2.4.4 $jarName key=$key messageLength=$messageLength numOfRecords=$numOfRecords topicName=$topicName keySerializer=$keySerializer valueSerializer=$valueSerializer bootStrapServer=$bootStrapServer partitionValue=$partitionValue raceTrack=$raceTrack runType=$runType


#using spark 2.4.4
#spark-submit --class com.test.AtomicityUsingMysql.randomMsgProducer --num-executors 1 --executor-cores 2 --executor-memory 1g --driver-memory 1g --driver-cores 1 --packages org.apache.kafka:kafka-clients:2.3.1,org.apache.kafka:kafka_2.12:2.3.1,org.apache.spark:spark-sql-kafka-0-10_2.12:2.4.4 $jarName key=$key messageLength=$messageLength numOfRecords=$numOfRecords topicName=$topicName keySerializer=$keySerializer valueSerializer=$valueSerializer bootStrapServer=$bootStrapServer partitionValue=$partitionValue raceTrack=$raceTrack runType=$runType

#spark-submit --class com.test.AtomicityUsingMysql.randomMsgProducer  --num-executors 1 --executor-cores 2 --executor-memory 1g --driver-memory 1g --driver-cores 1 --packages org.apache.kafka:kafka-clients:2.3.1,org.apache.kafka:kafka_2.12:2.3.1,org.apache.spark:spark-sql-kafka-0-10_2.12:2.4.4 KafkaGradleTest-1.0-SNAPSHOT-all.jar  key=SCS messageLength=5 numOfRecords=100 topicName=CarSensor keySerializer=org.apache.kafka.common.serialization.StringSerializer valueSerializer=org.apache.kafka.common.serialization.StringSerializer bootStrapServer=localhost:9092,localhost:9093,localhost:9094 partitionValue=2019-12-20

# sh randomMsgProducer.sh KafkaGradleTest-1.0-SNAPSHOT-all.jar SCS 5 100 CarSensor org.apache.kafka.common.serialization.StringSerializer org.apache.kafka.common.serialization.StringSerializer localhost:9092,localhost:9093,localhost:9094 2019-12-20
