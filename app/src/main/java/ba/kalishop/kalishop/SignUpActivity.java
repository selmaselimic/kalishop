package ba.kalishop.kalishop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ba.kalishop.kalishop.Model.User;
import ba.kalishop.kalishop.web.ApiClient;
import ba.kalishop.kalishop.web.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private User user;
    private Button signup;
    private EditText _passwordText;
    private EditText _username;
    private EditText _name;
    private EditText _lastname;
    private EditText _email;
    private  User user2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


                _username=findViewById(R.id.usernameS) ;
                _passwordText=findViewById(R.id.passwordS) ;
                _name=findViewById(R.id.firstnameS) ;
                _lastname=findViewById(R.id.lastnameS) ;
                _email=findViewById(R.id.emailS);
                signup=findViewById(R.id.buttonSignUp);
                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        signUp();
                    }
                });




    }

    public void signUp() {
        apiInterface = ApiClient.getApiClient() .create(ApiInterface.class);
        Call<User> call = apiInterface.getUser(_username.getText().toString(),_passwordText.getText().toString(),_name.getText().toString(),_lastname.getText().toString(),_email.getText().toString());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
               user = response.body();

                Toast.makeText(getApplicationContext(),"Uspjesna registracija",Toast.LENGTH_LONG).show();



                Intent i=new Intent(SignUpActivity.this,ProductsActivity.class);

                startActivity(i);
                finish();

                //   for (final String s : words) {


                //       word.setText(s);

                //  }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();

            }
        });
    }
}
