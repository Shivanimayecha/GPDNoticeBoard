package com.example.shivani.gpdnoticeboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class Categories extends AppCompatActivity {

    private CardView department,facultyDeptRegi,changepass,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        logout=(CardView)findViewById(R.id.card_logout);
        changepass=(CardView)findViewById(R.id.card_changPass);
        department=(CardView)findViewById(R.id.card_dept);
        facultyDeptRegi=(CardView)findViewById(R.id.card_facultydeptregi);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder adb=new AlertDialog.Builder(Categories.this);
                adb.setMessage("are you sure you want to Logout");

                //positive button
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Toast.makeText(Employee_Categories.this, "Logout User : " + string,Toast.LENGTH_LONG).show();

                        Toast.makeText(Categories.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Categories.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
                //negative button
                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                       /* Intent intent=new Intent(Categories.this,Categories.class);
                        startActivity(intent);*/
                    }
                });
                AlertDialog alertDialog = adb.create();
                alertDialog.show();
            }
        });



        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Categories.this,DepartmentPage.class);
                startActivity(intent);
            }
        });

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Categories.this,Admin_ChangePass.class);
                startActivity(intent);
            }
        });
        facultyDeptRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Categories.this,FacultyRegistrationPage.class);
                startActivity(intent);
            }
        });

    }
}
