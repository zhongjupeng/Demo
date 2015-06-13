package com.zjp.weixinsdk;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.tencent.mm.sdk.openapi.GetMessageFromWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String APP_ID = "";
    private IWXAPI api;
    private Button regBt,sendReqBt,sendRespBt;
    private String text = "Hello world";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regBt = (Button)findViewById(R.id.regBt);
        sendReqBt = (Button)findViewById(R.id.sendReqBt);
        sendRespBt = (Button)findViewById(R.id.sendRespBt);
        regBt.setOnClickListener(this);
        sendReqBt.setOnClickListener(this);
        sendRespBt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
       int id = v.getId();
        switch (id){
            case R.id.regBt:
                regToWx();
                break;
            case R.id.sendReqBt:
                sendReqToWx();
                break;
            case R.id.sendRespBt:
                sendRespToWX();
                break;

        }
    }

    private void regToWx(){
        //通过WXPAIFactory工厂获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this,APP_ID,true);

        //将应用的appId注册到微信
        api.registerApp(APP_ID);
    }

    //sendReq是第三方app主动发送消息给微信，发送完成之后会切回到第三方app界面
    private void sendReqToWx(){

        //初始化一个WXtextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        //用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis()); //用于唯一标识一个请求
        req.message = msg;

        //调用api接口发送数据到微信
        api.sendReq(req);
    }

    //sendResp是微信向第三方app请求数据，第三方app回应数据之后会切回到微信界面。
    private void sendRespToWX(){

        //初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        //用WXTextObject对象初始化一个WXmediaMessage对像
        WXMediaMessage msg = new WXMediaMessage(textObj);
        msg.description = text;

        //构建Resp
        GetMessageFromWX.Resp resp = new GetMessageFromWX.Resp();
        //将req的transaction设置到resp对象中，其中bundle为微信传递过来的intent所带的内容，通过getExtras方法获得
        //resp.transaction = new GetMessageFromWX.Req(bundle).transaction;
        resp.transaction = new GetMessageFromWX.Req().transaction;
        resp.message = msg;

        //调用api接口响应数据到微信
        api.sendResp(resp);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将应用的appId从微信中注销
        api.unregisterApp();
    }
}
