package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public final int TWEET_MAX_LENGTH = 140;
    public EditText txCompose;
    public Button btCompose;
    public TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);
        txCompose = (EditText) findViewById(R.id.txCompose);
        btCompose = findViewById(R.id.btTweet);

        btCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = txCompose.getText().toString();
                if(tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry, tweet can not be empty!", Toast.LENGTH_LONG).show();
                    return;
                } else if(tweetContent.length() > TWEET_MAX_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Sorry, tweet is too long!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Tweet tweet = null;
                            try {
                                tweet = Tweet.fromJSON(json.jsonObject);
                                Intent intent = new Intent();
                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                setResult(RESULT_OK, intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                        }
                    });
                }
            }
        });
    }
}