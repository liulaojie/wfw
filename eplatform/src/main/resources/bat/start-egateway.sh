. start-base.sh
java -Xmx128m -Xms128m -Dserver.servlet.context-path=abi551 -Dcom.esen.deploy.mode="microservice" -Dcom.esen.route="$(pwd)/routeconfig.properties" -Desen.ecore.workdir="$ABIDIR" -jar $JARDIR/com/esen/excutable/egateway/1.5.1-SNAPSHOT/egateway-1.5.1-SNAPSHOT.jar