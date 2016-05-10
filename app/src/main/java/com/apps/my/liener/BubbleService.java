package com.apps.my.liener;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BubbleService extends Service{
    WindowManager bubbleWindow;
    ImageView bubbleHead;
    WebView browser;
    WindowManager.LayoutParams paramBubble,paramBrowser;
    boolean is_open = false;
    int paramx = 0 ,paramy = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        bubbleWindow = (WindowManager) getSystemService(WINDOW_SERVICE);

        browser = new WebView(this);
        browser.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        browser.setBackgroundColor(Color.WHITE);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        browser.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });
        browser.loadUrl("http://www.google.com");

        bubbleHead = new ImageView(this);
        bubbleHead.setImageResource(R.mipmap.bubble); //set Bubble Icon

        paramBubble = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        paramBubble.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        paramBubble.x = 0;
        paramBubble.y = 1000;

        paramBrowser = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.FILL_PARENT,
                1000,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        paramBrowser.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        bubbleHead.setOnTouchListener(new View.OnTouchListener() {
            int initialX;
            int initialY;
            float initialTouchX;
            float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!is_open) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = paramBubble.x;
                            initialY = paramBubble.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return false;
                        case MotionEvent.ACTION_UP:
                            return false;
                        case MotionEvent.ACTION_MOVE:
                            paramBubble.x = initialX
                                    - (int) (event.getRawX() - initialTouchX);
                            paramBubble.y = initialY
                                    - (int) (event.getRawY() - initialTouchY);
                            bubbleWindow.updateViewLayout(bubbleHead, paramBubble);
                            return false;
                    }
                }
                return false;
            }
        });

        bubbleHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!is_open) {
                    paramBubble.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                    paramx = paramBubble.x;
                    paramy = paramBubble.y;
                    paramBubble.x = 0;
                    paramBubble.y = 1000;
                    bubbleWindow.updateViewLayout(bubbleHead, paramBubble);
                    bubbleWindow.addView(browser, paramBrowser);
                    is_open = true;
                } else {
                    paramBubble.x = paramx;
                    paramBubble.y = paramy;
                    bubbleWindow.removeView(browser);
                    bubbleWindow.updateViewLayout(bubbleHead, paramBubble);
                    is_open = false;
                }
            }
        });
        bubbleWindow.addView(bubbleHead, paramBubble);
    }

    public void onDestroy() {
        super.onDestroy();
        if (bubbleHead != null) {
            bubbleWindow.removeView(bubbleHead);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}