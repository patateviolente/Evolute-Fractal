#/bin/bash
# Demande qui provient de l'extérieur d'appel au moteur de rendu

cd $1
# on regroupe les arguments
args=""
shift
while [ ! $1 = "" ]
do
	args="$args $1"
	shift
done
java fract.renderEngine.Analyzer $args
