package org.swsd.stardust.presenter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import com.qiniu.util.Json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.HomeNoteModel;
import org.swsd.stardust.model.IHomeNoteModel;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.adapter.HomeAdapter;
import org.swsd.stardust.util.ErrorCodeJudgment;
import org.swsd.stardust.util.UpdateTokenUtil;
import org.swsd.stardust.view.fragment.HomeFragment;
import org.swsd.stardust.view.fragment.IHomeView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.logging.Handler;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *    author     :  张昭锡
 *    time       :  2017/11/14
 *    description:  主页Presenter层
 *    version:   :  1.0
 */
public class HomePresenter implements IHomePresenter{
    private static final String TAG = "HomePresenter";

    IHomeView mIHomeView;
    IHomeNoteModel mHomeNoteModel;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public HomePresenter(IHomeView iHomeView){
        mIHomeView = iHomeView;
        mHomeNoteModel = new HomeNoteModel();
    }

    /**
     *    author     :  张昭锡
     *    time       :  2017/11/14
     *    description:  更改时间选择器时间
     *    version:   :  1.0
     */
    @Override
    public void changeDate(Context context, HomeAdapter adapter, List<NoteBean> noteList,Activity activity,int year, int month, int day) {
        mHomeNoteModel.setYear(year);
        mHomeNoteModel.setMonth(month - 1);
        mHomeNoteModel.setDay(day);

        syncNotesOfDay(year, month, day,noteList,adapter,activity);


        //Log.d(TAG, "changeDate: zyzhang" + getNoteList().size());
        //refreshAdapter(adapter);
    }


    /**
     *    author     :  张昭锡
     *    time       :  2017/11/14
     *    description:  获取用户记录数据
     *    version:   :  1.0
     */
    @Override
    public List<NoteBean> getNoteList() {
        return mHomeNoteModel.getNoteList();
    }

    @Override
    public void setNoteYear(int year) {
        mHomeNoteModel.setYear(year);
    }

    @Override
    public void setNoteMonth(int month) {
        mHomeNoteModel.setMonth(month);
    }

    @Override
    public void setNoteDay(int day) {
        mHomeNoteModel.setDay(day);
    }


    @Override
    public void refreshAdapter(final HomeAdapter adapter) {
        adapter.notifyDataSetChanged();
    }

    /**
     *    author     :  张昭锡
     *    time       :  2017/11/18
     *    description:  判断是否是闰年
     *    version:   :  1.0
     */
    @Override
    public boolean isLeapYear(int year) {
        boolean res = false;
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0){
            res = true;
        }else{
            res = false;
        }
        return res;
    }

    /**
     *    author     :  张昭锡
     *    time       :  2017/11/18
     *    description:  判断本月份天数是否是31天
     *    version:   :  1.0
     */
    @Override
    public boolean isBigMonth(int month) {
         boolean res = false;
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12){
            res = true;
        }else if (month == 4 || month == 6 || month == 9 || month == 11){
            res = false;
        }
        return res;
    }

    @Override
    public void syncNotesOfDay(final int year, final int month, final int day, final List<NoteBean>noteList, final HomeAdapter adapter, final Activity activity){

        Log.d(TAG, "syncNotesOfDay: zxzhang month  " + month);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(year,month - 1,day,0,0,0);

        //根据日期计算得到的long值查询数据库
        long startTime = calendar.getTimeInMillis();
        long endTime = startTime + 24 * 3600 * 1000;


        DataSupport.deleteAll(NoteBean.class,"createTime > ? and createTime < ?",String.valueOf(startTime), String.valueOf(endTime));


        //将年月日转换成符合：年份四位数，月份两位数，日期两位数的格式
        final String strMonth ;
        final String strDay;
        if (month < 10){
            strMonth = "0" + month;
        }else{
            strMonth = String.valueOf(month);
        }
        if (day < 10){
            strDay = "0" + day;
        }else{
            strDay = String.valueOf(day);
        }

        //获得当前用户信息
        final UserBean user = DataSupport.findLast(UserBean.class);
        UpdateTokenUtil.updateUserToken(user);
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {

                //获取用户当天记录
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                                            .url("http://119.29.179.150:81/api/users/" + user.getUserId() + "/notes?all=0&year=" + year + "&month=" + strMonth + "&day=" + strDay)
                                            .addHeader("Authorization",user.getToken())
                                            .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String data = response.body().string();
                    Log.d(TAG, "run: zxzhang" + data);
                    updateDatabase(data,noteList,adapter,activity);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

    public void updateDatabase(final String responseData, List<NoteBean>noteList, final HomeAdapter adapter, Activity activity){
        try{

            if (ErrorCodeJudgment.errorCodeJudge(responseData) == "Ok"){

                JSONObject jsonObject = new JSONObject(responseData);
                final JSONArray jsonArray = jsonObject.getJSONArray("notes");

                Log.d(TAG, "zxzhang jsonArray size " + jsonArray.length());

                for (int i = 0;i < jsonArray.length();i++){
                    final JSONObject note = jsonArray.getJSONObject(i);
                    int noteId = note.getInt("id");
                    String noteUrl = note.getString("url");
                    String noteCreateTime = note.getString("create_time");
                    Boolean noteShareStatus = note.getBoolean("share");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date date = sdf.parse(noteCreateTime);


                    NoteBean noteBean = new NoteBean();
                    noteBean.setNoteId(noteId);
                    noteBean.setShareStatus(noteShareStatus);

                    noteBean.setCreateTime(date.getTime());

                    noteBean.setContent(noteUrl);

                    //getNoteContent(noteUrl,noteBean);
                    //noteBean.setContent(getNoteContent(noteUrl,NoteBean note));

                    noteBean.save();
                    Log.d(TAG, "updateDatabase: zxzhang success");

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        noteList.clear();
        noteList.addAll(getNoteList());
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshAdapter(adapter);
            }
        });
    }

    public void getNoteContent(final String noteUrl,final NoteBean note){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                                                .url(noteUrl)
                                                .build();
                    Response response = client.newCall(request).execute();
 //                   Log.d(TAG, "noteStrContent " + response.body().string());
//                    Log.d(TAG, "noteStrContentfull " + response.toString());
                    note.setContent(response.body().string());
                    note.save();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
