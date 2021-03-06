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
import com.andersonsilva.adapter.TodasVendaDetalhadas;
import com.andersonsilva.entity.Sms;

import java.util.ArrayList;

public class VendasGeraisActivity extends AppCompatActivity {

    ListView listView = null;
    ArrayList<Sms> listaSms = null;
    private static TodasVendaDetalhadas adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas_gerais);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Compras Detalhadas");
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
        listView = (ListView) findViewById(R.id.listVendasGerais);
        SmsUtils.ordenaListaValorData(listaSms,-1);
        View header = getLayoutInflater().inflate(R.layout.header_vendas_gerais, null);
        adapter= new TodasVendaDetalhadas(listaSms,getApplicationContext());
        listView.setAdapter(adapter);
        listView.addHeaderView(header);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Sms dataModel= listaSms.get(position);
                Sms dataModel= listaSms.get(position);
              /*  Snackbar.make(view, dataModel.getMsg(), Snackbar.LENGTH_LONG)
                        .setAction("Mensagem completa", null).show();*/
                AlertDialog alertDialog = new AlertDialog.Builder(VendasGeraisActivity.this).create();
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


        /**
         * ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header, myListView, false);
         myListView.addHeaderView(header, null, false);
         */

    }
}
