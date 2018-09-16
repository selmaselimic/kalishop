package ba.kalishop.kalishop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import ba.kalishop.kalishop.Model.User;
import ba.kalishop.kalishop.web.ApiClient;
import ba.kalishop.kalishop.web.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private User user;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private ProgressDialog pDialog;
    private Button  _loginButton;
    private TextView _signupButton;
    private EditText _passwordText;
    private EditText _username;

   //private  TextView _signupLink =findViewById(R.id.link_signup); to jos nema xd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);//sad cu malo odmorit
        _username=findViewById(R.id.username) ;
        _passwordText=findViewById(R.id.password) ;
        _loginButton=findViewById(R.id.buttonSignIn) ;
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupButton=findViewById(R.id.message) ;
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,SignUpActivity.class);

                startActivity(i);
                finish();
            }
        });
    }

    public void login() {
        apiInterface = ApiClient.getApiClient() .create(ApiInterface.class);
        Call<User> call = apiInterface.getWords(_username.getText().toString(),_passwordText.getText().toString());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
               user = response.body();

              //  Toast.makeText(getApplicationContext(),user.getUsername().toString(),Toast.LENGTH_LONG).show();

                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putInt("Id",user.getId());
                Ed.putString("username",user.getUsername().toString() );
                Ed.putString("password",user.getPassword().toString());
                Ed.putString("name",user.getName().toString());
                Ed.putString("lastname",user.getPrezime().toString());
                Ed.putString("email",user.getEmail().toString());
                Ed.commit();

                Intent i=new Intent(LoginActivity.this,ProductsActivity.class);

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


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public boolean validate() {
        boolean valid = true;

        String username = _username.getText().toString();
        String password = _passwordText.getText().toString();

        if ("".equals(username) ) {
            _username.setError("enter a valid email address");
            _username.requestFocus();
            valid = false;
        } else {
            _username.setError(null);
        }

        if ("".equals(password)) {
            _passwordText.setError("Password is empty");
            _passwordText.requestFocus();
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

}
