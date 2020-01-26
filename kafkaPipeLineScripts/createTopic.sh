jarName=KafkaGradleTest-1.0-SNAPSHOT-all.jar
topicName=CarSensor
topicPartitions=10
topicReplicationFactor=3 
zookeeperIp=localhost:3039 
isSecure=false 
sessionTimeOutMilliSecs=200000 
connectionTimeOutMilliSecs=15000 
maxInFlightRequests=20 
metricGroup=myGroup 
metricType=myType

#jarName=$1
#topicName=$2
#topicPartitions=$3
#topicReplicationFactor=$4
#zookeeperIp=$5 
#isSecure=$6 
#sessionTimeOutMilliSecs=$7 
#connectionTimeOutMilliSecs=$8 
#maxInFlightRequests=$9 
#metricGroup=$10 
#metricType=$11

cd /home/raptor/IdeaProjects/KafkaGradleTest/build/libs/

spark-submit --class com.test.AtomicityUsingMysql.creatingTopicUsingAPI  --num-executors 1 --executor-cores 2 --executor-memory 1g --driver-memory 1g --driver-cores 1 --packages org.apache.kafka:kafka-clients:2.3.1,org.apache.kafka:kafka_2.12:2.3.1,org.apache.spark:spark-sql-kafka-0-10_2.12:2.4.4 $jarName  topicName=$topicName topicPartitions=$topicPartitions topicReplicationFactor=$topicReplicationFactor zookeeperIp=$zookeeperIp isSecure=$isSecure sessionTimeOutMilliSecs=$sessionTimeOutMilliSecs connectionTimeOutMilliSecs=$connectionTimeOutMilliSecs maxInFlightRequests=$maxInFlightRequests metricGroup=$metricGroup metricType=$metricType

#spark-submit --class com.test.AtomicityUsingMysql.creatingTopicUsingAPI  --num-executors 1 --executor-cores 2 --executor-memory 1g --driver-memory 1g --driver-cores 1 --packages org.apache.kafka:kafka-clients:2.3.1,org.apache.kafka:kafka_2.12:2.3.1,org.apache.spark:spark-sql-kafka-0-10_2.12:2.4.4 KafkaGradleTest-1.0-SNAPSHOT-all.jar  topicName=CarSensor topicPartitions=10 topicReplicationFactor=3 zookeeperIp=localhost:3039 isSecure=false sessionTimeOutMilliSecs=200000 connectionTimeOutMilliSecs=15000 maxInFlightRequests=20 metricGroup=myGroup metricType=myType


#sh createTopic.sh KafkaGradleTest-1.0-SNAPSHOT-all.jar CarSensor 10 3 localhost:3039 false 200000 15000 20 myGroup myType
