# CSYE 6225 - Spring 2019

This repository contains shell scripts to create and delete AWS Virtual Private Cloud (VPC) using the AWS Command Line Interface (AWS CLI).


## Contents
- csye6225-aws-cf-create-stack.sh : The script automates the creation of a custom IPv4 VPC, having 3 public subnets, a public route table and an Internet gateway.
- csye6225-aws-cf-terminate-stack.sh : The script automates the deletion of a previously create VPC Stack.


## Prerequisites
- AWS CLI
- JQ Library for Bash


## Configuration
- AWS Cloud formation uses templates in either JSON or YAML format.  
- [Sample Reference Templates](https://aws.amazon.com/cloudformation/aws-cloudformation-templates/)
   

## Usage
1. Clone the repository into your local folder 
2. Navigate to the AWS CloudFormation folder 
   ```
   cd <local-folder-path>/infrastructure/aws/cloudformation/
   ```
3. Create new AWS Cloudformation template as per requirements and place it in same folder as the script.
   ```
   vi template.json
   ```
4. Run shell script to create a new VPC.
   ```
   sh script.sh <STACK_NAME>
   ```
