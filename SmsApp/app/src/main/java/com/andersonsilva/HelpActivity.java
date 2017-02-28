package com.andersonsilva;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ajuda");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"anderson.rodrigo@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Ajuda");
                email.putExtra(Intent.EXTRA_TEXT, "");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Escolha um Cliente de email:"));

            }
        });

        TextView textView = (TextView) findViewById(R.id.textHelp);
        String texto = "Esta aplicação Le os SMS enviados pelo banco a cada vez que é realizada uma compra com o Cartao.\n" +
                " Para Habiliatar o serviço no seu banco basta entrar em contato com o Gerente da sua conta.\n" +
                " Para utilizar a app basta apenas abrir e ver o total acumulado ou clicar sobre os meses para ver as \n" +
                "compras detalhadas Mês a Mês.\n" +
                "Nas opções do menu é possivel ver qual estabelecimento que voce mais gastou e o periodo que as compras foram realizadas.\n" +
                "Também é possivel ver todas as mensagens recebidas na opção Compras Detalhadas\n" +
                "Ao clicar sobre uma menagem será exibido o conteudo da Mensagem que o Banco te mandou.\n" +
                "Atualmente a Aplicação possuiintegração com os Bancos Bradesco, Itau e Santander.";
        textView.setText(texto);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }





}
