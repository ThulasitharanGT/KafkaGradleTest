jarName=KafkaGradleTest-1.0-SNAPSHOT-all.jar
#topicName=CarSensor
zookeeperIp=localhost:3039 
isSecure=false 
sessionTimeOutMilliSecs=200000 
connectionTimeOutMilliSecs=15000 
maxInFlightRequests=20 
metricGroup=myGroup 
metricType=myType

topicName=$1


cd /home/raptor/IdeaProjects/KafkaGradleTest/build/libs/

spark-submit --class com.test.AtomicityUsingMysql.deleteTopic  --num-executors 1 --executor-cores 2 --executor-memory 1g --driver-memory 1g --driver-cores 1 $jarName  topicName=$topicName  zookeeperIp=$zookeeperIp isSecure=$isSecure sessionTimeOutMilliSecs=$sessionTimeOutMilliSecs connectionTimeOutMilliSecs=$connectionTimeOutMilliSecs maxInFlightRequests=$maxInFlightRequests metricGroup=$metricGroup metricType=$metricType
