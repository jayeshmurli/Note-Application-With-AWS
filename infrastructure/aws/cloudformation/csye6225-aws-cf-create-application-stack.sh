#! /bin/bash


#########################################################################
### Author 	: Jayesh Iyer   					#
### NUID   	: 001472726     					#
### Description : This script creates a application stack on AWS CLI  	#
###		  using Cloud Formation Template.			#
#########################################################################



TEMPLATE_NAME=$1
STACK_NAME=$2
KEY_NAME=$3

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]
  then
    echo "Error! Argument Required"
    echo "Usage - sh script.sh <TemplateFile> <Stack_Name> <Key_Name>" 
    exit 1
fi

if [ ! -e $TEMPLATE_NAME ]
     then
       echo "Error! Template File not exisits"
       exit 1
fi

###### REPLACE=$(sed -i 's/stackvariable/'${STACK_NAME}'/g' template.json)

echo "Fetching latest AMI Image"
ImageId=$(aws ec2 describe-images --owners self --filter "Name=name,Values=csye6225_??????????" --output json | jq -r '.Images | sort_by(.CreationDate) | last(.[]).ImageId')
echo "Image ID : $ImageId "
webserversgid=$(aws ec2 describe-security-groups --output text --filters Name=tag-key,Values=Name Name=tag-value,Values=${STACK_NAME}-Network-csye6225-ws-security-group --query SecurityGroups[].GroupId)
dbserversgid=$(aws ec2 describe-security-groups --output text --filters Name=tag-key,Values=Name Name=tag-value,Values=${STACK_NAME}-Network-csye6225-db-security-group --query SecurityGroups[].GroupId)
subnet1=$(aws ec2 describe-subnets --query "Subnets[?Tags[?Key=='Name']|[?Value=='subnet1']].SubnetId" --output text)
subnet2=$(aws ec2 describe-subnets --query "Subnets[?Tags[?Key=='Name']|[?Value=='subnet2']].SubnetId" --output text)
subnet3=$(aws ec2 describe-subnets --query "Subnets[?Tags[?Key=='Name']|[?Value=='subnet3']].SubnetId" --output text)

echo "Creating stack..."
STACK_ID=$( \
  aws cloudformation create-stack \
  --stack-name ${STACK_NAME}-App \
  --template-body file://${TEMPLATE_NAME} \
  --parameters ParameterKey=NetworkStackName,ParameterValue=${STACK_NAME}-Network  \
  ParameterKey=ImageId,ParameterValue=$ImageId \
  ParameterKey=KeyName,ParameterValue=$KEY_NAME \
  ParameterKey=WebServerSGID,ParameterValue=$webserversgid \
  ParameterKey=DBServerSGID,ParameterValue=$dbserversgid \
  ParameterKey=Subnet1,ParameterValue=$subnet1 \
  ParameterKey=Subnet2,ParameterValue=$subnet2 \
  ParameterKey=Subnet3,ParameterValue=$subnet3 \
  | jq -r .StackId \
)

echo "Waiting on ${STACK_ID} create completion..."
aws cloudformation wait stack-create-complete --stack-name ${STACK_ID}
echo "Create Application Stack Status is : "
aws cloudformation describe-stacks --stack-name ${STACK_ID} | jq .Stacks[0].StackStatus
