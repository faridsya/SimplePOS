package faridsoft.simplepos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class proseslogin extends AsyncTask<String, Void, String> {
    private ProgressDialog spinner;
    public String urlserver;
    private TaskCompleted mCallback;
    private Context context;

    public Activity activity;
    private String a="";
    public proseslogin(Context context, Activity _activity) {
        this.context = context;
        this.activity = _activity;
        this.mCallback = (TaskCompleted) context;
    }


    protected void onPreExecute() {


    }



    @Override
    protected String doInBackground(String... arg0) {
        //Toast.makeText(this.context,urlserver, Toast.LENGTH_SHORT).show();
        String nis = arg0[0];
        String tgl = arg0[1];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;

        try {
            data = "?nis=" + URLEncoder.encode(nis, "UTF-8");
            data += "&tgl=" + URLEncoder.encode(tgl, "UTF-8");

            urlserver = context.getString(R.string.urlserver);
            link = urlserver + "login.php"+data;

            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            result = bufferedReader.readLine();
            return result;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }


    protected void onPostExecute(String result) {

        String jsonStr = result;
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String query_result = jsonObj.getString("hasil");
                ArrayList <String> hasilnya = new ArrayList<>();
                if (query_result.equals("ok")) {


                    hasilnya.add("ok");
                    hasilnya.add("login");
                    mCallback.TaskCompleted(hasilnya);

                }
                else {

                    Toast.makeText(context, query_result, Toast.LENGTH_SHORT).show();
                    hasilnya.add("gagal");
                    hasilnya.add("login");
                    mCallback.TaskCompleted(hasilnya);

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context,"Error parsing Json", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }
    }




}