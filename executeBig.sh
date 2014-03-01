# Compiling

echo 'Compiling'
rm *.class
export CLASSPATH=.
javac *.java

echo 'Test to run from 1 to 10'
read test_number

#echo 'Run for how long? (minutes)'
#read m
#s = $(($m * 60)) 
#echo " Program will run for max of $m minutes"

time java ProcessAssignment dms_assignment1_large/instance_$test_number.txt dms_assignment1_large/initial_$test_number.txt myResults/Myresult_large_$test_number.txt 

#cmdpid = $!
#sleep $s

#if [ -d /proc/$cmdpid ]
#then
#  echo "terminating program PID:$cmdpid"
#  kill $cmdpid
#fi


