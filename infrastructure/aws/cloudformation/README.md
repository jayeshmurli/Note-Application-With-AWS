# CSYE 6225 - Spring 2019

This repository contains shell scripts to create and delete AWS Virtual Private Cloud (VPC) using the AWS Command Line Interface (AWS CLI).


## Contents
- csye6225-aws-cf-create-stack.sh : The script automates the creation of a custom IPv4 VPC, having 3 public subnets, a public route table and an Internet gateway.
- csye6225-aws-cf-terminate-stack.sh : The script automates the deletion of a previously create VPC Stack.


## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
| Shubhankar Dandekar| 001439467| dandekar.s@husky.neu.edu |
| Jayesh Iyer|001472726 | iyer.j@husky.neu.edu|
| Mitali Salvi|001630137  | salvi.mi@husky.neu.edu|
| Neha Gaikwad|001886361 |gaikwad.n@husky.neu.edu |


## Prerequisites
- AWS CLI
- JQ Library for Bash


## Usage
1. Clone the repository into your local folder 
2. Navigate to the AWS CloudFormation folder 
   ```
   cd <local-folder-path>/infrastructure/aws/cloudformation/
   ```
3. Make appropriate changes to the Cloud Formation Template as per requirements.
   ```
   vi template.json
   ```
4. Run shell script to create a new VPC.
   ```
   sh script.sh <STACK_NAME>
   ```
