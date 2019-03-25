package com.example.shivani.gpdnoticeboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.MyViewHolder> {

    public List<data_models> models;
    Context context;

    public DepartmentAdapter(Context context,List<data_models> models)
    {
        this.models = models;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView deptname;
        ImageView delete,edit;

        public MyViewHolder(View itemView) {
            super(itemView);
            deptname=(TextView)itemView.findViewById(R.id.txt_deptname);
            delete=(ImageView)itemView.findViewById(R.id.iv_delete);
            edit=(ImageView)itemView.findViewById(R.id.iv_edit);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {

        data_models data_model=models.get(i);
        myViewHolder.deptname.setText(data_model.getDepartment());



        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteFromServer(models.get(i).getId());

            }
        });
        myViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editFromServer(models.get(i).getId(),models.get(i).getDepartment());
                //city=models.get(position).getCity().toString();
            }
        });
        

    }

    private void editFromServer( final String id, String department) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Edit Department");
        builder.setMessage("Enter new Department for edit");


        final EditText editDept=new EditText(context);
        builder.setView(editDept);
        editDept.setText(department);

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {

            String URLEDIT="https://gpdnoticeboard.000webhostapp.com/gpd_NoticeBoard/update_department.php";


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                final String txt=editDept.getText().toString();

                if(TextUtils.isEmpty(txt))
                {
                    Toast.makeText(context, "Enter Department Name", Toast.LENGTH_LONG).show();
                }
                else
                {
                    StringRequest request=new StringRequest(Request.Method.POST, URLEDIT, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.trim().equals("edit"))
                            {
                                Toast.makeText(context, "Department updated", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context,txt, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(context,DepartmentPage.class);
                                context.startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(context,"Department not update",Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String>str=new HashMap<>();
                            str.put("dept",txt);
                            str.put("dept_id",String.valueOf(id));
                            return str;
                        }
                    };

                    RequestQueue queue=Volley.newRequestQueue(context);
                    queue.add(request);
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog ad=builder.create();
        ad.show();

    }

    private void deleteFromServer(final String id) {


        String URL2="https://gpdnoticeboard.000webhostapp.com/gpd_NoticeBoard/delete_department.php";

        StringRequest request1=new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.trim().equals("delete"))
                {
                    Toast.makeText(context, "Delete successfully ", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context,DepartmentPage.class);
                    context.startActivity(intent);
                }
                else
                {
                    Toast.makeText(context, "unsuccessful", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>str=new HashMap<>();
                str.put("dept_id", String.valueOf(id));
                return str;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(request1);

    }

    @Override
    public int getItemCount() {
        return models.size();
    }


}
