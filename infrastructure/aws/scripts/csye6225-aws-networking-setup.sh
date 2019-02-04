NO_OF_SUBNETS=3
# echo "Enter a name for the VPC you wish to create:"
# read VPC_NAME
# echo $(aws ec2 describe-vpcs | jq '.Vpcs | .[] | .Tags | .[] | .Key')

#Create new VPC
echo "Enter IPv4 CIDR for VPC (eg. 10.0.0.0/24):"
read VPC_CIDR
VPC_ID=$(aws ec2 create-vpc --cidr-block $VPC_CIDR | jq -r '.Vpc .VpcId')
echo "VPC created with ID: $VPC_ID"

#Create 3 subnets
for i in `seq 1 $NO_OF_SUBNETS`;
        do
                echo "Enter IPv4 CIDR for Subnet $i of $NO_OF_SUBNETS (eg. 10.0.0.0/24):"
                read SUBNET_CIDR
                SUBNET_ID=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block $SUBNET_CIDR | jq -r '.Subnet .SubnetId')
                echo "Subnet with id $SUBNET_ID created for VPC with id $VPC_ID"
        done   