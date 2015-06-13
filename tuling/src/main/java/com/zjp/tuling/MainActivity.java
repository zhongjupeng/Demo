package com.zjp.tuling;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends ActionBarActivity implements GetDataListener,View.OnClickListener{

    ActionBar actionBar;
    private HttpData mHttpData;
    private List<ListData> lists;
    private EditText etSend;
    private ImageButton btSend;
    private ListView listView;
    private MyAdapter adapter;
    private String sendContent;
    private String array[];
    private ListData list;
    private double curTime = 0;
    private double oldTime = 0;
    private int who = 1;
    public static final int LY = 1;
    public static final int LZL = 2;
    public static final int ZJY = 3;
    public static final int CJK = 4;
    public static final int FYJ = 5;
    private List<ListData1> listsData;
    private ListData1 listData1;
//    private int im[] = {R.drawable.ly,R.drawable.zjy,R.drawable.lzl,R.drawable.cjk};
//    private String str[] = {"柳岩","张钧韵","林志玲","小仓"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);  应用程序设置为可点击按钮
        //设置图标为可点击的按钮，并且添加想左的按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        //设置是否显示应用程序的图标
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.ly);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//        listsData = new ArrayList<ListData1>();
//        for(int i=0;i<4;i++){
//            listData1 = new ListData1(im[i],str[i]);
//            listsData.add(listData1);
//        }
//        MyListAdapter adapter1 = new MyListAdapter(this,listsData);
//        actionBar.setListNavigationCallbacks(adapter1,new ActionBar.OnNavigationListener() {
//            @Override
//            public boolean onNavigationItemSelected(int i, long l) {
//               // Toast.makeText(MainActivity.this,i,Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        initView();
    }

    private void initView() {
        etSend = (EditText)findViewById(R.id.sendText);
        btSend = (ImageButton)findViewById(R.id.bt_send);
        listView = (ListView)findViewById(R.id.listView);
        btSend.setOnClickListener(this);
        list = new ListData(RandomWelcom(R.string.liuyan),ListData.RECEVE,ListData.CODE_TEXT,getTime(),1);
        lists = new ArrayList<ListData>();
        lists.add(list);
        adapter = new MyAdapter(this,lists);
        listView.setAdapter(adapter);
    }

    @Override
    public void getDataUrl(String result,int who) {
        if(who == FYJ){
           Log.i("zjp", result);
           parseXMLText(result,who);
        }else
           parseJSONText(result,who);

    }

    //根据不同的返回码，来解析XML数据
    private void parseXMLText(String str,int who){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例
            DocumentBuilder builder = factory.newDocumentBuilder(); //从factory获取DocumentBuilder实例
            Log.i("zjp","dddddd");
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(str));
            Document doc = builder.parse(is);   //解析输入流 得到Document实例
            Log.i("zjp","cccccc");
            Element rootElement = doc.getDocumentElement();
            //int errorCode= Integer.parseInt(rootElement.getChildNodes().item(0).getNodeValue());
            //String paragraph = rootElement.getChildNodes().item(2).getFirstChild().getNodeValue();
            NodeList item1 = rootElement.getElementsByTagName("errorCode");
            NodeList item2 = rootElement.getElementsByTagName("translation");
            Log.i("zjp","bbbbb");
            int errorCode = Integer.parseInt(((Element)item1.item(0)).getChildNodes().item(0).getNodeValue());
            Log.i("zjp","bbbbb");
            Log.i("zjp",errorCode + "bbbbb");
            String paragraph = ((Element)item2.item(0)).getElementsByTagName("paragraph").item(0).getFirstChild().getNodeValue();
            switch (errorCode){
               // 0 - 正常
                case 0:

                    list = new ListData(paragraph,ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
               // 　20 - 要翻译的文本过长
                case 20:
                    list = new ListData("要翻译的文本过长",ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
               // 　30 - 无法进行有效的翻译
                case 30:
                    list = new ListData("无法进行有效的翻译",ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
               // 　40 - 不支持的语言类型
                case 40:
                    list = new ListData("不支持的语言类型",ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
               // 　50 - 无效的key
                case 50:
                    list = new ListData("无效的key",ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
               // 　60 - 无词典结果，仅在获取词典结果生效
                case 60:
                    list = new ListData("无词典结果，仅在获取词典结果生效",ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
                default:

                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }
   //根据不同的返回码，来确定如何解析JSON数据
    private void parseJSONText(String str,int who) {
        try {
            JSONObject jb = new JSONObject(str);
            int code = jb.getInt("code");
            switch (code){
                //文本类数据
                case 100000:
                    list = new ListData(jb.getString("text"),ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
                //列车查询
                case 305000:
                    JSONArray ls = jb.getJSONArray("list");
                    JSONObject info = ls.getJSONObject(0);
                    list = new ListData(jb.getString("text"),info.getString("detailurl"),ListData.RECEVE,ListData.CODE_TRAIN,getTime(),who);
                    lists.add(list);
                    break;
                //航班查询
                case 306000:
                    JSONArray ls1 = jb.getJSONArray("list");
                    JSONObject info1 = ls1.getJSONObject(0);
                    list = new ListData(jb.getString("text"),info1.getString("detailurl"),ListData.RECEVE,ListData.CODE_AIRPLANE,getTime(),who);
                    lists.add(list);
                    break;
                //网址类数据
                case 200000:
                    list = new ListData(jb.getString("text"),jb.getString("url"),ListData.RECEVE,ListData.CODE_LINK,getTime(),who);
                    lists.add(list);
                    break;
                //新闻类
                case 302000:
                    JSONArray ls2 = jb.getJSONArray("list");
                    JSONObject info2 = ls2.getJSONObject(0);
                    list = new ListData(jb.getString("text"),info2.getString("detailurl"),ListData.RECEVE,ListData.CODE_NEW,getTime(),who);
                    lists.add(list);
                    break;
                //菜谱、视频、小说类
                case 308000:
                    JSONArray ls3 = jb.getJSONArray("list");
                    JSONObject info3 = ls3.getJSONObject(0);
                    list = new ListData(jb.getString("text"),info3.getString("detailurl"),ListData.RECEVE,ListData.CODE_MENU,getTime(),who);
                    lists.add(list);
                    break;
                //40001 key的长度错误（32位）
                case 40001:
                    String tips = "key的长度错误";
                    list = new ListData(tips,ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
                //40002	 请求内容为空
                case 40002:
                    String tips1 = "请求内容为空";
                    list = new ListData(tips1,ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
                //40003	 key错误或帐号未激活
                case 40003:
                    String tips2 = "key错误或帐号未激活";
                    list = new ListData(tips2,ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
                //40004	 当天请求次数已用完
                case 40004:
                    String tips3 = "当天请求次数已用完";
                    list = new ListData(tips3,ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
                //40005	 暂不支持该功能
                case 40005:
                    String tips4 = "暂不支持该功能";
                    list = new ListData(tips4,ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
                //40006	 服务器升级中
                case 40006:
                    String tips5 = "服务器升级中";
                    list = new ListData(tips5,ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
                default:
                    list = new ListData("客观不懂你说什么？",ListData.RECEVE,ListData.CODE_TEXT,getTime(),who);
                    lists.add(list);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        sendContent = etSend.getText().toString();
        lists.add(new ListData(sendContent,ListData.SEND,ListData.CODE_TEXT,getTime(),0));
        sendContent=sendContent.replace(" ","");
        sendContent=sendContent.replace("\n","");
        if(lists.size()>30){
            lists.remove(0);
        }
        adapter.notifyDataSetChanged();
        if(this.getWho() == FYJ){
            mHttpData = (HttpData) new HttpData("http://fanyi.youdao.com/openapi.do?keyfrom=zjppppp2&key=773952754&type=data&doctype=xml&version=1.1&q=" + sendContent,
                    this, this).execute();
        }else {
            mHttpData = (HttpData) new HttpData("http://www.tuling123.com/openapi/api?key=5a69788fb69ff8552d81e9345f151d00&info=" + sendContent,
                    this, this).execute();
        }
        etSend.setText("");
    }

    private String RandomWelcom(int str){
        /*String str = null;
        array = getResources().getStringArray(R.array.welcome);
        int index = (int)(Math.random()*(array.length - 1));
        str = array[index];
        return str;*/
        return getResources().getString(str);

    }

    private String getCurTime(){
        curTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = simpleDateFormat.format(curDate);
        return str;
    }
    private String getTime(){
        curTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = simpleDateFormat.format(curDate);
        if(curTime - oldTime>=5*60*1000){
            oldTime = curTime;
            return str;
        }else
            return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setIconEnable(menu,true);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
//        MenuItem item1 = menu.add(0,1,0,R.string.ly);
//        item1.setIcon(R.drawable.ly);
//        item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_ly:
                actionBar.setTitle(R.string.ly);
                actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.first));
                this.setWho(LY);
                lists.clear();
                list = new ListData(RandomWelcom(R.string.liuyan),ListData.RECEVE,ListData.CODE_TEXT,getCurTime(),this.getWho());
                break;
            case R.id.action_lzl:
                actionBar.setTitle(R.string.lzl);
                actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.second));
                this.setWho(LZL);
                lists.clear();
                list = new ListData(RandomWelcom(R.string.linzhiling),ListData.RECEVE,ListData.CODE_TEXT,getCurTime(),this.getWho());
                break;
            case R.id.action_zjy:
                actionBar.setTitle(R.string.zjy);
                actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.third));
                this.setWho(ZJY);
                lists.clear();
                list = new ListData(RandomWelcom(R.string.zhangjunning),ListData.RECEVE,ListData.CODE_TEXT,getCurTime(),this.getWho());
                break;
            case R.id.action_xc:
                actionBar.setTitle(R.string.xc);
                actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.fourth));
                this.setWho(CJK);
                lists.clear();
                list = new ListData(RandomWelcom(R.string.xiaocang),ListData.RECEVE,ListData.CODE_TEXT,getCurTime(),this.getWho());
                break;
            case R.id.action_fy:
                actionBar.setTitle(R.string.fyj);
                actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.five));
                this.setWho(FYJ);
                lists.clear();
                list = new ListData(RandomWelcom(R.string.fanyijun),ListData.RECEVE,ListData.CODE_TEXT,getCurTime(),this.getWho());
                break;
            case android.R.id.home:
                this.finish();
                break;
            default:

                break;
        }
        lists.add(list);
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    public int getWho() {
        return who;
    }

    public void setWho(int who) {
        this.who = who;
    }

    private void setIconEnable(Menu menu,boolean enable){
        try{
            Class<?> clazz = Class.forName("android.support.v7.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible",boolean.class);
            m.setAccessible(true);
            m.invoke(menu,enable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}