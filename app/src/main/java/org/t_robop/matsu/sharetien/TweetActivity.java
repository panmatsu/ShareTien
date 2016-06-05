package org.t_robop.matsu.sharetien;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.vision.barcode.Barcode;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

public class TweetActivity extends AppCompatActivity {

    //投稿用のtextView
    EditText TwitterText;
    //Tweetを入れる用
    String TweetStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        //Tweetを入力するeditTextの関連付け
        TwitterText = (EditText)findViewById(R.id.editText);
    }

    //現在地取得ボタンを押した時の処理
    public void onClick(View view){
        //LocationManagerの取得
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        //GPSから現在地の情報を取得
        Location myLocate = locationManager.getLastKnownLocation("gps");
        Log.d("TEST",String.valueOf(myLocate));
        //MapControllerの取得
        //GoogleMap MapCtrl = mapView.getController();
        if(myLocate != null){
            //現在地情報取得成功
            //緯度の取得
            int latitude = (int) (myLocate.getLatitude() * 1e6);
            //経度の取得
            int longitude = (int) (myLocate.getLongitude() * 1e6);
            //GeoPointに緯度・経度を指定
            //Barcode.GeoPoint GP = new Barcode.GeoPoint(latitude, longitude);
            //現在地までアニメーションで移動
            //MapCtrl.animateTo(GP);
            //現在地までパッと移動
            //MapCtrl.setCenter(GP);
        }else{
            //現在地情報取得失敗時の処理
            Toast.makeText(this, "現在地取得できませーん！", Toast.LENGTH_SHORT).show();
        }
    }

    //ツイートボタンをおした時の処理
    public void Tweet(View view) {
        TweetStr = String.valueOf(TwitterText.getText());
        StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();
        /**
         * データ形式　String　ついっとする文字列、Long　リプライ先のid、Boolean　？、Double　緯度、Double　経度、String　場所
         *
         *
         */
        double ido = 45.26;
        double keido = 12.02;

        Double a = new Double(ido);
        Double i = new Double(keido);



        String placeId = "5a110d312052166f";
        statusesService.update(TweetStr,
                                /*Long リプライ先ID*/null,
                                /*Boolean ?*/null,
                                /*Double 緯度*/null,
                                /*Double 経度*/null,
                                                null, null, null, new Callback<Tweet>() {

            //成功した場合
            @Override
            public void success(Result<Tweet> tweetResult) {
                Toast.makeText(getApplicationContext(), "ついっとしました", Toast.LENGTH_SHORT).show();
                TwitterText.setText(null);
            }

            //失敗した場合
            @Override
            public void failure(TwitterException e) {
                Log.d("ツイート失敗","");
            }
        });
    }
}