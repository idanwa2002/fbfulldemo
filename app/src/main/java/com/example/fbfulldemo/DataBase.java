package com.example.fbfulldemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DataBase extends AppCompatActivity implements AdapterView.OnItemClickListener {
    EditText info;
    ListView lv;
    ArrayList<String> stringList= new ArrayList<String>();
    ArrayList<String> AList= new ArrayList<String>();
    ArrayAdapter<String> adp;
    String nd;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    ValueEventListener strListener;

    AlertDialog.Builder ad;
    LinearLayout dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        info=(EditText)findViewById(R.id.info);
        lv=(ListView) findViewById(R.id.lv);

        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);

//        AList.add("idan");

        ValueEventListener mrListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                stringList.clear();
                for (DataSnapshot data : ds.getChildren()){
                    String tmp=data.getValue(String.class);
                    stringList.add(tmp);
                }
                adp = new ArrayAdapter<String>(DataBase.this,R.layout.support_simple_spinner_dropdown_item, stringList);
                lv.setAdapter(adp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(mrListener);


    }


    public void updateData(View view) {
        nd=info.getText().toString();
        // rtdb step 4:
        myRef.child(nd).setValue(nd);
        Toast.makeText(this, "Writing succeeded", Toast.LENGTH_SHORT).show();
        info.setText("");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.dialogx, null);
        ad = new AlertDialog.Builder(this);
        ad.setCancelable(false);
        ad.setTitle("delete?");
        ad.setView(dialog);
        ad.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String str= stringList.get(position);
                myRef.child(str).removeValue();
                Toast.makeText(DataBase.this, "Deleting succeeded", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        ad.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog adb = ad.create();
        adb.show();
    }

    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menuLogin) {
            Intent si = new Intent(DataBase.this,Loginok.class);
            startActivity(si);
        }
        if (id==R.id.menuDB) {
            Intent si = new Intent(DataBase.this,DataBase.class);
            startActivity(si);
        }
        if (id==R.id.menuStore) {
            Intent si = new Intent(DataBase.this,Storing.class);
            startActivity(si);
        }
        
        return true;
    }


}
