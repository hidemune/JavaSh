■UbuntuへのOpenJTalkのインストール方法


・build-essential は、インストールしておく。


・hts_engine_API のインストール

./configure
make
sudo make install


・open_jtalk のインストール

./configure --with-hts-engine-header-path=/usr/local/include --with-hts-engine-library-path=/usr/local/lib --with-charset=UTF-8
make
sudo make install


・音声ファイルの設置
sudo mkdir /usr/local/share/hts
sudo cp nitech_jp_atr503_m001.htsvoice /usr/local/share/hts


・UTF-8辞書ファイルの設置
sudo cp -r open_jtalk_dic_utf_8-1.07 /usr/local/share/hts/open_jtalk_dic_utf_8


・動作テスト

echo "こんにちは" > infile.txt

open_jtalk -x /usr/local/share/hts/open_jtalk_dic_utf_8 -m /usr/local/share/hts/nitech_jp_atr503_m001.htsvoice -ow out.wav infile.txt

aplay out.wav

音声が鳴れば成功
