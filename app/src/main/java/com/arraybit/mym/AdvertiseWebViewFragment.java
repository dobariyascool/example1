package com.arraybit.mym;


import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdvertiseWebViewFragment extends Fragment {

    WebView wvAdvertise;

    public AdvertiseWebViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advertise_web_view, container, false);
        try {
//            Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
//            if (toolbar != null) {
//                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
////                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
//            }
//            setHasOptionsMenu(true);
            Bundle bundle = getArguments();
            String url = null;
            if (bundle != null) {
                url = bundle.getString("url");
            }
            ImageView ivCancle  =(ImageView) view.findViewById(R.id.ivCancle);

            Log.e("url", " " + url);
            wvAdvertise = (WebView) view.findViewById(R.id.wvAdvertise);
            wvAdvertise.getSettings().setJavaScriptEnabled(true);
            wvAdvertise.getSettings().setLoadsImagesAutomatically(true);
            wvAdvertise.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            wvAdvertise.setWebViewClient(new WebViewClient() {
                ProgressDialog progressDialog;

                //If you will not use this method url links are opeen in new brower not in webview
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                //Show loader on url load
                public void onLoadResource(WebView view, String url) {
                    if (progressDialog == null) {
                        // in standard case YourActivity.this
                        progressDialog = new ProgressDialog();
                        progressDialog.show(getActivity().getSupportFragmentManager(), "");
                    }
                }

                public void onPageFinished(WebView view, String url) {
                    try {
                            progressDialog.dismiss();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                }
            });

            wvAdvertise.loadUrl(url);

            ivCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {
                        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("Advertisement")) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (getActivity() instanceof DetailActivity) {
            menu.findItem(R.id.share).setVisible(false);
            menu.findItem(R.id.changePassword).setVisible(false);
            menu.findItem(R.id.saveContact).setVisible(false);
            menu.findItem(R.id.accept).setVisible(false);
            menu.findItem(R.id.cancle).setVisible(false);
        } else if (getActivity() instanceof HomeActivity) {
//            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.memberRequest).setVisible(false);
            menu.findItem(R.id.notification).setVisible(false);
            menu.findItem(R.id.memberFilter).setVisible(false);
        }

        super.onPrepareOptionsMenu(menu);
    }


}
