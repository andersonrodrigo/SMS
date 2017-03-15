package com.andersonsilva;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.andersonsilva.entity.Sms;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                objTela.setValor((Double.parseDouble(obj.getValorReal().replace(",",".")) + Double.parseDouble(objTela.getValor().replace(",",".")))+"");
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
            valorTotal = valorTotal + Double.parseDouble(obj.getValorReal().replace(".","").replace(",","."));
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
                retornoTela.setBanco(sms.getBanco());
                retornoTela.setFinalCartao(sms.getFinalCartao());
                retornoTela.setValor(formato2.format(Double.parseDouble(retornoTela.getValor().replace("R$","").replace(".","").replace(",",".")) + Double.parseDouble(sms.getValorReal().replace(".","").replace(",","."))));
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
                if (dataCompra.getMonth() == dataArmazenada.getMonth() && dataCompra.getYear() == dataArmazenada.getYear()
                        && smsArmazenado.getFinalCartao().equals(sms.getFinalCartao())){
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
            String[] reqCols = new String[] { "_id", "address", "body","date" };
            Cursor c = cr.query(message, reqCols, null, null, null);
            //  startManagingCursor(c);
            int totalSMS = c.getCount();
            if (c.moveToFirst()) {
                for (int i = 0; i < totalSMS; i++) {
                    Sms objSms = new Sms();
                    objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
//objSms.setMsg("BB: compra RS  50,00 ANTON0O FAIANO SOUSA cartao final 2546 em 04/03/17.");
                    if (objSms.getMsg() != null) {

                        if (objSms.getMsg().indexOf("BRADESCO CARTOES") > -1) {
                            try {
                                objSms.setBanco("BRADESCO");
                                objSms.setFinalCartao(objSms.getMsg().substring(objSms.getMsg().indexOf(" APROVADA NO ")+13,objSms.getMsg().indexOf(" EM ")));
                                objSms.setLoja(objSms.getMsg().substring(objSms.getMsg().indexOf("NO(A)") + 5, objSms.getMsg().length()).trim());
                                objSms.setDataCompra(objSms.getMsg().substring(objSms.getMsg().indexOf("EM ") + 3, objSms.getMsg().indexOf("EM ") + 3 + 16));
                                objSms.setValor(objSms.getMsg().substring(objSms.getMsg().indexOf("VALOR DE $ ") + 11, objSms.getMsg().indexOf("NO(A)")));
                                if (objSms.getValor().indexOf(" EM ")>-1){
                                    objSms.setValorReal(objSms.getValor().substring(0,objSms.getValor().indexOf(" EM ")));
                                }else{
                                    objSms.setValorReal(objSms.getValor());
                                }

                                listaSms.add(objSms);
                            } catch (Exception e) {

                            }

                        } else if (objSms.getMsg().indexOf("ITAU DEBITO") > -1) {
                            /** ITAU DEBITO: Cartao final 3896 COMPRA APROVADA 11/02 20:25:37 R$ 10,00 Local: DEL REY. Consulte tambem pelo celular www.itau.com.br.*/
                            //objSms.setMsg("ITAU DEBITO: Cartao final 3896 COMPRA APROVADA 11/02 20:25:37 R$ 10,00 Local: DEL REY. Consulte tambem pelo celular www.itau.com.br.");
                            String data = c.getString(c.getColumnIndexOrThrow("date"));
                            Calendar d = Calendar.getInstance();
                            d.setTimeInMillis(Long.valueOf(data));
                            try {
                                objSms.setBanco("ITAU");
                                objSms.setFinalCartao(objSms.getMsg().substring(objSms.getMsg().indexOf("DEBITO: ")+8,objSms.getMsg().indexOf(" COMPRA")));
                                objSms.setLoja(objSms.getMsg().substring(objSms.getMsg().indexOf("Local:") + 6, objSms.getMsg().indexOf(" Consulte ")).trim());
                                objSms.setDataCompra(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(d.getTime()));
                                objSms.setValor(objSms.getMsg().substring(objSms.getMsg().indexOf(" R$ ") + 4, objSms.getMsg().indexOf("Local:")));
                                if (objSms.getValor().indexOf(" EM ")>-1){
                                    objSms.setValorReal(objSms.getValor().substring(0,objSms.getValor().indexOf(" EM ")));
                                }else{
                                    objSms.setValorReal(objSms.getValor());
                                }
                                listaSms.add(objSms);
                            } catch (Exception e) {

                            }
                        }else  if (objSms.getMsg().indexOf("Santander Informa") > -1) {
                            //objSms.setMsg("{BETEL}Santander Informa: Transacao Cartao VISA final 4249 de R$ 5,00 aprovada em 14/12/16 as 21:21 SHOP CONTAGEM");
                            try {
                                objSms.setBanco("Santander");
                                objSms.setFinalCartao(objSms.getMsg().substring(objSms.getMsg().indexOf("Transacao ")+10,objSms.getMsg().indexOf(" de ")));
                                objSms.setLoja(objSms.getMsg().substring(objSms.getMsg().indexOf("em ") + 3 + 17, objSms.getMsg().length()).trim());
                                objSms.setDataCompra((objSms.getMsg().substring(objSms.getMsg().indexOf("em ") + 3, objSms.getMsg().indexOf("em ") + 3 + 17)).replace("as ",""));
                                objSms.setValor(objSms.getMsg().substring(objSms.getMsg().indexOf(" de R$ ") + 7, objSms.getMsg().indexOf("aprovada ")));
                                if (objSms.getValor().indexOf(" EM ")>-1){
                                    objSms.setValorReal(objSms.getValor().substring(0,objSms.getValor().indexOf(" EM ")));
                                }else{
                                    objSms.setValorReal(objSms.getValor());
                                }
                                listaSms.add(objSms);
                            } catch (Exception e) {

                            }

                        }else if (objSms.getMsg().indexOf("Seguranca Santander: Pagamento")> -1 ){
                         //   objSms.setMsg("Seguranca Santander: Pagamento R$ 1762,53 em conta corrente 07/02/17 10:04");
                            try {
                              //  objSms.setLoja(objSms.getMsg().substring(objSms.getMsg().indexOf("em ") + 3, objSms.getMsg().length()));
                                objSms.setBanco("Santander");
                                objSms.setFinalCartao("Conta corrente");
                                objSms.setLoja("Pagamento Conta Corrente");
                                objSms.setDataCompra((objSms.getMsg().substring(objSms.getMsg().indexOf("corrente ") + 9, objSms.getMsg().indexOf("corrente ") + 9 + 14)));
                                objSms.setValor(objSms.getMsg().substring(objSms.getMsg().indexOf("R$ ") + 3, objSms.getMsg().indexOf(" em ")));
                                if (objSms.getValor().indexOf(" EM ")>-1){
                                    objSms.setValorReal(objSms.getValor().substring(0,objSms.getValor().indexOf(" EM ")));
                                }else{
                                    objSms.setValorReal(objSms.getValor());
                                }
                                listaSms.add(objSms);

                            } catch (Exception e) {

                            }

                        } else  if (objSms.getMsg().indexOf("ITAU UNICLASS") > -1) {
                          // objSms.setMsg("ITAU UNICLASS: Cartao final 9976 COMPRA APROVADA 17/02 13:32:22 R$ 39,50 Local: ARTES BAR. Consulte tambem pelo celular www.itau.com.br.");
                            try {
                                objSms.setBanco("ITAU");
                                objSms.setFinalCartao(objSms.getMsg().substring(objSms.getMsg().indexOf("UNICLASS: ")+10,objSms.getMsg().indexOf(" COMPRA")));
                                String data = c.getString(c.getColumnIndexOrThrow("date"));
                                Calendar d = Calendar.getInstance();
                                d.setTimeInMillis(Long.valueOf(data));
                                objSms.setLoja(objSms.getMsg().substring(objSms.getMsg().indexOf("Local: ") + 7, objSms.getMsg().indexOf("Consulte")).trim());
                                objSms.setDataCompra(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(d.getTime()));
                                objSms.setValor(objSms.getMsg().substring(objSms.getMsg().indexOf(" R$ ") + 4, objSms.getMsg().indexOf(" Local")));
                                if (objSms.getValor().indexOf(" EM ")>-1){
                                    objSms.setValorReal(objSms.getValor().substring(0,objSms.getValor().indexOf(" EM ")));
                                }else{
                                    objSms.setValorReal(objSms.getValor());
                                }
                                listaSms.add(objSms);
                            } catch (Exception e) {

                            }

                        }else  if (objSms.getMsg().indexOf("CAIXA informa: Saque com cartao de debito") > -1) {
                            //objSms.setMsg("CAIXA informa: Saque com cartao de debito, 1.000,00, conta 94-8, 11/02/2017 as 14:43, ATM. Duvidas: 3004-1104 / 0800-726-0104");
                            try {
                                String data = c.getString(c.getColumnIndexOrThrow("date"));
                                Calendar d = Calendar.getInstance();
                                d.setTimeInMillis(Long.valueOf(data));
                                objSms.setBanco("CAIXA");
                                objSms.setFinalCartao("Conta corrente");
                                objSms.setLoja("Saque com cartao de debito");
                                objSms.setDataCompra(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(d.getTime()));
                                objSms.setValor(objSms.getMsg().substring(objSms.getMsg().indexOf("debito, ") + 8, objSms.getMsg().indexOf(" conta")-1).replace(".",""));
                                if (objSms.getValor().indexOf(" EM ")>-1){
                                    objSms.setValorReal(objSms.getValor().substring(0,objSms.getValor().indexOf(" EM ")));
                                }else{
                                    objSms.setValorReal(objSms.getValor());
                                }
                                listaSms.add(objSms);

                            } catch (Exception e) {

                            }
//BB: Compra SELVA GASTRONOM. Cartao final 2546. RS 97,71. 05/03. 00:41. Responda BL2546 se quiser bloquear cartao Lim disp RS 1.769
                        }else  if (objSms.getMsg().indexOf("BB: Compra ") > -1) {
                            try {
                                //objSms.setMsg("BB: Compra SELVA GASTRONOM. Cartao final 2546. RS 97,71. 05/03. 00:41. Responda BL2546 se quiser bloquear cartao Lim disp RS 1.769");
                                //  BB: compra RS  50,00 ANTON0O FAIANO SOUSA cartao final 2546 em 04/03/17.

                                String data = c.getString(c.getColumnIndexOrThrow("date"));
                                Calendar d = Calendar.getInstance();
                                d.setTimeInMillis(Long.valueOf(data));
                                objSms.setBanco("Banco Brasil");
                                objSms.setFinalCartao(objSms.getMsg().substring(objSms.getMsg().indexOf("Cartao final ") + 13, objSms.getMsg().indexOf(". RS")));
                                objSms.setLoja(objSms.getMsg().substring(objSms.getMsg().indexOf("BB: Compra ") + 11, objSms.getMsg().indexOf("Cartao final")));
                                objSms.setDataCompra(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(d.getTime()));
                                String msgCorte = objSms.getMsg().substring(objSms.getMsg().indexOf(" RS ") + 4, objSms.getMsg().length());
                                objSms.setValor(msgCorte.substring(0, msgCorte.indexOf(". ")).replace(".", ""));
                                if (objSms.getValor().indexOf(" EM ") > -1) {
                                    objSms.setValorReal(objSms.getValor().substring(0, objSms.getValor().indexOf(" EM ")));
                                } else {
                                    objSms.setValorReal(objSms.getValor());
                                }
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

