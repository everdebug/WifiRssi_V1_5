package com.MyApk.wifirssi_v1_5;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateDB extends Activity {
	private Button buttonback, buttonselect, buttoninsert, buttonfinish;
	private WifiManager manager;
	private List<ScanResult> wifiList;
	private TextView textView, textViewstate;
	private String[] ap = { "test1", "test2", "test3" };
	private int[] level;
	private EditText editText1, editText2, editText3, editText4, editText5,
			editText6;
	private Mytools mytools;
	private String x, y;
	private MyDB db;
	private String tableName;
	private int j = 0;
	private Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.createdb);
		ExitApp.getInstance().addActivity(this);
		buttonback = (Button) findViewById(R.id.button2);
		buttonback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (j > 0) {
					Intent intent = new Intent();
					intent.setClass(CreateDB.this, MainActivity.class);
					startActivity(intent);
				} else
					Toast.makeText(CreateDB.this, "请至少插入一次数据",
							Toast.LENGTH_SHORT).show();
			}
		});

		db = new MyDB(this);
		mytools = new Mytools(this);
		textView = (TextView) findViewById(R.id.textView1);
		textView.setMovementMethod(new ScrollingMovementMethod());
		manager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		// ap1
		editText1 = (EditText) findViewById(R.id.EditText02);
		// ap2
		editText2 = (EditText) findViewById(R.id.EditText01);
		// ap3
		editText3 = (EditText) findViewById(R.id.editText1);
		// DBName
		editText4 = (EditText) findViewById(R.id.EditText05);
		// x
		editText5 = (EditText) findViewById(R.id.EditText03);
		// y
		editText6 = (EditText) findViewById(R.id.EditText04);
		editText1.setEnabled(false);
		editText2.setEnabled(false);
		editText3.setEnabled(false);
		buttonselect = (Button) findViewById(R.id.button4);
		buttonselect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (manager.isWifiEnabled()) {
					manager.startScan();
					StringBuilder sb = new StringBuilder();
					wifiList = manager.getScanResults();
					for (int i = 0; i < wifiList.size(); i++) {
						sb.append((i + 1) + ".");
						sb.append((wifiList.get(i)).toString());
						sb.append("\n\n");
					}
					textView.setText(sb);
				} else
					Toast.makeText(CreateDB.this, "请打开Wifi", Toast.LENGTH_SHORT)
							.show();
			}
		});
		tableName = editText4.getText().toString();
		// 次数
		textViewstate = (TextView) findViewById(R.id.textView3);
		buttoninsert = (Button) findViewById(R.id.button3);
		buttoninsert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (manager.isWifiEnabled()) {
					x = editText5.getText().toString();
					y = editText6.getText().toString();
					level = mytools.myscan(manager, ap);
					ContentValues values = new ContentValues();
					values.put("ap1", ap[0] + "," + level[0]);
					values.put("ap2", ap[1] + "," + level[1]);
					values.put("ap3", ap[2] + "," + level[2]);
					values.put("location", x + "," + y);
					db.insert(tableName, values);
					j++;
					if (toast == null) {
						toast = Toast.makeText(CreateDB.this, "插入成功",
								Toast.LENGTH_SHORT);
					} else {
						toast.setText("插入成功");
					}
					toast.show();
					textViewstate.setText("已经插入数据" + j + "次");
				} else
					Toast.makeText(CreateDB.this, "请打开Wifi", Toast.LENGTH_SHORT)
							.show();
			}
		});

		// new table
		buttonfinish = (Button) findViewById(R.id.button1);
		buttonfinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tableName = editText4.getText().toString();
				if (!tableName.equals("")) {
					String SQL = "create table if not exists "
							+ tableName
							+ "(id integer primary key autoincrement,ap1 varchar(100),ap2 varchar(100),ap3 varchar(100),location varchar(50))";
					db.createTable(SQL);
					editText4.setEnabled(false);
				} else
					Toast.makeText(CreateDB.this, "请输入数据库名称",
							Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
