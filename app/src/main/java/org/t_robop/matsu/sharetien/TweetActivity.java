package org.t_robop.matsu.sharetien;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
                                /*Double 緯度*/a,
                                /*Double 経度*/i,
                                                null, null, null, new Callback<Tweet>() {

            //成功した場合
            @Override
            public void success(Result<Tweet> tweetResult) {
                Log.d("ツイート完了","");
            }

            //失敗した場合
            @Override
            public void failure(TwitterException e) {
                Log.d("ツイート失敗","");
            }
        });
    }
}