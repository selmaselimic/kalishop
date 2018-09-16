package ba.kalishop.kalishop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

public class ProfileActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private User user;
    private Button cancel;
    private Button confirm;
    EditText name;
    EditText lastname;
    EditText username;
    EditText password;
    EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences sp1=getSharedPreferences ("Login",0);


         name=findViewById(R.id.firstnameProfile);

         lastname=findViewById(R.id.lastnameProfile);

         username=findViewById(R.id.usernameProfile);

         password=findViewById(R.id.passwordProfile);

         email=findViewById(R.id.emailProfile);


        String mail=sp1.getString("email", null);
        String _name=sp1.getString("name",null);
        String _lastname=sp1.getString("lastname",null);
        String _username=sp1.getString("username",null);
        String _password=sp1.getString("password",null);


        name.setText(_name);
        lastname.setText(_lastname);
        email.setText(mail);
        username.setText(_username);
        password.setText(_password);

        cancel=findViewById(R.id.cancel);
        confirm=findViewById(R.id.confirm);







        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp1=getSharedPreferences ("Login",0);

                String mail=sp1.getString("email", null);
                String _name=sp1.getString("name",null);
                String _lastname=sp1.getString("lastname",null);
                String _username=sp1.getString("username",null);
                String _password=sp1.getString("password",null);


                name.setText(mail);
                lastname.setText(_name);
                email.setText(_lastname);
                username.setText(_username);
                password.setText(_password);

                Intent i=new Intent(ProfileActivity.this,ProductsActivity.class);

                startActivity(i);
                finish();


            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ovdje sad treba pozvati api i proslijediti parametre al ne da mi se sad, malo cu odmorit
                //dont forget to put new method in api interface for changing data
                //here need to provide userid so api knows which user in database
                SharedPreferences sp1=getSharedPreferences ("Login",0);

                Integer id=sp1.getInt("Id", 0);
                name=findViewById(R.id.firstnameProfile);

                lastname=findViewById(R.id.lastnameProfile);

                username=findViewById(R.id.usernameProfile);

                password=findViewById(R.id.passwordProfile);

                email=findViewById(R.id.emailProfile);
                apiInterface = ApiClient.getApiClient() .create(ApiInterface.class);
                Call<User> call = apiInterface.changeData(id,username.getText().toString(),password.getText().toString(),name.getText().toString(),lastname.getText().toString(),email.getText().toString());
                 //u ovom pozivu nikako mi fino ne preuzima ove parametre moram provjerit to
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        user = response.body();

                        SharedPreferences sp1=getSharedPreferences ("Login",0);
                         SharedPreferences.Editor editor = sp1.edit();
                        editor.putString("email",user.getEmail());
                        editor.putString("name",user.getName());
                        editor.putString("username",user.getUsername());
                        editor.putString("password",user.getPassword());
                        editor.putString("lastname",user.getPrezime());
                       editor.apply();
                        Toast.makeText(getApplicationContext(),"Uspjesna izmjena podataka",Toast.LENGTH_LONG).show();



                        Intent i=new Intent(ProfileActivity.this,ProductsActivity.class);

                        startActivity(i);
                        finish();
                        //now should put this to shared references to remebemer changed data
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
        });
    }
}
