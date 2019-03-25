package com.example.shivani.gpdnoticeboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacultyRegistrationPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    String FacultyRegi_URL = "https://gpdnoticeboard.000webhostapp.com/gpd_NoticeBoard/FacultyRegistration.php";
    String GETDept_URL = "https://gpdnoticeboard.000webhostapp.com/gpd_NoticeBoard/get_departmentdata.php";
    Spinner department;
    Button btnReg;
    EditText username,password;
    SharedPreferences sharedPreferences;
    public static final String MYPREFERENCE = "MyPref";
    public static final String UserName = "userkey";
    public static final String Password = "passkey";
    String dept_id;
    List<String> deptname=new ArrayList<>();
    ArrayList<data_models>deptlist=new ArrayList<data_models>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_registration_page);


        username = (EditText)findViewById(R.id.edt_username);
        password = (EditText)findViewById(R.id.edt_pass);
        department = (Spinner)findViewById(R.id.spinner_dept);

        btnReg = (Button)findViewById(R.id.btn_signup);

        sharedPreferences = getSharedPreferences(MYPREFERENCE, Context.MODE_PRIVATE);




        StringRequest request=new StringRequest(Request.Method.GET, GETDept_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", ">>>>>" + response);
                try {

                    JSONArray j=new JSONArray(response);
                    for(int i=0;i<j.length();i++)
                    {
                        String dept=j.getJSONObject(i).getString("Departmentname");
                        String id=j.getJSONObject(i).getString("dept_id");
                        Log.e("Department", ">>>>>" + dept);

                        data_models d=new data_models();
                        d.setId(id);
                        d.setDepartment(dept);
                        deptname.add(dept);
                        deptlist.add(d);
                    }


                    department.setOnItemSelectedListener(FacultyRegistrationPage.this);

                    ArrayAdapter aa = new ArrayAdapter(FacultyRegistrationPage.this,R.layout.spinner_item,deptname);
                    department.setAdapter(aa);

                }
                catch (Exception e)
                {
                    Toast.makeText(FacultyRegistrationPage.this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FacultyRegistrationPage.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();

            }
        });


        RequestQueue queue= Volley.newRequestQueue( FacultyRegistrationPage.this);
        queue.add(request);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String uname = username.getText().toString();
                final String pass = password.getText().toString().trim();


                StringRequest request = new StringRequest(Request.Method.POST, FacultyRegi_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.trim().equals("Success"))
                        {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(UserName,uname);
                            editor.putString(Password,pass);
                            editor.putString("reg","1");
                            editor.commit();
                            Toast.makeText(FacultyRegistrationPage.this, "Register Successfully", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(FacultyRegistrationPage.this, "Register failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FacultyRegistrationPage.this, "Error"+error, Toast.LENGTH_SHORT).show();
                    }
                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String>str=new HashMap<>();

                        str.put("dept_id",dept_id);
                        str.put("username",uname);
                        str.put("password",pass);

                        return str;
                    }
                };

                RequestQueue queue1 = Volley.newRequestQueue(FacultyRegistrationPage.this);
                queue1.add(request);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        dept_id=deptlist.get(position).getId();
        Toast.makeText(this, deptlist.get(position).getDepartment(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(), "Select spinner item", Toast.LENGTH_SHORT).show();
    }
}
