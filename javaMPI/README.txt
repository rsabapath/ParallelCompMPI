For the code to work you must set the correct paths
First mpj must be set
the folder mpj here must be set to env variable MPJ_HOME.
Example on how to do so:
export MPJ_HOME=/mpj

Second you must add to path the bin of mpj.
Example on how to do so:
export PATH=$PATH:$MPJ_HOME/bin

Next to make you life easier we suggest adding some alias's.

alias javacmpi="javac -cp .:$MPJ_HOME/lib/mpj.jar"
alias mpjrun="mpjrun.sh"


*********if you don't set alias read further**********
Alias is not set then you must use the following sting to compile:
javac -cp .:$MPJ_HOME/lib/mpj.jar <filename.java>

and following to run

mpjrun.sh -np processes <filename>(no .java)
