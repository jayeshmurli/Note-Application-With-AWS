#! /bin/bash

#tail /etc/profile -n 5 > /tmp/tempFile.sh
#sudo chmod 777 /tmp/tempFile.sh
#sudo fuser -k 80/tcp
#echo "cd /var" >> /tmp/tempFile.sh
#echo "sudo java -jar restapi.jar --server.port=80 --spring.datasource.url=jdbc:mysql://\$DB_HOST:\$DB_PORT/csye6225 --spring.datasource.username=\$DB_USERNAME --spring.datasource.password=\$DB_PASSWORD --cloud.islocal=false --cloud.bucketName=\$S3_BUCKET" >> /tmp/tempFile.sh
#cd /tmp/

#./tempFile.sh > run.out 2> run.err &
fuser -k 80/tcp
source /etc/profile
cd /var
java -jar -Dspring.profiles.active=jayesh-dev restapi.jar --server.port=80 > /tmp/run.out 2> /tmp/run.err &
