package sadiq.raza.assesszone;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Sadiq on 3/11/2018.
 */

public class AvailableTestBt extends AsyncTask<String,Void,String> {
    Context context;
    ArrayList<String> myList;
    ProgressDialog pd ;
    String test_id,test_name;
    static String start_date_time,time_allowed;
    static TestBackgroundTask testBackgroundTask;
    android.support.v7.app.AlertDialog.Builder builder;
    private ArrayList<AvailableTestDetails> availableTestDetails = new ArrayList<>();
    private String testId;

    Boolean flag;
    public AvailableTestBt(Context context,Boolean flag) {
        this.context=context;
        this.flag=flag;
    }

    @Override
    protected String doInBackground(String... param) {


        String test_list="https://assesszone.000webhostapp.com/client/getTest.php";
         try {
            URL url = new URL(test_list);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
             httpURLConnection.setReadTimeout(5000);
             httpURLConnection.setConnectTimeout(5000);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String post_data= URLEncoder.encode("reg_id","UTF-8")+"="+ URLEncoder.encode(MainActivity.reg_no,"UTF-8");
            bw.write(post_data);
            bw.flush();
            bw.close();
            outputStream.close();

            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result="";
            String line="";
            while((line=br.readLine())!=null)
            {
                result+=line;
            }
            br.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        pd=new ProgressDialog(context);
        pd.setMessage("Loading test details");
        pd.setCancelable(false);

        pd.show();

    }

    @Override
    protected void onPostExecute(String result) {
       // Log.e("Available Test ", "jh"+result);
        try
        {
            if(result!=null)
            {
                loadTestDetails(result);
                openTestList();
            }
            else
            {
                Toast.makeText(context, "Could not load test details", Toast.LENGTH_SHORT).show();
                Log.e("ss","null");
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        pd.dismiss();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String aVoid) {
        super.onCancelled(aVoid);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
    private void loadTestDetails(String json)throws  JSONException
    {
        JSONArray arr= new JSONArray(json);
        for(int i=0;i<arr.length();i++)
        {
            JSONObject jsonObject = arr.getJSONObject(i);//for only one test at zero index
            test_id=jsonObject.getString("test_id");
            test_name=jsonObject.getString("test_name");
             start_date_time=jsonObject.getString("start_date_time");
             time_allowed=jsonObject.getString("time_allowed");
            //Log.e("tes : ",test_id+" "+test_name);
            availableTestDetails.add(new AvailableTestDetails(test_id,test_name,start_date_time,time_allowed));
        }
    }

//    public ArrayList<AvailableTestDetails> getAvailableTestDetails() {
//        return availableTestDetails;
//    }

    private void openTestList()
    {

        builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setCancelable(false);
        myList=new ArrayList<String>();
        for(int i=0;i<availableTestDetails.size();i++)
        {
            myList.add(availableTestDetails.get(i).getTest_name());
            //Log.e("NAme",availableTestDetails.get(i).getTest_name());
        }
        if(flag==true)
        {
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, myList);
            HomePage.listView.setAdapter(itemsAdapter);
            return;
        }
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View newView = layoutInflater.inflate(R.layout.testlist,null);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Log.e("Testid ",""+testId);
            }
        });

        builder.setView(newView);

        ListView listView = newView.findViewById(R.id.listView);
        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setTitle("Available Tests");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_activated_1,myList);


        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String testName=adapterView.getItemAtPosition(i).toString();
                for(int j=0;j<availableTestDetails.size();j++)
                {
                    if(testName.equals(availableTestDetails.get(i).getTest_name()))
                    {
                        testId=availableTestDetails.get(i).getTest_id();
                        break;
                    }
                }
                alert.dismiss();
                testBackgroundTask=new TestBackgroundTask(context,testId);
                testBackgroundTask.execute();
                //Log.e("clicked",testName+"  dd "+testId);
            }
        });
        alert.show();
    }
}
