#!/bin/bash


echo "    ***      *** RCP! ***     ***      "
echo " Use: R - rock, S - scissors, P - paper"
echo "    ***      ***      ***     ***      "

R="R"
S="S"
P="P"

while true
do

read usr_answer

# check the correct input 
if [[ "$usr_answer" != "R" ]] && [[ "$usr_answer" != "S" ]] && [[ "$usr_answer" != "P" ]]; then
  echo "ERROR: Read rules!"
  continue
fi

# create pc_answer
fool=false
luck=$(($RANDOM%100))
if [ $luck -le 56 ]
then
  #foolim
  # echo "foolim..."
  if [ "$usr_answer" = "R" ]
  then
    pc_answer=2
  elif [ "$usr_answer" = "S" ]
  then
    pc_answer=0
   else
    pc_answer=1
   fi
else
    #igraem v random
    pc_answer=$(($RANDOM%3))
fi

# pc answer output
if [ "$pc_answer" = "0" ]
then
  echo "PC: R"
elif [ "$pc_answer" = "1" ]
then
  echo "PC: S"
else
  echo "PC: P"
fi

    #result output
    if [ "$usr_answer" = "R" ]
    then
      if [ "$pc_answer" = 0 ]
      then
        echo "Draw..."
      elif [ "$pc_answer" = 2 ]
      then
        echo "You loose :("
      else 
        echo "You win!"
      fi

     elif [ "$usr_answer" = "S" ]
     then
       if [ "$pc_answer" = 1 ]
       then
         echo "Draw..."
      elif [ "$pc_answer" = 2 ]
      then
        echo "You loose :("
      else 
        echo "You win!"
      fi

     elif [ "$usr_answer" = "P" ]
      then
      if [ "$pc_answer" = 2 ]
	  then
	    echo "Draw..."
      elif [ "$pc_answer" = 1 ]
      then
        echo "You loose :("
      else 
        echo "You win!"
      fi
   fi
 
done
