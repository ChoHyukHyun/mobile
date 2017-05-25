package cho.com.toktalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    Button btnLogin, btnRegister;
    EditText etEmail, etPassword;
    String stEmail, stPassword;
    ProgressBar pbLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);
        mAuth = FirebaseAuth.getInstance(); //로그인 여부 정보를 가져오는 변수

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = etEmail.getText().toString(); //입력한 문자를 가져와서 stEmail에 저장하겠다
                stPassword = etPassword.getText().toString();
                userLogin(stEmail,stPassword);

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = etEmail.getText().toString(); //입력한 문자를 가져와서 stEmail에 저장하겠다
                stPassword = etPassword.getText().toString();

                //Toast.makeText(getApplicationContext(), stEmail+","+stPassword,Toast.LENGTH_LONG).show();
                registerUser(stEmail,stPassword); //registerUser 메소드 실행하면서 인자값 가져오기
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        Toast.makeText(MainActivity.this, "Susses",
                                Toast.LENGTH_SHORT).show();


                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication faild",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void userLogin(String email,String password) {
        pbLogin.setVisibility(View.VISIBLE); //로그인 버튼을 클릭하면 프로그래스바가 보이게
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        pbLogin.setVisibility(View.INVISIBLE); //완료가 되면 안보이게
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) { //아이디 비밀번호가 틀리면
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication faild",
                                    Toast.LENGTH_SHORT).show();
                        }
                        /*else if() { //아이디 비밀번호가 공백일때

                        }*/

                        else {
                            Intent it = new Intent(MainActivity.this,ChatActivity.class);
                            startActivity(it);
                        }

                        // ...
                    }
                });
    }

}
