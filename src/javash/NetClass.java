/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javash;

import java.net.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;

/**
 *
 * @author hdm
 */
public class NetClass {
    String mode = "out";
    String rangemode = "out";
    String tagname = "";
    String title = "";
    
    public NetClass() {
    }
    /*
    public void Set(String urls, JTextPane textP) {
        urlC = urls;
        textPC = textP;
    }
    */
    
  public void NetClass(String urls, JTextArea textP){

      //読み上げモード切り替え
      frmTerminal.yomiageWebMode = true;
      //行番号クリア
      frmTerminal.linkLanstLineNo = -100;
      
    try{ //概ねの操作で例外処理が必要です。
       //URLを作成する
       URL url = null;
       String wk = "";
       
        System.err.println("urlDir:" + frmTerminal.urlDir);
        System.err.println("urlpath:" + urls);
        //ドメイン情報が含まれていたら保存
        String[] dom = {"http:", "https:", "\\.co\\.jp", "\\.com"};
        boolean flg = false;
        for (int i = 0; i < dom.length; i++) {
            if (urls.contains(dom[i])) {
                flg = true;
                break;
            }
        }
        if (flg) {
            wk = urls;      //URLを設定
            url=new URL(wk);
            frmTerminal.urlDir = url.getProtocol() + "://" + url.getHost() + url.getPath();
            frmTerminal.urlRoot = url.getProtocol() + "://" + url.getHost();
        } else {
            //１文字目が/の場合、ルートからの絶対パス
            if (urls.startsWith("/")) {
                wk = frmTerminal.urlRoot + urls;
            } else {
                wk = frmTerminal.urlDir + "/" + urls;
            }
        }
        url=new URL(wk);
        frmTerminal.urlDir = wk.replaceFirst("/[a-zA-Z0-9_\\-]+?\\.html*#*.*$", "");
        //frmTerminal.urlDir = 
        frmTerminal.urlRireki.add(wk.toString());
        
        System.err.println("RirekiAdd:" + url.toString());
        // URL接続
        HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
          connect.setRequestMethod("GET");//プロトコルの設定
          InputStream in=connect.getInputStream();//ファイルを開く
          
          StringBuilder sb = new StringBuilder("\n");
          // ネットからデータの読み込み
          String str;//ネットから読んだデータを保管する変数を宣言
          str=readString(in);//1行読み取り
          flg = false;
          while (str!=null) {//読み取りが成功していれば
              //System.out.println(str);
              
              if (!str.trim().equals("")) {
                  sb.append(str);
                  //sb.append("\n");
              }
              
              str=readString(in);//次を読み込む
          }
          
          // URL切断
          in.close();//InputStreamを閉じる
          connect.disconnect();//サイトの接続を切断
          
        /* タグの削除
         * 1行中の、　<　から　> までを削除
         * 但し、Aタグだけはhref文字列を取得
         */
        /* タグの削除
         * <!--　から　--> までの、全ての行を削除
         */

        str = null;
        while (true) {
            int sta = sb.toString().indexOf("<!--");
            int ed = sb.toString().indexOf("-->", sta);
            if ((0 <= sta) && (sta < ed)) {
                sb.delete(sta, ed + 3);
            } else {
                break;
            }
        }
        while (true) {
            int sta = sb.toString().indexOf("<!");
            int ed = sb.toString().indexOf(">", sta);
            if ((0 <= sta) && (sta < ed)) {
                sb.delete(sta, ed + 1);
            } else {
                break;
            }
        }
        str = sb.toString();
        str = replaceTag(str);
        
        //全行トリム(全角スペースも)
        sb = new StringBuilder();
        String[] strs = str.split("\n");
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i].replaceAll("[ 　]+$",""));
            sb.append("\n");
        }
        str = sb.toString();
        
        //丸で改行
        str = str.replaceAll("。", "。\n");
        //2つ以上の連続した改行は1つに設定
        str = str.replaceAll("\r", "");
        str = str.replaceAll("\n\n*", "\n");
        
        java.awt.Toolkit.getDefaultToolkit().beep();
        textP.setText("\n" + str + "\n以上です。\n");
        textP.setCaretPosition(0);
        
    }catch(Exception e){
      //例外処理が発生したら、表示する
      e.printStackTrace();
    }
  }
  
  public void LocalClass(String urls, JTextArea textP){

      //読み上げモード切り替え
      frmTerminal.yomiageWebMode = true;
      //行番号クリア
      frmTerminal.linkLanstLineNo = -100;
      
    try{ //概ねの操作で例外処理が必要です。
       //URLを作成する
       URL url = null;
       String wk = "";
       
        System.err.println("urlDir:" + frmTerminal.urlDir);
        System.err.println("urlpath:" + urls);
        //ドメイン情報が含まれていたら保存
        String[] dom = {"file:"};
        boolean flg = false;
        for (int i = 0; i < dom.length; i++) {
            if (urls.contains(dom[i])) {
                flg = true;
                break;
            }
        }
        if (flg) {
            wk = urls;      //URLを設定
            url=new URL(wk);
            frmTerminal.urlDir = url.getProtocol() + "://" + url.getHost() + url.getPath();
            frmTerminal.urlRoot = url.getProtocol() + "://" + url.getHost();
        } else {
            //１文字目が/の場合、ルートからの絶対パス
            if (urls.startsWith("/")) {
                wk = frmTerminal.urlRoot + urls;
            } else {
                wk = frmTerminal.urlDir + "/" + urls;
            }
        }
        url=new URL(wk);
        frmTerminal.urlDir = wk.replaceFirst("/[a-zA-Z0-9_\\-]+?\\.html*#*.*$", "");
        //frmTerminal.urlDir = 
        frmTerminal.urlRireki.add(wk.toString());
        
        System.err.println("RirekiAdd:" + url.toString());
        // URL接続
        //HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
        //  connect.setRequestMethod("GET");//プロトコルの設定
          InputStreamReader in = new InputStreamReader(url.openStream());//ファイルを開く
          BufferedReader br = new BufferedReader(in);
          
          StringBuilder sb = new StringBuilder("\n");
          // ネットからデータの読み込み
          String str;//ネットから読んだデータを保管する変数を宣言
          str=br.readLine();//1行読み取り
          flg = false;
          while (str!=null) {//読み取りが成功していれば
              //System.out.println(str);
              
              if (!str.trim().equals("")) {
                  sb.append(str);
                  //sb.append("\n");
              }
              
              str=br.readLine();//次を読み込む
          }
          
          // URL切断
          in.close();//InputStreamを閉じる
          //connect.disconnect();//サイトの接続を切断
          
        /* タグの削除
         * 1行中の、　<　から　> までを削除
         * 但し、Aタグだけはhref文字列を取得
         */
        /* タグの削除
         * <!--　から　--> までの、全ての行を削除
         */

        str = null;
        while (true) {
            int sta = sb.toString().indexOf("<!--");
            int ed = sb.toString().indexOf("-->", sta);
            if ((0 <= sta) && (sta < ed)) {
                sb.delete(sta, ed + 3);
            } else {
                break;
            }
        }
        while (true) {
            int sta = sb.toString().indexOf("<!");
            int ed = sb.toString().indexOf(">", sta);
            if ((0 <= sta) && (sta < ed)) {
                sb.delete(sta, ed + 1);
            } else {
                break;
            }
        }
        str = sb.toString();
        str = replaceTag(str);
        
        //全行トリム(全角スペースも)
        sb = new StringBuilder();
        String[] strs = str.split("\n");
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i].replaceAll("[ 　]+$",""));
            sb.append("\n");
        }
        str = sb.toString();
        
        //丸で改行
        str = str.replaceAll("。", "。\n");
        //2つ以上の連続した改行は1つに設定
        str = str.replaceAll("\r", "");
        str = str.replaceAll("\n\n*", "\n");
        
        java.awt.Toolkit.getDefaultToolkit().beep();
        textP.setText("\n" + str + "\n以上です。\n");
        textP.setCaretPosition(0);
        
    }catch(Exception e){
      //例外処理が発生したら、表示する
      e.printStackTrace();
    }
  }

  private String replaceTag(String str) {
      StringBuilder ret = new StringBuilder();
      StringBuilder sbwk = new StringBuilder();
      
      if (rangemode.equals("")) {
        rangemode = "out";
      }
      if (mode.equals("")) {
        mode = "out;";
      }
      //    
      str = str.replaceAll("&nbsp;", " ");
      str = str.replaceAll("&quot;", "'");
      str = str.replaceAll("&amp;", "&");
      
      String url = "";
      
      for (int i = 0; i < str.length(); i++) {
          String c = str.substring(i, i + 1);
          if (c.equals(">")) {
              mode = "tagend";
          }
          if (c.equals("<")) {
            try {
                mode = "tag";
                int ed = str.indexOf(">", i);
                String wk;
                if (ed < 0) {
                    wk = str.substring(i);
                    break;
                } else {
                    wk = str.substring(i, ed);
                }
                wk = wk.replaceFirst("<", "");
                //System.out.println(wk);
                String[] arr = wk.split("[\\s\\=]+");
                tagname = arr[0].toLowerCase();
                
                if (arr[0].toLowerCase().equals("a")) {
                    mode = "a";
                }
//                if (arr[0].toLowerCase().equals("/a")) {
//                    mode = "br";
//                }
                if (arr[0].toLowerCase().equals("br")) {
                    mode = "br";
                    //mode = "";  これはさすがに無理がある（。以外で改行しないという処理）
                }
                if (arr[0].toLowerCase().equals("p")) {
                    mode = "br";
                }
                if (arr[0].toLowerCase().equals("/p")) {
                    mode = "br";
                }
                if (arr[0].toLowerCase().equals("td")) {
                    mode = "br";
                }
                if (arr[0].toLowerCase().equals("tr")) {
                    mode = "br";
                }
                if (arr[0].toLowerCase().equals("dt")) {
                    mode = "br";
                }
                if (arr[0].toLowerCase().equals("dd")) {
                    mode = "br";
                }
                //b は、ただの大文字指定 何もしない
                if (arr[0].toLowerCase().equals("b")) {
                    mode = "tagend";
                }
                if (arr[0].toLowerCase().equals("/b")) {
                    mode = "tagend";
                }
                if (arr[0].toLowerCase().equals("li")) {
                    mode = "br";
                }
                if (arr[0].toLowerCase().equals("td")) {
                    mode = "br";
                }
                if (arr[0].toLowerCase().equals("tr")) {
                    mode = "br";
                }
                if (arr[0].toLowerCase().equals("div")) {
                    mode = "br";
                }
                if (arr[0].toLowerCase().equals("/div")) {
                    mode = "br";
                }
                if (arr[0].toLowerCase().equals("span")) {
                    mode = "out";
                }
                if (arr[0].toLowerCase().equals("style")) {
                    rangemode = "del";
                }
                if (arr[0].toLowerCase().equals("/style")) {
                    rangemode = "out";
                }
                if (arr[0].toLowerCase().equals("script")) {
                    rangemode = "del";
                }
                if (arr[0].toLowerCase().equals("/script")) {
                    rangemode = "out";
                }
                if (arr[0].toLowerCase().equals("title")) {
                    rangemode = "get";
                }
                if (arr[0].toLowerCase().equals("/title")) {
                    rangemode = "getend";
                }
                for (int j = 0; j < arr.length; j++) {
                  if ((mode.equals("href"))) {
                      System.err.println(wk);
                      int staH = wk.toLowerCase().indexOf("href") + 4;
                      int endH = wk.length();
                      int endW = wk.length();
                      endW = wk.indexOf(" ", staH);
                      if ((0 < endW) && (endW < endH)) {
                          endH = endW;
                      }
                      endW = wk.indexOf(">", staH);
                      if ((0 < endW) && (endW < endH)) {
                          endH = endW;
                      }
                      url = wk.substring(staH, endH);
                      url = url.replaceFirst("=", "");
                      mode = "tagend";
                  }
                  if ((mode.equals("a")) && (arr[j].toLowerCase().equals("href"))) {
                      mode = "href";
                  }
                }
                i = ed;
                if (mode.equals("tag")) {
                    System.err.println("未処理？：" + arr[0]);
                    mode = "tagend";
                }
              } catch (Exception e) {
                  e.printStackTrace();
              }

          }
          if (c.equals("\n")) {
              mode = "";
          }
          if (c.equals("\r")) {
              mode = "";
          }
          //出力処理
          if (!c.equals("<") && (rangemode.equals("get"))) {
            ret.append(c);
            sbwk.append(c);
            mode = "out";
          }
          if (!c.equals("<") && ((rangemode.equals("out")) && (mode.equals("out")))) {
            ret.append(c);
          }
          
          //タグの終了が観測できたら元に戻す
          if (mode.equals("tagend")) {
            mode = "out";
          }
          if (rangemode.equals("getend")) {
            //値を取得
              System.err.println("get:" + tagname + ":" + sbwk.toString());
              if (tagname.equals("/title")) {
                  title = sbwk.toString();
                  JavaSh.talkNoWait(title);
              }
              sbwk = new StringBuilder();
            //モードを戻さないといけない
            rangemode = "out";
            mode = "out";
          }
          if (mode.equals("br")) {
            ret.append('\n');
            mode = "out";
          }
          if (!url.equals("")) {
            ret.append("\n[[[" + url.replaceAll("[\"\']", "") + "]]]");
          }
          if (rangemode.equals("out")) {
              //範囲指定の条件が無ければ出力モードに
              mode = "out";
          }
          url = "";
      }
      
      return ret.toString();
  }
  
  //InputStreamより１行だけ読む（読めなければnullを返す）
  static String readString(InputStream in){
    String charset = "JISAutoDetect";     //初期値
    Pattern pattern3 = Pattern.compile("content=[\"\'][.*]charset=[.+?][\"\']>");
    try{
      int l;//呼んだ長さを記録
      int a;//読んだ一文字の記録に使う
      byte b[]=new byte[20480];//呼んだデータを格納
      a=in.read();//１文字読む
      if (a<0) return null;//ファイルを読みっていたら、nullを返す
      l=0;
      while(a>10){//行の終わりまで読む
        if (a>=' '){//何かの文字であれば、バイトに追加
          b[l]=(byte)a;
          l++;
        }
        a=in.read();//次を読む
      }
      String utf8 = new String(b,0,l);//文字列に変換
      String sjis = new String(b,0,l, charset);//文字列に変換 JISAutoDetect
      //System.err.println("utf8:" + utf8 + "  jis:" + sjis);
      Matcher matcherL = pattern3.matcher(utf8);
      if (matcherL.find()) {
          charset = matcherL.group(2);
      }
      int u = utf8.length();
      //int e = eucjp.length();
      int s = sjis.length();
      //System.err.println("utf8:" + utf8 + "  euc:" + eucjp + "  sjis:" + sjis);
      //System.err.println("utf8:" + u + "  euc:" + e + "  sjis:" + s);
        if (u <= s) {
            return utf8;
        } else {
            return sjis;
        }
    }catch(IOException e){
      //Errが出たら、表示してnull値を返す
      System.out.println("Err="+e);
      return null;
    }
  }
}



