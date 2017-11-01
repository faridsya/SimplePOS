package faridsoft.simplepos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by faridya on 8/27/2016.
 */
public class fungsi2 {
public ProgressDialog spinner;
    private static Locale myLocale;
    public void dataguna(Context context,String datajason){
        SharedPreferences sharedpreferences;
        sharedpreferences = context.getSharedPreferences("sesi", Context.MODE_PRIVATE);
        SQLiteDatabase dbtst = context.openOrCreateDatabase("datatst",context.MODE_PRIVATE, null);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("datageneral",datajason);
        editor.commit();
        String jsonStr = datajason;
        dbtst.execSQL("create table if not exists `t_guna`(`c_idguna` int(2),`c_kegunaan` varchar(50),PRIMARY KEY (`c_idguna`))");
        if (jsonStr != null) {
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObj.optJSONArray("dataguna");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String kode = jsonObject.getString("c_idKegunaan");
                String namaguna = jsonObject.getString("c_kegunaan");

                dbtst.execSQL("replace into t_guna(c_idguna,c_kegunaan) values(" + kode + ",'" + namaguna + "')");

            }

        dbtst.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }
    }
    protected void hideSoftKeyboard(Context context, EditText input) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }
    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //jika ada koneksi return true
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        //jika tidak ada koneksi return false
        return false;
    }


    public void changeLocale(Activity context , String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);//Set Selected Locale

        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());//Update the config

    }

    public boolean hapussatuan(){
        return true;
    }

}
