#/bin/bash

CURDIR=`dirname $0`
echo $CURDIR
cd $CURDIR

java -jar $CURDIR/dist/JavaSh.jar
