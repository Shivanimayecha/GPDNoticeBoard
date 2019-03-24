package com.example.shivani.gpdnoticeboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String URL_ADMIN = "https://gpdnoticeboard.000webhostapp.com/gpd_NoticeBoard/AdminLogin.php";
    //String URL_EMPLOYEE = "https://shivanimayecha0908.000webhostapp.com/HandyManService/EmployeeLogin.php";
    Button btnlogin;
    EditText inputUsername, inputPassword;
    RadioGroup rd_login;
    RadioButton rb_employee, rb_admin;
    TextView txtLogin;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UserName = "userKey";
    public static final String Password = "passwordKey";
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUsername = (EditText) findViewById(R.id.edtUsername);
        inputPassword = (EditText) findViewById(R.id.edtPassword);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        rd_login = (RadioGroup) findViewById(R.id.rdlogin);

        txtLogin = (TextView) findViewById(R.id.txtLogin);

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String strng = pref.getString("userKey", "");

       // rb_employee = (RadioButton) findViewById(R.id.employeelogin);
        rb_admin = (RadioButton) findViewById(R.id.adminlogin);

        rb_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean checked = ((RadioButton) view).isChecked();
                if (checked) {
                    txtLogin.setText("Admin Login");
                    // Toast.makeText(MainActivity.this, "Admin", Toast.LENGTH_SHORT).show();

                    btnlogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final String username = inputUsername.getText().toString();
                            final String pass = inputPassword.getText().toString();

                            StringRequest request = new StringRequest(Request.Method.POST, URL_ADMIN, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    if (response.trim().equals("Success")) {
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString(UserName, username);
                                        editor.putString(Password, pass);
                                        editor.putString("login", "1");
                                        editor.commit();
                                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(MainActivity.this, "Login User : " + strng,Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(MainActivity.this, Categories.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(MainActivity.this, "Error" + error, Toast.LENGTH_LONG).show();
                                }
                            }
                            ) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> str = new HashMap<>();
                                    str.put("username", username);
                                    str.put("password", pass);
                                    return str;
                                }
                            };

                            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                            queue.add(request);
                        }
                    });
                }
            }
        });
       /* rb_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // pref =
                boolean checked = ((RadioButton) view).isChecked();
                if (checked) {
                    txtLogin.setText("Employee Login");
                    // Toast.makeText(MainActivity.this, "Employee", Toast.LENGTH_SHORT).show();
                    btnlogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final String email = inputUsername.getText().toString();
                            final String pass = inputPassword.getText().toString();

                            StringRequest request = new StringRequest(Request.Method.POST, URL_EMPLOYEE, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    if (response.trim().equals("success")) {
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString(UserName, email);
                                        editor.putString(Password, pass);
                                        editor.putString("login", "1");
                                        editor.commit();
                                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(MainActivity.this, "Login User : " + strng,Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(MainActivity.this, Employee_Categories.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(MainActivity.this, "Error" + error, Toast.LENGTH_LONG).show();
                                }
                            }
                            ) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> str = new HashMap<>();
                                    str.put("email", email);
                                    str.put("password", pass);
                                    return str;
                                }
                            };

                            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                            queue.add(request);
                        }
                    });
                }

            }
        });*/
    }
}
