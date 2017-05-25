package cho.com.toktalk;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {

    Button btnFinish, btnSend;
    EditText etText;
    String email;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String[] myDataset = {"안녕","오늘","뭐했어","??"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            email = user.getEmail();


            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }

        etText = (EditText) findViewById(R.id.etText);
        btnFinish = (Button)findViewById(R.id.btnFinish);
        btnSend = (Button)findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stText = etText.getText().toString(); //etText를 가져와서 변수에 저장

                if(stText.equals("") || stText.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ChatActivity.this, email+","+stText , Toast.LENGTH_SHORT).show();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance(); //파이어베이스의 겟인스턴스를 가져와서 데이터를 연결해주고
                    DatabaseReference myRef = database.getReference("chat").child(formattedDate); // chat에 저장하겠다 chat은 또 하나의 디렉토리의 다른 내용을 저장한다고 생각하면 됨

                    Hashtable<String, String> chat = new Hashtable<String, String>();
                    chat.put("email",email);
                    chat.put("text",stText); //stText가 text키값에 저장

                    myRef.setValue(chat);

                }


            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }
}
