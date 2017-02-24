package com.andersonsilva.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andersonsilva.R;
import com.andersonsilva.entity.Sms;

import java.util.ArrayList;

/**
 * Created by anderson.silva on 14/02/2017.
 */
public class SmsAdapter  extends ArrayAdapter<Sms> implements View.OnClickListener{

        private ArrayList<Sms> dataSet;
        Context mContext;

    

    // View lookup cache
        private static class ViewHolder {
            TextView txtDataCompra;
            TextView txtLoja;
            TextView txtValor;

        }


    /**
     *
     * @param data
     * @param context

     */
        public SmsAdapter(ArrayList<Sms> data, Context context) {
            super(context, R.layout.item_sms, data);
            this.dataSet = data;
            this.mContext=context;

        }

        @Override
        public void onClick(View v) {

            int position=(Integer) v.getTag();
            Object object= getItem(position);
            Sms dataModel=(Sms)object;

           /* switch (v.getId())
            {
                case R.id.item_info:
                    Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    break;
            }*/
        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Sms dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_sms, parent, false);
                viewHolder.txtDataCompra = (TextView) convertView.findViewById(R.id.dataCompra);
                viewHolder.txtValor = (TextView) convertView.findViewById(R.id.valor);
                viewHolder.txtLoja = (TextView) convertView.findViewById(R.id.loja);
                result=convertView;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;

            viewHolder.txtLoja.setText(dataModel.getBanco()+" - "+ dataModel.getFinalCartao());
            viewHolder.txtDataCompra.setText(dataModel.getDataCompra());
            viewHolder.txtValor.setText(dataModel.getValor());
            //viewHolder.info.setOnClickListener(this);
           // viewHolder.info.setTag(position);
            // Return the completed view to render on screen
            return convertView;
        }

}
