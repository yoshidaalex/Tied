package com.tied.android.tiedapp.ui.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.HelpModel;
import com.tied.android.tiedapp.ui.adapters.HelpAdapter;

import java.util.ArrayList;

public class HelpActivityFragment extends Fragment implements AdapterView.OnItemClickListener , View.OnClickListener{

    ListView list;
    private RelativeLayout done;

    int[] IMAGE = {R.mipmap.email_icon, R.mipmap.email_icon, R.mipmap.email_icon, R.mipmap.email_icon, R.mipmap.email_icon};
    String[] TITLE = {"How does Tied work?","How can I add multiple clients?","Who can use Tied?","Is Tied that helpful?","How much does it cost?"};
    //    String [] DESCRIPTION = getResources().getStringArray(R.array.help_description);
    String [] DESCRIPTION = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididun"};

    private ArrayList<HelpModel> helpArrayList;
    private HelpAdapter helpAdapter;
    ProgressBar progressBar;
    Bundle bundle;
    int index = 0;

    public HelpActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }


    public void initComponent(View view) {
        list= (ListView) view.findViewById(R.id.list);
        done = (RelativeLayout) view.findViewById(R.id.bottom_layout);
        done.setOnClickListener(this);
        helpArrayList = new ArrayList<HelpModel>();
        progressBar=(ProgressBar)view.findViewById(R.id.progress_bar) ;

        bundle = getArguments();
        if(bundle != null){
            index = bundle.getInt(Constants.PREVIOUS);
        }
        WebView myWebView = (WebView) view.findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyWebViewClient(progressBar));
        myWebView.loadUrl(Constants.HOST+"help");
        /*
        for (int i = 0; i < TITLE.length; i++) {

            HelpModel helpClass = new HelpModel(TITLE[i], DESCRIPTION[i],IMAGE[i]);
            helpArrayList.add(helpClass);

        }
        helpAdapter = new HelpAdapter(getActivity(), helpArrayList);
        list.setAdapter(helpAdapter);
*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottom_layout:
                getActivity().finish();
                break;
        }
    }
    private class MyWebViewClient extends WebViewClient {
        private ProgressBar progressBar;

        public MyWebViewClient (ProgressBar progressBar) {
            this.progressBar=progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
           // myWebView.loadUrl(Constants.HOST+"/help");
            if (Uri.parse(url).getHost().equals(Constants.HOST)) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
       /* @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            if (uri.getHost().equals(Constants.HOST)) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }*/

    }
}
