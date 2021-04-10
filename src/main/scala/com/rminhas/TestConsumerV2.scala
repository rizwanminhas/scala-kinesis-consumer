package com.rminhas

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.{KinesisAsyncClient, KinesisClient}
import software.amazon.awssdk.services.kinesis.model.DescribeStreamRequest
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorRequest
import software.amazon.awssdk.services.kinesis.model.GetRecordsRequest

import scala.jdk.CollectionConverters._
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}

import java.net.URI
import software.amazon.awssdk.http.SdkHttpConfigurationOption.TRUST_ALL_CERTIFICATES
import software.amazon.awssdk.http.{Protocol, SdkHttpClient, SdkHttpConfigurationOption}

import java.net._
import java.nio.file.{Files, Paths}
import java.security.cert.X509Certificate
import javax.net.ssl.{HostnameVerifier, SSLSession, X509TrustManager}
import software.amazon.awssdk.http.apache.ApacheHttpClient
import software.amazon.awssdk.http.apache.internal.impl.ApacheSdkHttpClient
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.utils.AttributeMap
import software.amazon.awssdk.http.apache.ApacheHttpClient

object TestConsumerV2 extends App {

  // https://github.com/aws/aws-sdk-java/tree/master/src/samples/AmazonKinesis
  val STREAM_NAME = "rizwan.test.stream"

  val basicCreds = AwsBasicCredentials.create("test", "test")

  val awsCreds = StaticCredentialsProvider.create(basicCreds)

  val region = Region.US_EAST_1

  val nettyClient: SdkAsyncHttpClient =
    NettyNioAsyncHttpClient
      .builder()
      .protocol(Protocol.HTTP1_1)
      .buildWithDefaults(
        AttributeMap
          .builder()
          .put(
            SdkHttpConfigurationOption.TRUST_ALL_CERTIFICATES,
            java.lang.Boolean.TRUE
          )
          .build()
      )
  val kinesisClient = KinesisAsyncClient
    .builder()
    .region(region)
    .credentialsProvider(awsCreds)
    .endpointOverride(URI.create("https://localhost:4568"))
    .httpClient(nettyClient)
    .build()

  val describeStreamRequest = DescribeStreamRequest
    .builder()
    .streamName(STREAM_NAME)
    .build()

  var streamRes = kinesisClient.describeStream(describeStreamRequest)

  println(streamRes.get().streamDescription())

//  while (streamRes.streamDescription().hasMoreShards()) {
//    val shards = streamRes.streamDescription().shards()
//
//    val itReq = GetShardIteratorRequest
//      .builder()
//      .streamName(STREAM_NAME)
//      .shardIteratorType("TRIM_HORIZON")
//      .shardId(shards.get(0).shardId())
//      .build()
//
//    val shardIteratorResult = kinesisClient.getShardIterator(itReq)
//    val shardIterator = shardIteratorResult.shardIterator()
//    val recordsRequest = GetRecordsRequest
//      .builder()
//      .shardIterator(shardIterator)
//      .limit(1000)
//      .build();
//
//    val result = kinesisClient.getRecords(recordsRequest)
//
//    result
//      .records()
//      .asScala
//      .foreach(record => {
//        val byteBuffer = record.data();
//        println(s"Seq No: ${record.sequenceNumber} ${byteBuffer.asByteArray}")
//      })
//
//    streamRes = kinesisClient.describeStream(describeStreamRequest)
//  }

  kinesisClient.close();
}
