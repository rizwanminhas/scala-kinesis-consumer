# About

A simple kinesis producer written in scala using the Java AWS KPL library.

## Running the app

1. Start the kinesis via localstack by executing  
  `docker-compose up`
2. Once the kinesis is running execute either `TestSyncProducer.scala` or `TestAsyncProducer.scala`

## Steps to test the producers by consuming the messages using CLI:
1. Install aws cli on your machine e.g. `brew install awscli`(MacOs) or `apt-get install awscli`(Ubuntu)  (Only need to do once).
2. Get the ShardId:
    ```bash 
    aws kinesis describe-stream --stream-name rizwan.test.stream --endpoint-url https://localhost:4568 --no-verify-ssl
    ```
3. Get the Shard Iterator:
    ```bash 
    aws kinesis get-shard-iterator --stream-name rizwan.test.stream --shard-id SHARD_ID_FROM_STEP_2 --shard-iterator-type TRIM_HORIZON --endpoint-url https://localhost:4568 --no-verify-ssl
    ```
4. Get the actual record:
   ```bash
   aws kinesis get-records --endpoint-url https://localhost:4568 --no-verify-ssl --shard-iterator SHARD_ITERATOR_FROM_STEP_3
   ```
5. You will get a Records array, the `Data` key of a Record object
contains the actual event payload encoded in Base64. Use a tool like https://www.base64decode.org/ to decode Base64 and verify the payload.

6. (optional) To list kinesis streams: 
    ```bash
    aws kinesis list-streams --endpoint-url https://localhost:4568 --no-verify-ssl
    ```

## Notes:

1. This code uses the `BasicAWSCredentials`, so you don't have to configure any aws credentials on your machine. For more details check https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
2. If you want you can remove the `BasicAWSCredentials` and create `config` and `credentials` file under `.aws` directory in your home directory as described in the above link.
3. The `docker-compose.yml` starts the kinesis at `https://localhost:4568` and it also creates a stream named `rizwan.test.stream`, these configs are also referenced in the code in `CommonUtils.scala`
