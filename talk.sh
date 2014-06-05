#/bin/bash
#引数を喋らせます。

#Open JTalk
infile=/tmp/"$1".txt
outfile=${infile}.wav

echo ${infile}
echo ${outfile}

shift

echo "$*"
echo "$*" > ${infile}

/usr/local/bin/open_jtalk -x /home/user/tool/voiceLinux/OpenJTalk/open_jtalk-1.07/mecab-naist-jdic -m /usr/local/share/hts/mei_normal.htsvoice -ow ${outfile} ${infile}
#/usr/local/bin/flite_hts_engine -m /usr/local/share/hts/cmu_us_arctic_slt.htsvoice -ow ${outfile} ${infile}

/usr/bin/aplay ${outfile}

rm ${infile}
rm ${outfile}

