package org.swsd.stardust.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * author     :  骆景钊
 * time       :  2017/12/26
 * description:  录音工具包，返回录音文件
 * version:   :  1.0
 */

public class RecordUtil {

    public static File recordFile;
    public static MediaRecorder mediaRecorder;

    //传入Context和Activity
    public static void startRecord(Context context, Activity activity) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("number", Context.MODE_PRIVATE);
        int num = sharedPreferences.getInt("num", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        recordFile = new File("/mnt/sdcard/" + num + ".amr");
        editor.putInt("num", num + 1);
        editor.commit();
        mediaRecorder = new MediaRecorder();

        // 判断，若当前文件已存在，则删除
        if (recordFile.exists()) {
            Log.d("luojingzhao", "delete");
            recordFile.delete();
        }

        //设置录音文件格式和存储路径
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(recordFile.getAbsolutePath());
        Log.d("luojingzhao", recordFile.getAbsolutePath());

        try {

            // 准备好开始录音
            mediaRecorder.prepare();

            mediaRecorder.start();
            Log.d("luojingzhao", "success");
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static File stopRecord() {
        if (recordFile != null) {
            mediaRecorder.stop();
            mediaRecorder.release();

            //结束录音，返回录音文件（可以用字符串得到路径
            return recordFile;
        } else {

            //录音文件为空,返回null
            return null;
        }
    }
}