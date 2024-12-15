# Bike OpenData - Producer

## Overview
This repo includes a producer which call the [Bike OpenData API](https://tcgbusfs.blob.core.windows.net/dotapp/youbike/v2/youbike_immediate.json) and send the data to Kafka.

## Deployment
1. create fat jar (`sbt clean assembly`)
2. create docker image and push to registry
3. use AWS Fargate to run the docker image

## Some steps of configuration
### Upload image to AWS ECR (things in curly brackets means you need to replace it with your own values)
1. login to AWS ECR   
   (`aws ecr get-login-password --region {Region} | docker login --username AWS --password-stdin {AWS_ACCOUNT_ID}.dkr.ecr.{Region}.amazonaws.com`)
2. create repository
3. tag the image  
   (`docker tag {localImageName}:{tag} {AWS_ACCOUNT_ID}.dkr.ecr.{Region}.amazonaws.com/{repositoryName}:{tag}`)
4. push the image  
   (`docker push {AWS_ACCOUNT_ID}.dkr.ecr.{Region}.amazonaws.com/{repositoryName}:{tag}`)

## Related projects
- Consumer (read message from kafka and insert into postgres): [Link](https://github.com/CuteChuanChuan/__SideProject__BikeOpenData-Consumer/tree/main)