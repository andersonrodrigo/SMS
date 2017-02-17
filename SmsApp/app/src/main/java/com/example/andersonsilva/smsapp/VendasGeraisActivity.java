package com.example.andersonsilva.smsapp;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andersonsilva.smsapp.com.exemplo.andersonsilva.smsapp.adapter.AgrupadoPorLoja;
import com.example.andersonsilva.smsapp.com.exemplo.andersonsilva.smsapp.adapter.SmsAdapter;
import com.example.andersonsilva.smsapp.com.exemplo.andersonsilva.smsapp.adapter.TodasVendaDetalhadas;
import com.example.andersonsilva.smsapp.com.exemplo.andersonsilva.smsapp.adapter.VendaDetalhada;
import com.example.andersonsilva.smsapp.entity.Sms;

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
                Snackbar.make(view, dataModel.getMsg(), Snackbar.LENGTH_LONG)
                        .setAction("Mensagem completa", null).show();
            }
        });


        /**
         * ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header, myListView, false);
         myListView.addHeaderView(header, null, false);
         */

    }
}
