package com.run.ultimate_fitness.ui.appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.run.ultimate_fitness.R;

public class AppointmentsFragment extends Fragment {
    private WebView mWebView;

    private AppointmentsViewModel mViewModel;

    public static AppointmentsFragment newInstance() {
        return new AppointmentsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);

        mWebView = (WebView) view.findViewById(R.id.webView_Appointments);
        mWebView.loadUrl("https://www.monash.edu");

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Forces the browser to open in the application instead of external browser
        mWebView.setWebViewClient(new WebViewClient());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AppointmentsViewModel.class);
        // TODO: Use the ViewModel
    }

}