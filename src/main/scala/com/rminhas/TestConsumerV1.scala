package com.rminhas

import com.amazonaws.services.kinesis.AmazonKinesisClient
import com.amazonaws.ClientConfiguration
import com.amazonaws.regions.Regions
import com.amazonaws.regions.Region
import com.amazonaws.SDKGlobalConfiguration
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.kinesis.model.{GetRecordsRequest, GetShardIteratorRequest}
import com.amazonaws.services.kms.AWSKMSClientBuilder

import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters._

object TestConsumerV1 extends App {

  val STREAM_NAME = "rizwan.test.stream"
  System.setProperty(
    SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY,
    "true"
  );
  System.setProperty("com.amazonaws.sdk.disableCbor", "true")
   val awsCredentials = new BasicAWSCredentials("test", "test")

  val endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("https://localhost:4568","us-east-1")
  val client = AmazonKinesisClientBuilder
    .standard()
    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withEndpointConfiguration(endpointConfiguration)
    .build()

  val describeStream = client.describeStream(STREAM_NAME)
  describeStream.getStreamDescription.getShards.asScala.foreach(shard => {
    val getShardIteratorRequest = new GetShardIteratorRequest()
    getShardIteratorRequest.setStreamName(STREAM_NAME)
    getShardIteratorRequest.setShardId(shard.getShardId)
    getShardIteratorRequest.setShardIteratorType("TRIM_HORIZON")
    val getShardIteratorResult = client.getShardIterator(getShardIteratorRequest)
    val shardIterator = getShardIteratorResult.getShardIterator

    val getRecordsRequest = new GetRecordsRequest()
    getRecordsRequest.setShardIterator(shardIterator)
    getRecordsRequest.setLimit(25)

    val getRecordsResult = client.getRecords(getRecordsRequest)
    getRecordsResult.getRecords.asScala.foreach(record => {

      println(StandardCharsets.UTF_8.decode(record.getData).toString())
    })
  })
}
