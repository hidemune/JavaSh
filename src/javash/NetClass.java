/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javash;

import java.net.*;
import java.io.*;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 *
 * @author hdm
 */
public class NetClass {


    public NetClass() {
    }
    /*
    public void Set(String urls, JTextPane textP) {
        urlC = urls;
        textPC = textP;
    }
    */
    
  public void NetClass(String urls, JTextPane textP){

    try{ //概ねの操作で例外処理が必要です。
       //URLを作成する
       URL url=new URL(urls);//URLを設定

         // URL接続
        HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
          connect.setRequestMethod("GET");//プロトコルの設定
          InputStream in=connect.getInputStream();//ファイルを開く
          
          StringBuilder sb = new StringBuilder();
          // ネットからデータの読み込み
          String str;//ネットから読んだデータを保管する変数を宣言
          str=readString(in);//1行読み取り
          textP.setContentType( "text/html" );
          textP.setEditable(false);
          //HTMLDocument doc = (HTMLDocument)textP.getDocument();
          HTMLEditorKit editorKit = (HTMLEditorKit)textP.getEditorKit();
          
          StyledDocument styleDoc = textP.getStyledDocument();
          HTMLDocument doc = (HTMLDocument)styleDoc;
          
          
          while (str!=null) {//読み取りが成功していれば
              System.out.println(str);
              try {
                  Element last = doc.getParagraphElement(doc.getLength());
                  doc.insertBeforeEnd(last, str);
              } catch (Exception ex) {
              }
//                editorKit.insertHTML(doc, doc.getLength(), str, 0, 0, null);
              str=readString(in);//次を読み込む
          }
          textP.setDocument(doc);
          
          System.err.println(textP.getDocument().getText(0, textP.getDocument().getLength()));
          // URL切断
          in.close();//InputStreamを閉じる
          connect.disconnect();//サイトの接続を切断
          
          //textP.setText(sb.toString());
          
          
    }catch(Exception e){
      //例外処理が発生したら、表示する
      System.out.println("Err ="+e);
    }
  }
  public void NetClass(String urls, JTextArea textP){

      //読み上げモード切り替え
      frmTerminal.yomiageWebMode = true;
      
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
            frmTerminal.urlDir = url.toString();
            frmTerminal.urlDir = frmTerminal.urlDir.replaceFirst("/[a-zA-Z]+?\\.htm.*", "");
        } else {
            wk = frmTerminal.urlDir + "/" + urls;
        }
        //http 付加
//        if (!wk.startsWith("http")) {
//            wk = "http://"  + wk;
//        }
        url=new URL(wk);
        System.err.println("ToStr:" + url.toString());
        // URL接続
        HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
          connect.setRequestMethod("GET");//プロトコルの設定
          InputStream in=connect.getInputStream();//ファイルを開く
          
          StringBuilder sb = new StringBuilder();
          // ネットからデータの読み込み
          String str;//ネットから読んだデータを保管する変数を宣言
          str=readString(in);//1行読み取り
          flg = false;
          while (str!=null) {//読み取りが成功していれば
              //System.out.println(str);
              
              /* タグの削除
               * <!--　から　--> までの、全ての行を削除
               */
              
              if (str.indexOf("<!--") >= 0) {
                  flg = true;
              }
              if (str.indexOf("-->") >= 0) {
                  flg = false;
                  str = str.replaceAll(".+?-->", "");
              }
              /* タグの削除
               * 1行中の、　<　から　> までを削除
               * 但し、Aタグだけはhref文字列を取得
               */
              
              str = replaceTag(str);
              
              if ((!flg) && (!str.trim().equals(""))) {
                  sb.append(str);
                  sb.append("\n");
              }
              
              str=readString(in);//次を読み込む
          }
          
          // URL切断
          in.close();//InputStreamを閉じる
          connect.disconnect();//サイトの接続を切断
          
          textP.setText(sb.toString());
          textP.setCaretPosition(0);
          
    }catch(Exception e){
      //例外処理が発生したら、表示する
      e.printStackTrace();
    }
  }
  
  private String replaceTag(String str) {
      StringBuilder ret = new StringBuilder();
      
      str = str.replaceAll("&nbsp;", " ");
      str = str.replaceAll("&quot;", "'");
      
      String url = "";
      String mode = "out";
      for (int i = 0; i < str.length(); i++) {
          String c = str.substring(i, i + 1);
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
                System.out.println(wk);
                String[] arr = wk.split("[\\s\\=]+");
                System.err.println(arr[0]);
                if (arr[0].toLowerCase().equals("a")) {
                    mode = "a";
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
                      mode = "";
                  }
                  if ((mode.equals("a")) && (arr[j].toLowerCase().equals("href"))) {
                      mode = "href";
                  }
                }
                i = ed;
              } catch (Exception e) {
                  e.printStackTrace();
              }

          }
          if (mode.equals("out")) {
            ret.append(c);
          }
          if (!url.equals("")) {
            ret.append("[" + url.replaceAll("[\"\']", "") + "]");
          }
          mode = "out";
          url = "";
      }
      
      return ret.toString();
  }
  
  //InputStreamより１行だけ読む（読めなければnullを返す）
  static String readString(InputStream in){
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
      return new String(b,0,l);//文字列に変換
    }catch(IOException e){
      //Errが出たら、表示してnull値を返す
      System.out.println("Err="+e);
      return null;
    }
  }
}



