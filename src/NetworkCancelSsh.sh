#/bin/bash
# annule le dernier processus ssh
kill `ps -A | grep " ssh$" | tail -n 1 | grep "^[0-9]*"`
