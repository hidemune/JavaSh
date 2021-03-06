/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import static java.lang.Thread.sleep;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hdm
 */
public class JavaSh {
public static frmTerminal frmT = new frmTerminal();
public static String talkExecIn ;
public static String talkExec ;
public static String talkSh ;
public static String talkEnExecIn ;
public static String talkEnExec ;
public static String talkEnSh ;
public static String user ;
//public static boolean terminate = false;
public static ArrayList<String[]> Dict = new ArrayList<String[]>();
//public static Process backTalkP;
public static String pwd = "/home";
public static String[] url = new String[10];
public static String mainPage = "";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //プロパティファイルの読み込み
        Properties config = new Properties();
        try {
            //config.load(new FileInputStream("card.properties"));
            config.load(new InputStreamReader(new FileInputStream("JavaSh.properties"), "UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        
        if (args.length >= 1) {
            mainPage = args[0];
        }
        
        talkExecIn = config.getProperty("talkExec", "/usr/local/bin/open_jtalk");
        talkExec = config.getProperty("talkExec", "/usr/local/bin/open_jtalk");
        talkSh = config.getProperty("talkSh", "./talk.sh");
        talkEnExecIn = config.getProperty("talkEnExec", "/usr/local/bin/flite_hts_engine");
        talkEnExec = config.getProperty("talkEnExec", "/usr/local/bin/flite_hts_engine");
        talkEnSh = config.getProperty("talkEnSh", "./talkEn.sh");
        user = config.getProperty("user", "xxxx");
        pwd = "/home/" + user;
        
        //辞書ファイルの読み込み
        readDict();
        
        frmT.setVisible(true);
        
    }
    public static void writeProp() {
        //プロパティファイルの書き込み
        Properties config = new Properties();
//        Rectangle rect = eijiroFrm.getBounds();
//        config.setProperty("x", String.valueOf(rect.x));
//        config.setProperty("y", String.valueOf(rect.y));
//        config.setProperty("width", String.valueOf(rect.width));
//        config.setProperty("height", String.valueOf(rect.height));
        
        config.setProperty("talkExec", talkExecIn);
        config.setProperty("talkSh", talkSh);
        config.setProperty("talkEnExec", talkEnExecIn);
        config.setProperty("talkEnSh", talkEnSh);
        config.setProperty("user", user);
        
        try {
            config.store(new OutputStreamWriter(new FileOutputStream("JavaSh.properties"),"UTF-8"), "by TANAKA Hidemune");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void readDict() {
        Dict = new ArrayList<String[]>();
        ArrayList<String> wk = new ArrayList<String>();
        
        //ファイルの読み込み
        try {
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(new FileInputStream("Dict.csv"), "UTF-8"));
            // 最終行まで読み込む
            String line = "";
            while ((line = br.readLine()) != null) {
                    String[] str = line.toLowerCase().split(" ", 2);
                    //文字数チェック
                    int len = str[0].length();
                    DecimalFormat df4 = new DecimalFormat("0000");
                    wk.add(df4.format(len)+ " " + line.toLowerCase());
                }
            br.close();
            //文字数の多い順に並べ替え
            Object[] oa = wk.toArray(); // 配列に変換
            Arrays.sort(oa, new Comparator() {

                @Override
                public int compare(Object t, Object t1) {
                    String s1 = (String) t;
                    String s2 = (String) t1;
                    s1 = s1.toLowerCase();
                    s2 = s2.toLowerCase();
                    return s2.compareTo(s1);    //1と2を逆にしてみた
                }
            });
            //データ部
            for (int i = 0; i < oa.length; i++) {
                String[] str = ((String)oa[i]).split(" ", 3);
                String[] setStr = new String[2];
                setStr[0] = str[1].toLowerCase();
                setStr[1] = str[2];
                Dict.add(setStr);
            }
        } catch (Exception e) {
            // BufferedReaderオブジェクトのクローズ時の例外捕捉
            e.printStackTrace();
        }
    }
    public static String linkConv(String str, boolean lineControl) {
        System.err.println("linkConv:" + frmTerminal.linkLanstLineNo);
        System.out.println(str);
        //if (frmTerminal.yomiageWebMode) {
            //URL省略
            Pattern pattern3 = Pattern.compile("\\[\\[\\[.+?\\]\\]\\]");
            
            for (int j = 0; j < 10; j++) {
                Matcher matcherL = pattern3.matcher(str);
                if (matcherL.find()) {
                    String link = matcherL.group(0);
                    String wk = link.replaceAll("\\[\\[\\[", "").replaceAll("\\]\\]\\]", "");
                    System.err.println(wk);
//                    str = matcherL.replaceFirst(" F" + (j+1) + "Link ");
                    str = matcherL.replaceFirst(" F2Link ");
                    System.out.println(str + wk);
                    //最後に取得した行番号を取得
                    if (lineControl) {
                        url[j] = wk;
                        frmTerminal.linkLanstLineNo = frmT.getLineNo();
                    }
                } else {
                    //url[j] = "";
                }
            }
            //System.out.println(str);
        //}
        return str;
    }
    public static void talkNoWait(String str) {
        talk(str, false);
    }
    public static void talk(String str, boolean wait) {
        //System.out.println(str);
        if (talkExec == null) {
            return;
        }
        if (talkExec.equals("")) {
            return;
        }
        str = str.trim();
        if (str.trim().equals("")) {
            return;
        }
        //行の読み上げとは限らない
        str = linkConv(str, true);
        //言語判定
        String sh = talkSh;
        frmT.englishFlg = false;
        if (isEnglish(str)) {
            sh = talkEnSh;
            frmT.englishFlg = true;
        }
        
        if (!frmT.englishFlg) {
            //辞書の内容で置換
            str = str.toLowerCase();
            for (int i = 0; i < Dict.size(); i++) {
                if (Dict.get(i).length >= 2) {
    //                System.out.println("0:" + Dict.get(i)[0]);
    //                System.out.println("1:" + Dict.get(i)[1]);
                    str = str.replaceAll(Dict.get(i)[0], Dict.get(i)[1]);
                }
            }
        }
        
        //OpenJTalkインストール済みかチェック
        File file = new File(talkExec);
        if (!file.exists()){
            frmT.append("OpenJTalk is not installed.\naudio output is set to OFF.\n");
            talkExec = "";
            return;
        }
        File file2 = new File(talkEnExec);
        if (!file2.exists()){
            frmT.append("flite_hts_engine is not installed.\naudio output is set to OFF.\n");
            talkEnExec = "";
            return;
        }
        
        //タイムスタンプ
        long now = System.currentTimeMillis();
        
        Runtime r = Runtime.getRuntime();
        try {
            System.err.println(sh + ":" + str);
            Process proc = r.exec(new String[] { sh, Long.toString(now), str });
            //プロセス終了まで待ち
            if (wait) {
                InputStream is = proc.getInputStream();
                try {
                    while (is.read() != -1) {
                        //待ち
                        sleep(10);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(JavaSh.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    is.close();
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(JavaSh.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static boolean isEnglish(String str) {
        int en = 0;
        int jp = 0; 
        for (int i = 0; i < str.length(); i++) {
            if (isJapaneseChar(str.charAt(i))) {
                jp = jp + 1;
            } else {
                en = en + 1;
            }
        }
        if (jp >= 1) {
            return false;
        }
        return true;
    }
    public static boolean isJapaneseChar(char c) {
        if (c == '　') {
            return false;
        }
        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(c);

        if (Character.UnicodeBlock.HIRAGANA.equals(unicodeBlock))
            return true;

        if (Character.UnicodeBlock.KATAKANA.equals(unicodeBlock))
            return true;

        if (Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                .equals(unicodeBlock))
            return true;

        if (Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(unicodeBlock))
            return true;

        if (Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                .equals(unicodeBlock))
            return true;

        return false;
    }
    public static void talk(String str) {
        talk(str, true);
    }
    
    static class ExecThread extends Thread{
        boolean running = false;
        boolean tutorial = false;
        OutputStream osT;
        InputStream isT;
        InputStream esT;
        private String cmdT = "";
        private ProcessBuilder pb = null;
        public Process process = null;
        
        public void run() {
            exec(cmdT);
            //cmdT = "";
        }
        public void setCmd(String cmd) {
            cmdT = cmd;
        }
        public String getCmd() {
            return cmdT;
        }
        public void exec(String str) {

            String retLine = "";
            String msg = "";
            str = str.trim();
            if (str.trim().equals("")) {
                return;
            }
            //コマンド文字列の分割
            String[] cmdarg = split(str);

            running = true;
            try {
                //組み込みコマンドはここに記述
                if (cmdarg[0].equals("cd")) {
                    //ここでエラーが出なかったら初めてクリア
                    frmTerminal.yomiageWebMode = false;
                    frmT.clear();
                    String path = pwd;
                    if (cmdarg.length > 1) {
                        String pathPara = cmdarg[1];
                        //絶対パスの場合
                        if (pathPara.substring(0, 1).equals("/")) {
                            path = pathPara;
                        } else if (pathPara.equals("~")){
                            path = "/home/" + user;
                        } else {
                            path = pwd + "/" + pathPara;
                        }
                        //そのパスは、本当に実在するか、フォルダかどうかチェック。
                        File dir = new File(path);
                        if (!dir.exists()) {
                            msg = "Such a folder does not exist." + path;
                            frmT.append(msg);
                            frmT.append("\n");
                            JavaSh.talk(msg);
                            return;
                        }
                        if (!dir.isDirectory()) {
                            msg = "Not a directory." + path;
                            frmT.append(msg);
                            frmT.append("\n");
                            JavaSh.talk(msg);
                            return;
                        }
                        pwd = dir.getAbsolutePath();
                    }
                    frmT.append("It's all.\n");
                    frmT.setCaretPos(0);
                    return;
                } 
                if (cmdarg[0].startsWith("http")) {
                    // Web取得
                    NetClass net = new NetClass();
                    net.NetClass(cmdarg[0], frmT.getTextArea());
                    return;
                }
                if (cmdarg[0].startsWith("file")) {
                    // Web取得
                    NetClass net = new NetClass();
                    net.LocalClass(cmdarg[0], frmT.getTextArea());
                    return;
                }
                
                if (cmdarg[0].equals("yahoo")) {
                    // Web取得
                    NetClass net = new NetClass();
                    String para = cmdarg[1];
                    net.NetClass("http://search.yahoo.co.jp/search?p=" + para + "&search.x=1&fr=top_ga1_sa&tid=top_ga1_sa&ei=UTF-8&aq=&oq=", frmT.getTextArea());
                    return;
                }
                
                //必要なエイリアス
                String[] argAdd = {cmdarg[0]};  //Def
                if (cmdarg[0].equals("ls")) {
                    argAdd = new String[] {"ls", "-1", "-p", "-b"};
                }
                
                //size
                String[] args = new String[cmdarg.length + argAdd.length - 1];
                for (int i = 0; i < argAdd.length; i++) {
                    args[i] = argAdd[i];
                }
                //No.1 is alias
                for (int i = 1; i < cmdarg.length; i++) {
                    args[argAdd.length + i - 1] = cmdarg[i];
                }
                //System.err.println(args);
                
                //Linuxコマンドはここで実行
                pb = new ProcessBuilder(args);
                pb.directory(new File (pwd));
                process = pb.start();
                //process.waitFor();
                
                //ここでエラーが出なかったら初めてクリア
                frmTerminal.yomiageWebMode = false;
                frmT.clear();
                
                //StringBuilder sb = new StringBuilder();
                isT = process.getInputStream();
                osT = process.getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(isT));
                try {
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        //１行目のみ変数に退避
                        if (retLine.equals("")) {
                            retLine = line;
                        }
                        frmT.append(line);
                        frmT.append("\n");
                        frmT.repaintReq();
                        //talk(line);
                    }
                } finally {
                    br.close();
                    isT.close();
                    osT.close();
                    running = false;
                    frmT.append("It's all.\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                talk("Error");
                //frmT.append(ex.toString());
                //frmT.append("\n");
                //frmT.repaintReq();
                //talk(ex.toString());
                running = false;
                return;
            }
            frmT.setCaretPos(0);
        }
    }
    
    static class InputThread extends Thread{
        private String cmdT = "";
        public void run() {
            setInput(cmdT);
            cmdT = "";
        }
        public void setInput(String input) {
            try {
                if (frmT.execTrd.osT == null) {
                    return;
                }
                if (input == null) {
                    return;
                }
                frmT.execTrd.osT.write(input.trim().getBytes());
                frmT.execTrd.osT.write("\n".getBytes());
                frmT.execTrd.osT.flush();
            } catch (IOException ex) {
                Logger.getLogger(JavaSh.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void setKey(int input) {
            String hex = Integer.toHexString( (int)input );
            System.out.println("set:" + hex + "(" + String.valueOf(input) + ")");
            
            try {
                if (frmT.execTrd.osT == null) {
                    return;
                }
                frmT.execTrd.osT.write(input);
                frmT.execTrd.osT.flush();
                //System.err.println("setKey:" + (int)input);
            } catch (IOException ex) {
                Logger.getLogger(JavaSh.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    static class ErrorThread extends Thread{
        private String cmdT = "";
        public void run() {
            setOutput();
        }
        public void setOutput() {
            try {
                if (frmT.execTrd.esT == null) {
                    return;
                }
                //frmT.ttyTrd.esT = process.getErrorStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(frmT.execTrd.esT));
                
                String line = null;
                while (true) {
                    try {
                        line = br.readLine();
                    } catch (Exception e) {
                        line = "";
                    }
                    if (!line.equals("")) {
                        frmT.append(line);
                        frmT.append("\n");
                        frmT.repaintReq();
                        talk(line);
                    }
                    if (cmdT.equals("stop")) {
                        break;
                    }
                }
                
                br.close();
                frmT.execTrd.esT.close();
                System.out.println("ErrorThread has ended.");
            } catch (IOException ex) {
                ex.printStackTrace();
                talk("Error");
                frmT.append(ex.toString());
                frmT.append("\n");
                frmT.repaintReq();
                talk(ex.toString());
            }
        }
    }
    public static String[] split(String str) {
        
        ArrayList al = new ArrayList();
        StringBuilder sb = new StringBuilder();
        boolean flgEsc = false;
        
        for (int i = 0; i < str.length(); i++) {
            String wk = str.substring(i, i + 1);
            if (wk.equals("\\")) {
                flgEsc = true;
            } else {
                if (flgEsc) {
                    sb.append(wk);
                } else {
                    if (wk.equals(" ")) {
                        //分割
                        al.add(sb.toString());
                        sb = new StringBuilder();
                    } else {
                        sb.append(wk);
                    }
                }
                flgEsc = false;
            }
        }
        al.add(sb.toString());
        
        String[] ret = (String[]) al.toArray(new String[0]);
        return ret;
    }
    /*
    class MyTty implements Tty{

        @Override
        public boolean init(Questioner q) {
            frmT.append("init.");
            return true;
        }

        @Override
        public void close() {
            frmT.append("close.");
        }

        @Override
        public void resize(Dimension termSize, Dimension pixelSize) {
            frmT.append("termSize." + termSize.width + "," + termSize.height);
            frmT.append("pixelSize." + pixelSize.width + "," + pixelSize.height);
            
        }

        @Override
        public String getName() {
            return "JavaSh";
        }

        @Override
        public int read(byte[] buf, int offset, int length) throws IOException {
            frmT.append("read.");
            while(frmT.ttyTrd.cmdT.equals("")) {
                try {
                    sleep(10);
                } catch (InterruptedException ex) {
                    throw new IOException(ex.toString());
                }
            }
            buf = frmT.ttyTrd.cmdT.getBytes();
            offset = 0;
            length = buf.length;
            return 0;
        }

        @Override
        public void write(byte[] bytes) throws IOException {
            frmT.append(bytes.toString());
        }
        
    }
    class MyQuestioner implements Questioner {

        @Override
        public String questionVisible(String question, String defValue) {
            frmT.append(question);
            while(frmT.ttyTrd.cmdT.equals("")) {
                try {
                    sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JavaSh.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return frmT.ttyTrd.cmdT;
        }

        @Override
        public String questionHidden(String string) {
            frmT.append(string);
            while(frmT.ttyTrd.cmdT.equals("")) {
                try {
                    sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JavaSh.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return frmT.ttyTrd.cmdT;
        }

        @Override
        public void showMessage(String message) {
            frmT.append(message);
        }
        
    }
    */
}
