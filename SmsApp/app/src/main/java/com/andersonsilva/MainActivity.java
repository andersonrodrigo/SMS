package com.andersonsilva;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.andersonsilva.R;
import com.andersonsilva.adapter.SmsAdapter;
import com.andersonsilva.entity.Sms;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView = null;
    ArrayList<Sms> listaSms = null;
    ArrayList<Sms> listaUtilizada = null;
    private static SmsAdapter adapter;
    static int  REQUEST_CODE_A = 10;

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(MainActivity.this, R.string.ERRO_PERMISSAO, Toast.LENGTH_LONG).show();
                    TextView valorTotal = (TextView) findViewById(R.id.tituloTotalGasto);
                    valorTotal.setText("Não consegui Ler seus SMSs, reintale novamente e me de permissão por favor.. :)");

                 //   Toast.makeText(MainActivity.class, "Não posso ler seus SMSs.. :(", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!checkIfAlreadyhavePermission()){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{ Manifest.permission.READ_SMS},
                        REQUEST_CODE_A);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

       // this.startManagingCursor(c);

        iniciaActivity(toolbar);


 }

    public void iniciaActivity( Toolbar toolbar){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaSms = SmsUtils.getAllSms(getContentResolver());
        TextView valorTotal = (TextView) findViewById(R.id.totalGasto);
        // SmsUtils smsUtils = new SmsUtils();
        valorTotal.setText(SmsUtils.recuperaValorTotalGasto(listaSms));


        listView = (ListView) findViewById(R.id.listaMensal);
        //   listaSms = (ArrayList) SmsUtils.getAgrupamentoMensal(c,getContentResolver());
        //     listaSms = new ArrayList<Sms>();
        listaUtilizada = SmsUtils.recuperaListaValorMensal(listaSms);
        adapter= new SmsAdapter(listaUtilizada,getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Sms dataModel= listaUtilizada.get(position);
             
                Intent intent = new Intent(getApplicationContext(), ActivityMensal.class);
                Bundle params = new Bundle();
                params.putString("mesReferencia", dataModel.getDataCompra());
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Locate MenuItem with ShareActionProvider

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        } else if (id == R.id.todasComprasAgrupadasPorLoja) {
            Intent i = new Intent(getApplicationContext(), TotalActivity.class);
            startActivity(i);
       } else if (id == R.id.todasDetalhadasPorData) {
            Intent i = new Intent(getApplicationContext(), VendasGeraisActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_send) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.andersonsilva");
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            startActivity(sharingIntent);
        }else if(id == R.id.graficos){
            Intent i = new Intent(getApplicationContext(), GraficosActivity.class);
            startActivity(i);
        }else if(id== R.id.nav_help){
            Intent i = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(i);
        }else if (id == R.id.nav_sobre){
            Intent i = new Intent(getApplicationContext(), SobreActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }


}
