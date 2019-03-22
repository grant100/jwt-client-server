#!/bin/bash
sudo keytool -import -trustcacerts -keystore /Library/Java/JavaVirtualMachines/jdk1.8.0_151.jdk/Contents/Home/jre/lib/security/cacerts \
   -storepass changeit -noprompt -alias localhost -file localhost.crt
