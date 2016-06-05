package org.t_robop.matsu.sharetien;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.core.services.params.Geocode;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetViewFetchAdapter;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class TimelineActivity extends ListActivity {

    //表示ツイート数の設定
    final int TWEET_NUM = 10;
    TwitterApiClient twitterApiClient;

    //アダプターの設定
    final TweetViewFetchAdapter adapter =
            new TweetViewFetchAdapter<CompactTweetView>(
                    TimelineActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //アダプターをセット
        setListAdapter(adapter);
        //データを読み込む
        twitterApiClient = TwitterCore.getInstance().getApiClient();

        /**
        //自分のタイムラインを表示するとき
        homeTimeline();
         */

        //タグ検索したデータを表示するとき
        search();
    }

    /**
    //自分のタイムラインを表示するメソッド
    void homeTimeline(){
        // statusAPI用のserviceクラス
        StatusesService statusesService = twitterApiClient.getStatusesService();
        //ログインユーザーのタイムラインを表示する
        statusesService.homeTimeline(TWEET_NUM, null, null, false, false, false, false,
                new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        Log.d("aaa", String.valueOf(listResult));
                        adapter.setTweets(listResult.data);
                    }

                    @Override
                    public void failure(TwitterException e) {
                    }
                });
    }
     */
    void search (){
        //検索で使う
        SearchService searchService = twitterApiClient.getSearchService();
        //指定したワードを使った検索
        /*
        データ形式
        String 検索したい文字列, GeoCode 緯度経度?,String 検索したい文字列1,String 検索したい文字列2,String 検索したい文字列3,
        Integer 取得するツイート数,String 検索したい文字列4,Long 検索したいLong?時間?,Long 検索したいLong1?時間?Boolean 謎,Callback 返り値
        */

        /**
         * 緯度,経度,距離　最後のは距離の単位の指定
         */
        Geocode a = new Geocode(35.637867,139.734576,4, Geocode.Distance.KILOMETERS);

        searchService.tweets("てすと",a, null, null, null, TWEET_NUM, null, null, null, false, new Callback<Search>() {
            //成功した時
            @Override
            public void success(Result<Search> listResult) {
                Log.d("aa",String.valueOf(listResult.data.tweets.get(0).coordinates.getLatitude()));
                if(listResult.data.tweets.equals("[]")){
                    toastMake("データがありません",0,-200);
                    Log.d("ii",String.valueOf(listResult.data.tweets));
                }
                //listResult.data.tweets

                adapter.setTweets(listResult.data.tweets);

            }
            @Override
            public void failure(TwitterException e) {
            }
        });
        Log.d("TEST",searchService.toString());


    }
    //Toastを出力させるメソッド
    private void toastMake(String message, int x, int y){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER, x, y);
        toast.show();
    }
}