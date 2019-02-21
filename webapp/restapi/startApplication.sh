mvn package

if [ -z "$1" ] 
  then
    echo "Starting application with default profile"	  
    java -jar target/restapi.jar
  else
    echo "Starting application with $1 profile"	  
    java -jar target/restapi.jar  --spring.profiles.active=$1	  
fi


