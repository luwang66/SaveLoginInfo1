package com.example.savelogininfo1;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button login;// 登陆按钮
	private CheckBox rememberPsdBox, autoLoginBox;// 记住密码、自动登陆复选框
	private EditText name, psd;// 用户名和密码
	private TextView userInfo;
	SharedPreferences loginPreferences, accessPreferences;// 保存登陆信息和访问次数
	SharedPreferences.Editor loginEditor, accessEditor;// 对应的编辑器
	String userName;
	String userPsd;
	boolean isSavePsd,isAutoLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loginPreferences=getSharedPreferences("login",Context.MODE_PRIVATE);
		accessPreferences = getSharedPreferences("access",Context.MODE_WORLD_READABLE);
		int count=accessPreferences.getInt("count",1);
		Toast.makeText(MainActivity.this,"欢迎您，这是第"+count+"次访问！", Toast.LENGTH_LONG).show();
		loginEditor = loginPreferences.edit();
		accessEditor = accessPreferences.edit();
		accessEditor.putInt("count",++count);
		accessEditor.commit();
		userName = loginPreferences.getString("name", null);
		userPsd = loginPreferences.getString("psd", null);
		isSavePsd=loginPreferences.getBoolean("isSavePsd",false);
		isAutoLogin=loginPreferences.getBoolean("isAutoLogin", false);
		System.out.println("userName=" + userName + ",userPsd=" + userPsd);
		if (isAutoLogin) {
			this.setContentView(R.layout.activity_welcome);
			userInfo = (TextView) findViewById(R.id.userInfo);
			userInfo.setText("欢迎您：" + userName + ",登陆成功！");
		} else{
			loadActivity();
		}
	}
	
	public void loadActivity() {
		this.setContentView(R.layout.activity_main);
		login = (Button) findViewById(R.id.login);
		rememberPsdBox = (CheckBox) findViewById(R.id.rememberPsd);
		autoLoginBox = (CheckBox) findViewById(R.id.autoLogin);
		name = (EditText) findViewById(R.id.name);
		psd = (EditText) findViewById(R.id.psd);
		if (isSavePsd) {
			psd.setText(userPsd);
			name.setText(userName);
			rememberPsdBox.setChecked(true);
		}
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loginEditor.putString("name", name.getText().toString());
				loginEditor.putString("psd", psd.getText().toString());
				loginEditor.putBoolean("isSavePsd", rememberPsdBox.isChecked());
				loginEditor.putBoolean("isAutoLogin", autoLoginBox.isChecked());
				loginEditor.commit();
				MainActivity.this.setContentView(R.layout.activity_welcome);
				userInfo = (TextView) findViewById(R.id.userInfo);
				userInfo.setText("欢迎您：" + name.getText().toString() + ",登陆成功！");
			}
		});
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings://单击注销菜单
			loginEditor.putBoolean("isAutoLogin", false);
			loginEditor.commit();
			onCreate(null);
			break;
		case R.id.exit://单击退出按钮
			this.finish();
			break;
		default:
			break;
		}
		return true;
	}
}

