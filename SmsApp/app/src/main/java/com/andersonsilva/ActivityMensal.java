package com.andersonsilva;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andersonsilva.R;
import com.andersonsilva.adapter.VendaDetalhada;
import com.andersonsilva.entity.Sms;

import java.util.ArrayList;

public class ActivityMensal extends AppCompatActivity {

    ListView listView = null;
    ArrayList<Sms> listaSms = null;
    private static VendaDetalhada adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensal);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Compras Do Mês");
        Bundle params = intent.getExtras();

        if(params!=null) {
            String dataReferencia = params.getString("mesReferencia");
            String finalCartao = params.getString("finalCartao");
            listView = (ListView) findViewById(R.id.listMensalAgrupado);
            listaSms = SmsUtils.getVendasAcumuladasMes(getContentResolver(),dataReferencia,finalCartao);
            adapter= new VendaDetalhada(listaSms,getApplicationContext());
            SmsUtils.ordenaListaValorData(listaSms,1);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Sms dataModel= listaSms.get(position);
                    Sms dataModel= listaSms.get(position);
                  //  Snackbar.make(view, dataModel.getMsg(), Snackbar.LENGTH_LONG)
                    //        .setAction("Mensagem completa", null).show();
                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityMensal.this).create();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
