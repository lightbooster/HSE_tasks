#!/bin/bash
echo "    *****       CALCULATOR       *****   "
echo " [number 1] >> [expression] >> [number 2]"

function interpret()
{
 echo $1 > num.txt
 
 if [ `grep "^[nmNM][oauyOAUY][lL]*" num.txt` ]
 then
  rm num.txt
  return 0
 fi
 
 if [ `grep "^[oaeiuOAEIU][dtrnDTRN].*" num.txt` ]
 then
  rm num.txt
  return 1
 fi
 
 if [ `grep "^[dtprDTPR][vfhwVFHW]." num.txt` ]
 then
  rm num.txt
  return 2
 fi
 
 if [ `grep "^[tpdTDP][rlnRLN]." num.txt` ]
 then
  rm num.txt
  return 3
 fi
 
 if [ `grep "^[chstCHST][chstCHST][eiEI][tdTD][yuieYUIE][rR]." num.txt` ]
 then
  rm num.txt 
  return 4
 fi
 
 if [ `grep "^[pP][yaiYAI][tdnpTDNP]*" num.txt` ]
 then
  rm num.txt
  return 5
 fi
 
 if [ `grep "^[shcSHC][shcSHC][eiaEIA][sczSCZ]\W*" num.txt` ]
 then
  rm num.txt
  return 6
 fi
 
 if [ `grep "^[cszCSZ][eiaEIA][mnltMNLT]\W*" num.txt` ]
 then
  rm num.txt
  return 7
 fi
 
 if [ `grep "^[vwfVWF][oauyOAUY][sczSCZ][eiaEIA]\W*" num.txt` ]
 then
  rm num.txt
  return 8
 fi
 
 if [ `grep "^[dtpDTP][eiaEIA][vwfVWF][yiaYIA][tdpTDP]\W*" num.txt` ]
 then
  rm num.txt
  return 9
 fi
 
 return 0
}


function interpret_action()
{
 echo $1 > action.txt
 
 if [ `grep "^[ptdfPTDF][lnLN][uyaeUYAE][sczSCZ]" action.txt` ]
 then
  rm action.txt
  return 1
 fi
 
 if [ `grep "^[nmNM][ieuaIEUA][nmNM][uyaiIEUA][sczSCZ]" action.txt` ]
 then
  rm action.txt
  return 2
 fi
 
 if [ `grep "^[uyUY][mnMN][mnMN][oauyOAUY][zscZSC][yuiYUI][tdTD]\W*" action.txt` ]
 then
  rm action.txt
  return 3
 fi
 
 
 if [ `grep "^[dtpDTP][eiEI][lnmLNM][eiEI][tdTD]\W*" action.txt > result_znak.txt` ]
 then
  rm action.txt
  return 4
 fi
 return -1
}

while true
do

read num1

if [ "$num1" = "exit" ]
then
	exit
fi

read exp
read num2

# interpetyruem 
interpret $num1
num1=$?

interpret $num2
num2=$?

interpret_action $exp
exp=$?

case $exp in
  0) exp="no_action";;
  1) exp="+";;
  2) exp="-";;
  3) exp="/";;
  4) exp="*";;
esac
# zakonchili interpretirovat'

if [ "$exp" = "no_action" ]
then
  echo "ERROR: NO SUCH ACTION"
  exit
fi

if [[ "$num1" == "-1" ]] && [[ "$num2" == "-1" ]]
then
  echo "ERROR: NO SUCH NUMBER"
  exit
fi

if [[ "$exp" = "/" ]] && [[ "$num2" = "0" ]]
then
  echo "ERROR: ZERO DIVISION DENIED"
else

  echo "------------------"
  echo "RESULT:"
  if [ "$exp" = "*" ]
  then
    echo "$num1 $exp $num2 = `expr $num1 "*" $num2`"
  else
    echo "$num1 $exp $num2 = `expr $num1 $exp $num2`"
  fi
  echo "------------------"

fi


done
