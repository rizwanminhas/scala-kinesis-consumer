# About

Simple kinesis consumers written in scala using the Java AWS KCL v1 and v2 libraries.

## Running the app

1. Start the kinesis via localstack by executing  
  `docker-compose up`
2. Once the kinesis is running execute `TestConsumerV1.scala`

## Notes:

1. `TestConsumerV2.scala` is still wip, currently it is throwing 502 error.