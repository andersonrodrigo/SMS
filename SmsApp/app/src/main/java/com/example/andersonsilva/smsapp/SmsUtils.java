package com.example.andersonsilva.smsapp;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.andersonsilva.smsapp.entity.Sms;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by anderson.silva on 14/02/2017.
 */
public class SmsUtils  {


    private  List<Sms> sms_All;


    static NumberFormat formato2 = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));




    /**
     *
     * @return
     */
    public static ArrayList<Sms> agrupaPorNomeLoja(ArrayList<Sms> listaSms) {
        ArrayList<Sms> listaRetorno = new ArrayList<Sms>();
        for (Sms obj:listaSms){
            Sms objTela = recuperaObjetoTela(listaRetorno,obj);
            objTela.setLoja(obj.getLoja());
            if (objTela.getValor() == null){
                objTela.setValor("0");
            }

            if (objTela.getDataInicial() == null){
                objTela.setDataInicial(obj.getDataCompra());
            }
            objTela.setDataFinal(obj.getDataCompra());
            try {
                objTela.setValor((Double.parseDouble(obj.getValor().replace(",",".")) + Double.parseDouble(objTela.getValor().replace(",",".")))+"");
            }catch(Exception e){

            }
            objTela.setDataCompra( "De: "+ objTela.getDataFinal() +" - a - "+ objTela.getDataInicial());


        }
        return listaRetorno;
    }

    /**
     *
     * @param listaRetorno
     * @param compra
     * @return
     */
    private static Sms recuperaObjetoTela(List<Sms> listaRetorno,Sms compra) {
        Sms retorno = new Sms();
        if (listaRetorno.isEmpty()){
            listaRetorno.add(retorno);
            return retorno;
        }else for (Sms obj: listaRetorno){
            if (obj.getLoja().equals(compra.getLoja())){
                return obj;
            }
        }
        listaRetorno.add(retorno);
        return retorno;
    }

    /**
     *
     * @param listaAgrupada
     */
    public static void ordenaListaValorDecrecente(List<Sms> listaAgrupada){
        Collections.sort(listaAgrupada,(new Comparator<Sms>() {
            @Override
            public int compare(Sms sms, Sms t1) {
                Double val1 = Double.parseDouble(sms.getValor());
                Double val2 = Double.parseDouble(t1.getValor());
                return val1.compareTo(val2)*-1;
            }
        }));
    }


    /**
     *
     * @param lista
     * @param ordem
     */
    public static void ordenaListaValorData(List<Sms> lista,final int ordem){
        Collections.sort(lista,(new Comparator<Sms>() {
            @Override
            public int compare(Sms sms, Sms t1) {
                Date data1 = null;
                Date data2 = null;
                try{
                    data1 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(sms.getDataCompra());
                    data2 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(t1.getDataCompra());
                    return data1.compareTo(data2) * ordem;
                }catch (Exception e){
                    return 0;
                }

            }
        }));
    }

    /**
     *
     * @param c
     * @param cr
     * @return
     */
    public static String recuperaValorTotalGasto(Cursor c,ContentResolver cr){
        ArrayList<Sms> listaSms =  null;//getAllSms(c, cr);
        List<Sms> listaAgrupada = agrupaPorNomeLoja(listaSms);
        double valorTotal = 0;
        for(Sms obj:listaAgrupada){
            valorTotal = valorTotal + Double.parseDouble(obj.getValor().replace(".","").replace(",","."));
        }
        return formato2.format(valorTotal);
    }

    /***
     *
     * @param listaSms
     * @return
     */
    public static String recuperaValorTotalGasto(ArrayList listaSms){
        List<Sms> listaAgrupada = agrupaPorNomeLoja(listaSms);
        double valorTotal = 0;
        for(Sms obj:listaAgrupada){
            valorTotal = valorTotal + Double.parseDouble(obj.getValor().replace(",","."));
        }
        return formato2.format(valorTotal);
    }

    /**
     *
     * @param listaSms
     * @return
     */
    public static ArrayList<Sms> recuperaListaValorMensal(ArrayList<Sms> listaSms){
        return (ArrayList<Sms>)agrupaPorMes(listaSms);
    }
    /**
     *
     * @param c
     * @param cr
     * @return
     */
    public static List<Sms> getAgrupamentoMensal(Cursor c, ContentResolver cr) {
        List<Sms> listaSms = null;// getAllSms(c, cr);
        return agrupaPorMes(listaSms);
    }

    /**
     *
     * @param lista
     * @return
     */
    private static List<Sms> agrupaPorMes(List<Sms> lista){
        List<Sms> retorno = new ArrayList<Sms>();
        for(Sms sms:lista){
            try {
                Date dataCompra = new SimpleDateFormat("dd/MM/yyyy").parse(sms.getDataCompra());
                Sms retornoTela = recuperaAgrupamentoMensalTela(retorno,sms,dataCompra);
                if (retornoTela.getValor() == null){
                    retornoTela.setValor("0");
                }
                retornoTela.setDataCompra(sms.getDataCompra());
                retornoTela.setValor(formato2.format(Double.parseDouble(retornoTela.getValor().replace("R$","").replace(".","").replace(",",".")) + Double.parseDouble(sms.getValor().replace(".","").replace(",","."))));
                }catch(Exception e){
              //  Toast.makeText()
            }

        }
        formataDataParaExibicao(retorno);
        return retorno;
    }



    /**
     *
     * @param retorno
     * @param sms
     * @return
     */
    private static Sms recuperaAgrupamentoMensalTela(List<Sms> retorno, Sms sms,Date dataCompra)throws Exception {
        Sms obj = new Sms();
        if (retorno.isEmpty()){
            retorno.add(obj);
        }else{
            for(Sms smsArmazenado : retorno){
                Date dataArmazenada = null;
              try{
                    dataArmazenada = new SimpleDateFormat("dd/MM/yyyy").parse(smsArmazenado.getDataCompra());
                }catch (Exception e){

                }
                if (dataCompra.getMonth() == dataArmazenada.getMonth() && dataCompra.getYear() == dataArmazenada.getYear()){
                    return smsArmazenado;
                }
            }
            retorno.add(obj);
        }

        return obj;
    }

    /**
     * Formata do formato mes ano
     *
     * @param listaSms
     */
    private static void formataDataParaExibicao(List<Sms> listaSms) {
        for(Sms s:listaSms){
            try {
                s.setDataCompra(new SimpleDateFormat("MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse(s.getDataCompra())));
            }catch (Exception e){

            }
        }
    }


    /**
     *
     * @param cr
     * @param dataReferencia
     * @return
     */
    public static ArrayList<Sms> getVendasAcumuladasMes(ContentResolver cr,String dataReferencia){
        ArrayList<Sms> msgs = getAllSms(cr);
        Date objDataReferencia = null;
        try{
            if (dataReferencia.length() == 7){
                dataReferencia = "01/"+dataReferencia;
            }
            objDataReferencia = new SimpleDateFormat("dd/MM/yyyy").parse(dataReferencia);
            return filtraMsgPorMes(msgs,objDataReferencia);
        }catch (Exception e){
            return null;
        }

    }

    /**
     *
     * @param msgs
     * @param objDataReferencia
     * @return
     */
    private static ArrayList<Sms> filtraMsgPorMes(ArrayList<Sms> msgs, Date objDataReferencia) {
        ArrayList<Sms> listaRetorno = new  ArrayList<Sms>();
        for(Sms sms:msgs){
            if (sms.getDataCompra()!=null){
                Date dataReferencia = null;
                try {
                    dataReferencia = new SimpleDateFormat("dd/MM/yyyy").parse(sms.getDataCompra());
                    if (dataReferencia.getMonth()==objDataReferencia.getMonth() && dataReferencia.getYear() == objDataReferencia.getYear()){
                        listaRetorno.add(sms);
                    }
                }catch(Exception e){

                }
            }
        }
        return listaRetorno;
    }



    /**
     *
     * @return
     */
    public static ArrayList<Sms> getAllSms(ContentResolver cr ) {
        try {
            ArrayList<Sms> listaSms = new ArrayList<Sms>();
            Uri message = Uri.parse("content://sms/");
            Cursor c = cr.query(message, null, null, null, null);
            //  startManagingCursor(c);
            int totalSMS = c.getCount();
            if (c.moveToFirst()) {
                for (int i = 0; i < totalSMS; i++) {
                    Sms objSms = new Sms();
                    objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    if (objSms.getMsg() != null) {
                        if (objSms.getMsg().indexOf("BRADESCO CARTOES") > -1) {
                            try {
                                objSms.setLoja(objSms.getMsg().substring(objSms.getMsg().indexOf("NO(A)") + 5, objSms.getMsg().length()));
                                objSms.setDataCompra(objSms.getMsg().substring(objSms.getMsg().indexOf("EM ") + 3, objSms.getMsg().indexOf("EM ") + 3 + 16));
                                objSms.setValor(objSms.getMsg().substring(objSms.getMsg().indexOf("VALOR DE $ ") + 11, objSms.getMsg().indexOf("NO(A)")));
                                listaSms.add(objSms);
                            } catch (Exception e) {

                            }

                        } else if (objSms.getMsg().indexOf("ITAU DEBITO") > -1) {
                            /** ITAU DEBITO: Cartao final 3896 COMPRA APROVADA 11/02 20:25:37 R$ 10,00 Local: DEL REY. */
                            try {
                                objSms.setLoja(objSms.getMsg().substring(objSms.getMsg().indexOf("Local:") + 6, objSms.getMsg().length()));
                                objSms.setLoja(objSms.getMsg().substring(objSms.getMsg().indexOf("APROVADA ") + 9, objSms.getMsg().indexOf("APROVADA ") + 9 + 14));
                                objSms.setLoja(objSms.getMsg().substring(objSms.getMsg().indexOf(" R$ ") + 4, objSms.getMsg().indexOf("Local:")));
                                listaSms.add(objSms);
                            } catch (Exception e) {

                            }
                        }
                    }
                    c.moveToNext();
                }
            }
            c.close();
            return listaSms;
        }catch(Exception e){
            return new ArrayList<Sms>();
        }
    }
}

