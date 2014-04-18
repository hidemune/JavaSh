#/bin/bash
#引数を喋らせます。

#Open JTalk
infile="$1".txt
outfile=${infile}.wav

echo ${infile}
echo ${outfile}

shift

echo "$*"
echo "$*" > ${infile}

#/usr/local/bin/open_jtalk -x /usr/local/share/hts/open_jtalk_dic_utf_8 -m /usr/local/share/hts/mei_happy.htsvoice -ow ${outfile} ${infile}
/usr/local/bin/flite_hts_engine -m /usr/local/share/hts/cmu_us_arctic_slt.htsvoice -ow ${outfile} ${infile}

/usr/bin/aplay ${outfile}

rm ${infile}
rm ${outfile}

