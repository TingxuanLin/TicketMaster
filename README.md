# TicketMaster
-local database: 
 - start-up: mysql -u root -p (password: 12345678)
-public pv4 DNS: ec2-3-19-32-169.us-east-2.compute.amazonaws.com

-tomcat-1 links to tomcat server
 - how to start tomcat? (sudo /opt/tomcat-1/bin/startup.sh)
 - test: http://ec2-3-19-32-169.us-east-2.compute.amazonaws.com:8080/

Open project:  http://ec2-3-19-32-169.us-east-2.compute.amazonaws.com:8080/TicketMaster-1.0-SNAPSHOT

Access vm: ssh -i ~/Downloads/mykey.pem ubuntu@ec2-3-19-32-169.us-east-2.compute.amazonaws.com