package org.t_robop.matsu.sharetien;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.internal.TwitterCollection;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements LocationListener{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Nqt9lJurtSTsU1MD36LrzyD06";
    private static final String TWITTER_SECRET = "GSbJVYO5PbyaLa763jZnkLoX2dtTvxkkhkb38MhPk5dbKkDQkc";

    //GPS用
    private LocationManager mLocationManager;


    //ログインボタンの宣言
    private TwitterLoginButton loginButton;

    String userData = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        //GPS
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsFlg = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d("GPS Enabled", gpsFlg ? "OK" : "NG");

        // Criteriaオブジェクトを生成
        Criteria criteria = new Criteria();

        // Accuracyを指定(低精度)
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        // PowerRequirementを指定(低消費電力)
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        // ロケーションプロバイダの取得
        String provider = mLocationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(provider, 0, 0, this);
        //ログインボタンの実装
        //Twitterのボタンの関連付け
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        //押した後のコールバック
        loginButton.setCallback(new Callback<TwitterSession>() {
            //成功した時
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model

                userData = "@" + session.getUserName();
                Log.d("AAAAA", userData);
                //ログインしたTwitter名とIDをToastで出力
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            //失敗した時
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }


    // GPSボタン
    public void onBtnGpsClicked(View view) {

        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, //LocationManager.NETWORK_PROVIDER,
                3000, // 通知のための最小時間間隔（ミリ秒）
                10, // 通知のための最小距離間隔（メートル）
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        String msg = "Lat=" + location.getLatitude()
                                + "\nLng=" + location.getLongitude();
                        Log.d("GPS", msg);
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mLocationManager.removeUpdates(this);

                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                }
        );



//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLocationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER, //LocationManager.NETWORK_PROVIDER,
//                3000, // 通知のための最小時間間隔（ミリ秒）
//                10, // 通知のための最小距離間隔（メートル）
//                new LocationListener() {
//                    @Override
//                    public void onLocationChanged(Location location) {
//                        String msg = "Lat=" + location.getLatitude()
//                                + "\nLng=" + location.getLongitude();
//                        Log.d("GPS", msg);
//                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        mLocationManager.removeUpdates(this);
//
//                    }
//
//                    @Override
//                    public void onProviderDisabled(String provider) {
//                    }
//
//                    @Override
//                    public void onProviderEnabled(String provider) {
//                    }
//
//                    @Override
//                    public void onStatusChanged(String provider, int status, Bundle extras) {
//                    }
//                }
//        );
    }


    //Twitter認証から帰ってきた時に呼ばれる
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);

        TextView userName = (TextView)findViewById(R.id.textView);
        if(data == null){
            userName.setText("ログインID:なし");
        }else{
            Log.d("AAA",this.userData);
            userName.setText("ログインID:"+ userData);
        }
    }



    //Tweet画面にIntentする
    public void TweetBtn(View view) {
        Intent intent = new Intent();
        intent.setClass(this,TweetActivity.class);
        startActivity(intent);
    }

    //タイムライン表示画面にIntentする
    public void TimelineBtn(View view) {
        Intent intent = new Intent();
        intent.setClass(this,TimelineActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
