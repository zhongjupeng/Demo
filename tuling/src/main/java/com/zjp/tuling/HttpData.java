package com.zjp.tuling;


import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpData extends AsyncTask<String,Void,String>{

    private HttpClient mHttpClick;
    private HttpGet mHttpGet;
    private HttpResponse mHttpRespose;
    private HttpEntity mHttpEntity;
    private InputStream in;
    private GetDataListener getDataListener;
    private Context context;

    private String url;
    public HttpData(String url,GetDataListener getDataListener,Context context) {
        this.url = url;
        this.getDataListener = getDataListener;
        this.context = context;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        int who = ((MainActivity)context).getWho();
        getDataListener.getDataUrl(s,who);
    }

    @Override
    protected String doInBackground(String... params) {
       try{
           mHttpClick = new DefaultHttpClient();
           mHttpGet = new HttpGet(url);
           mHttpRespose = mHttpClick.execute(mHttpGet);
           mHttpEntity = mHttpRespose.getEntity();
           if(((MainActivity)context).getWho() == MainActivity.FYJ){
               return EntityUtils.toString(mHttpEntity);
           }else{
               in = mHttpEntity.getContent();
               BufferedReader br =new BufferedReader(new InputStreamReader(in));
               String line;
               StringBuffer sb = new StringBuffer();
               if((line = br.readLine())!=null){
                   sb.append(line);
               }
               return sb.toString();
           }

       }catch (Exception e){
           e.printStackTrace();
       }
        return null;
    }

}
