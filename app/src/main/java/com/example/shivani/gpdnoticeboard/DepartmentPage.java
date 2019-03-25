package com.example.shivani.gpdnoticeboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

public class DepartmentPage extends AppCompatActivity {

    String GETDEPT_URL = "https://gpdnoticeboard.000webhostapp.com/gpd_NoticeBoard/get_departmentdata.php";
    private DepartmentAdapter departmentAdapter;
    private List<data_models> data_models = new ArrayList();
    EditText inputDept;
    private RecyclerView recyclerView;
    public data_models d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_page);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        recyclerView=(RecyclerView)findViewById(R.id.rv_dep);


        StringRequest request=new StringRequest(Request.Method.GET, GETDEPT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", ">>>>>" + response);
                try {

                    JSONArray j=new JSONArray(response);
                    for(int i=0;i<j.length();i++)
                    {
                        String department=j.getJSONObject(i).getString("Departmentname");
                        String id=j.getJSONObject(i).getString("dept_id");
                        Log.e("Department", ">>>>>" + department);


                        departmentAdapter=new DepartmentAdapter(DepartmentPage.this,data_models);
                        RecyclerView.LayoutManager clayoutmanager=new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(clayoutmanager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(departmentAdapter);

                        d=new data_models();
                        d.setDepartment(department);
                        d.setId(id);
                        data_models.add(d);


                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(DepartmentPage.this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DepartmentPage.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();

            }
        });


        RequestQueue queue= Volley.newRequestQueue(DepartmentPage.this);
        queue.add(request);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Department");
        builder.setMessage("Enter new Department");

        inputDept = new EditText(this);
        builder.setView(inputDept);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            String DEPTENTRY_URL = "https://gpdnoticeboard.000webhostapp.com/gpd_NoticeBoard/DepartmentEntry.php";

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String txt = inputDept.getText().toString();
                if (TextUtils.isEmpty(txt)) {
                    Toast.makeText(DepartmentPage.this, "Enter Department Name", Toast.LENGTH_LONG).show();
                } else
                {
                    StringRequest request = new StringRequest(Request.Method.POST, DEPTENTRY_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.trim().equals("Success")) {
                                Toast.makeText(DepartmentPage.this, "Department Added", Toast.LENGTH_SHORT).show();
                                Toast.makeText(DepartmentPage.this, txt, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DepartmentPage.this, DepartmentPage.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(DepartmentPage.this, "Department Not Added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DepartmentPage.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> str = new HashMap<>();
                            str.put("departmentname", txt);
                            return str;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(DepartmentPage.this);
                    queue.add(request);

                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
        return true;
    }
}
