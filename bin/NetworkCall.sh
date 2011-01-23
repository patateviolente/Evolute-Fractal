# $1 = port (22)
# $2 = utilisateur
# $3 = adresse distante
# $4 = dossier
# $... = arguments pour le calcul du fractal
unset SSH_AUTH_SOCK

if [ ! $4 = "" ]
then
        # demande externe
        
        user=$2
        addr=$3
        port=$1
        fold=$4
        args=""
		while [ ! $4 = "" ]
		do
			args="$args $4"
			shift
		done
		echo "${fold}NetworkEngine.sh $args | ssh -p ${port} ${user}@${addr} "
        echo "${fold}NetworkEngine.sh $args" | ssh -p ${port} ${user}@${addr} 
else
        # recherche auto NetworkEngine
        ssh -p $1 $2@$3 locate bin/NetworkEngine.sh --limit 1 | sed s/NetworkEngine.sh$//g
fi
