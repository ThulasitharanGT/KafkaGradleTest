#reading data for batch job putting it into Delta table.run using spark 240

jarName=SparkLearning-1.0-SNAPSHOT.jar
basePath=hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/ 
sourceTable=carSensorStreamedData 
#datePartitionValue=2019-12-20 
bronzeTable=carSensorBronze 
overWriteOrAppend=append

#jarName=$1
#basePath=$2
#sourceTable=$3
datePartitionValue=$1
#bronzeTable=$5
#overWriteOrAppend=$6 



cd /home/raptor/IdeaProjects/SparkLearning/build/libs/

spark-submit --class org.controller.deltaLakeEG.pushingStreamedDataToDeltaBronze  --num-executors 1 --executor-cores 2 --executor-memory 1g --driver-memory 1g --driver-cores 1 --packages io.delta:delta-core_2.11:0.5.0 $jarName basePath=$basePath sourceTable=$sourceTable datePartitionValue=$datePartitionValue bronzeTable=$bronzeTable overWriteOrAppend=$overWriteOrAppend


#spark-submit --class org.controller.deltaLakeEG.pushingStreamedDataToDeltaBronze  --num-executors 1 --executor-cores 2 --executor-memory 1g --driver-memory 1g --driver-cores 1 --packages io.delta:delta-core_2.11:0.5.0 SparkLearning-1.0-SNAPSHOT.jar basePath=hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/ sourceTable=carSensorStreamedData datePartitionValue=2019-12-20 bronzeTable=carSensorBronze overWriteOrAppend=append

# sh pushingDataFromDumpToBronze.sh SparkLearning-1.0-SNAPSHOT.jar hdfs://localhost/user/raptor/kafka/temp/output/kafkaDeltaTableDump/ carSensorStreamedData 2019-12-20 carSensorBronze append
