#!/usr/bin/expect -f
set force_conservative 1;

set mdp [lrange $argv 0 0]
set port [lrange $argv 1 1]
set user [lrange $argv 2 2]
set addr [lrange $argv 3 3]
set fold [lrange $argv 4 4]
set args [lrange $argv 4 15]

#set timeout 3 
spawn ssh -p ${port} ${user}@${addr} ${fold}NetworkEngine.sh $args
expect "password:" 
send "${mdp}\r" 
interact
