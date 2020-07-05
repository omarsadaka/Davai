package com.technosaab.newdavai.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.technosaab.newdavai.R;

public class PaymentTypeFragment extends Fragment {
    ImageView paymentImage;
    TextView paymentName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_payment_type, container, false);
        paymentImage = view.findViewById(R.id.payment_icon);
        paymentName = view.findViewById(R.id.payment_name);
        checkPayment();
        return view;
    }

    public void checkPayment(){

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            String paymentWord = bundle.getString("paymentType");
            if (paymentWord.equals("payPal")){
                paymentImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.paypal));
                paymentName.setText(getString(R.string.paypal));
            }else if (paymentWord.equals("visa")){
                paymentImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.visa));
                paymentName.setText(getString(R.string.visa));
            }else if (paymentWord.equals("masterCard")){
                paymentImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mastercard));
                paymentName.setText(getString(R.string.master_card));
            }else if (paymentWord.equals("amExpress")){
                paymentImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.american_express));
                paymentName.setText(getString(R.string.am_express));
            }

        }
    }

}
