package mr.bel.projectforhttpre;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import mr.bel.projectforhttpre.utils.Link;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity {

    private TextView viewVkInfo;
    private EditText InsertVkId;
    private Button Start;
    private ProgressBar progressBar;
    private static String IPVK = "https://api.vk.com/";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(IPVK)
            .build();
    private Link link = retrofit.create(Link.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewVkInfo = findViewById(R.id.textView);
        InsertVkId = findViewById(R.id.editTextTextPersonName);
        Start = findViewById(R.id.button);
        progressBar = findViewById(R.id.progbar);

        @SuppressLint("StaticFieldLeak")
        class VKQueryTask extends AsyncTask<Void,Void,String > {
            protected void onPreExecute(){
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            protected String  doInBackground(Void... voids) {
                Map<String, String> map = null;
                try {
                    Map<String, String> responseRetrofit = new HashMap<>();
                    responseRetrofit.put("user_ids", InsertVkId.getText().toString());
                    responseRetrofit.put("access_token", "340388dda23477069c33403340388dd6b9169466b9e52a439d3652b");
                    responseRetrofit.put("v", "5.124");
                    Call<Object> call = link.ParsVK(responseRetrofit);
                    Response<Object> response1 = call.execute();
                    map = gson.fromJson(response1.body().toString(), Map.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert map != null;
                return map.toString();
            }
            protected void onPostExecute(String response){
                String FirstName = null;
                String LastName = null;
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("response");
                    JSONObject userInfo = ja.getJSONObject(0);
                    FirstName = userInfo.getString("first_name");
                    LastName = userInfo.getString("last_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                StringBuilder result = new StringBuilder("");
                result.append(FirstName).append(" ").append(LastName).append(" ");
                viewVkInfo.setText(result);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

        View.OnClickListener SuperStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VKQueryTask().execute();
            }
        };
        Start.setOnClickListener(SuperStart);
    }
}