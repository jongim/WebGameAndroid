package com.example.joypad;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	EditText email;
	EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		startActivity(new Intent(this, Splash_Activity.class));
		Button loginBtn = (Button) findViewById(R.id.loginBtn);
		email = (EditText) findViewById(R.id.idInput);
		password = (EditText) findViewById(R.id.passwordInput);

		loginBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Thread thread = new Thread() {
					public void run() {
						HttpClient httpClient = new DefaultHttpClient();

						String urlString = "http://210.118.74.117:3000/LOGINMOBILE";
						try {
							URI url = new URI(urlString);

							HttpPost httpPost = new HttpPost();
							httpPost.setURI(url);

							List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(
									2);
							nameValuePairs.add(new BasicNameValuePair("userId",
									email.getText().toString()));
							nameValuePairs.add(new BasicNameValuePair(
									"password", password.getText().toString()));

							httpPost.setEntity(new UrlEncodedFormEntity(
									nameValuePairs));

							HttpResponse response = httpClient
									.execute(httpPost);
							String responseString = EntityUtils.toString(
									response.getEntity(), HTTP.UTF_8);

							JSONObject responseJSON = new JSONObject(
									responseString);

							Log.d("Test", responseJSON.get("status").toString());

							if (responseJSON.get("status").toString()
									.equals("200")) {
								setPreference();
								Intent i = new Intent();
								i.setClassName("com.example.joypad",
										"com.example.joypad.MainActivity");
								startActivity(i);
								finish();
							}
						} catch (URISyntaxException e) {
							e.printStackTrace();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				};
				thread.start();
			}
		});
	}

	private void setPreference() {
		SharedPreferences prefs = getSharedPreferences("userId", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("userId", email.getText().toString());
		editor.commit();
	}
}
