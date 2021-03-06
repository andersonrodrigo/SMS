package com.andersonsilva;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andersonsilva.R;
import com.andersonsilva.adapter.AgrupadoPorLoja;
import com.andersonsilva.entity.Sms;

import java.util.ArrayList;

public class TotalActivity extends AppCompatActivity {

    ListView listView = null;
    ArrayList<Sms> listAgrupadaNomeLoja = null;
    ArrayList<Sms> listaSms = null;
    private static AgrupadoPorLoja adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Compras Agrupadas Loja");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaSms = SmsUtils.getAllSms(getContentResolver());
        listView = (ListView) findViewById(R.id.listAgrupadaPorLoja);
        listAgrupadaNomeLoja = SmsUtils.agrupaPorNomeLoja(listaSms);
        SmsUtils.ordenaListaValorDecrecente(listAgrupadaNomeLoja);
        adapter= new AgrupadoPorLoja(listAgrupadaNomeLoja,getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Sms dataModel= listaSms.get(position);
                Sms dataModel= listaSms.get(position);
             /*   Snackbar.make(view, dataModel.getMsg(), Snackbar.LENGTH_LONG)
                        .setAction("Mensagem completa", null).show();*/
                AlertDialog alertDialog = new AlertDialog.Builder(TotalActivity.this).create();
                alertDialog.setTitle("Mensagem Completa");
                alertDialog.setMessage(dataModel.getMsg());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

}
