#! /bin/bash


#########################################################################
### Author 	: Jayesh Iyer   					#
### NUID   	: 001472726     					#
### Description : This script creates a vpc stack on AWS CLI using 	#
###		  Cloud Formation Template.				#
#########################################################################



TEMPLATE_NAME=$1
STACK_NAME=$2

if [ -z "$1" ] || [ -z "$2" ]
  then
    echo "Error! Argument Required"
    echo "Usage - sh script.sh <TemplateFile> <Stack_Name>" 
    exit 1
fi

###### REPLACE=$(sed -i 's/stackvariable/'${STACK_NAME}'/g' template.json)

echo "Creating stack... ${STACK_NAME}"
STACK_ID=$( \
  aws cloudformation create-stack \
  --stack-name ${STACK_NAME}-IAM \
  --template-body file://${TEMPLATE_NAME} \
  --capabilities CAPABILITY_NAMED_IAM \
  --parameters ParameterKey=IAMStackName,ParameterValue=${STACK_NAME}-IAM \
  | jq -r .StackId \
)

echo "Waiting on ${STACK_ID} create completion..."
aws cloudformation wait stack-create-complete --stack-name ${STACK_ID}
aws cloudformation describe-stacks --stack-name ${STACK_ID} | jq .Stacks[0].StackStatus
