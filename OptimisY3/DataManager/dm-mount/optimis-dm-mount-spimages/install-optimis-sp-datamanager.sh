#!/bin/bash

IProvider=$1

if [ "" == "$IProvider" ]; then
  echo "Error: You must specify the infrastructure provider name located in conf folder"
  exit
fi

confFile=./conf/$IProvider.conf
if [ !  -f $confFile ]; then
  echo "Error: Infrastructure provider is not valid: $confFile"
  exit
fi

cp optimis-sp-mount  /usr/bin/
cp optimis-sp-umount /usr/bin
cp optimis-sp-datamanager /etc/init.d/
chmod +x /etc/init.d/optimis-sp-datamanager

echo "Copying key for infrastructure provider $IProvider"
cp $confFile /etc/datamanager-sp.conf

if [ -f /etc/debian_version ]; then
    OS=Debian
    apt-get install sshfs
    apt-get install ruby
    update-rc.d optimis-sp-datamanager defaults
elif [ -f /etc/redhat-release ]; then
    OS=Centos
    wget http://packages.sw.be/rpmforge-release/rpmforge-release-0.5.2-2.el5.rf.`uname -i `.rpm
    rpm --import http://apt.sw.be/RPM-GPG-KEY.dag.txt
    rpm -K rpmforge-release-0.5.2-2.el5.rf.*.rpm
    rpm -i rpmforge-release-0.5.2-2.el5.rf.*.rpm
    yum search sshfs
    yum install fuse-sshfs.`uname -i`
    yum install ruby rubygems
    rm rpmforge-release-0.5.2-2.el5.rf.*.rpm
    chkconfig --add optimis-sp-datamanager
    chkconfig --level 345 optimis-sp-datamanager on
fi



