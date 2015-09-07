package com.MyApk.wifirssi_v1_5;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//简单handler进行wifi扫描
public class MainActivity extends Activity {

	// 数据库相关变量定义
	private SQLiteDatabase mMyDbDatabase = null;
	private final static String DB_NAME = "Pos.db";
	 private final static String sqlString =
	 "create table pos(id integer primary key autoincrement,ap1 varchar(100),ap2 varchar(100),ap3 varchar(100),location varchar(50))";
	private String[] nameString = { "test1", "test2", "test3" };
	private Button button1, button2, button3, button4;
	private TextView textView2, textView3,textView4;
	private WifiManager manager;
	private ProgressDialog dialog;
	private String chooseTable = "pos";
	// 扫描平均值
	private int[] level;
	private Mytools mytools = new Mytools(this);
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int[] obje = (int[]) msg.obj;
			String levelString = "";
			levelString +="|" + nameString[0].toString()
					+ obje[0] + "|" + nameString[1].toString() + obje[1] + "|"
					+ nameString[2].toString() + obje[2]+"|";
			textView2.setText(levelString);
			String location = locationClient(obje, chooseTable);
			textView3.setText(location);
			dialog.dismiss();

		}
	};

	// 创建数据库


	// try {
	// mMyDbDatabase.execSQL(sqlString);
	// } catch (Exception e) {
	// System.err.println(e.toString());
	// // TODO: handle exception
	// }//运行SQL语句
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ExitApp.getInstance().addActivity(this);
		 try {
			 mMyDbDatabase = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE,
			 null);
			 mMyDbDatabase.execSQL(sqlString);
			 } catch (Exception e) {
			 // TODO: handle exception
			 }
		Intent intent = getIntent();
		chooseTable = intent.getStringExtra("Tablename");
		// 扫描按钮
		button2 = (Button) findViewById(R.id.button2);
		// // 得到数据库表信息
		textView4 = (TextView)findViewById(R.id.Text01);
		textView4.setText(chooseTable);
		// 得到wifilevel信息
		textView2 = (TextView) findViewById(R.id.TextView2);
		// 文本滚动
		textView2.setMovementMethod(new ScrollingMovementMethod());
		// 得到坐标信息
		textView3 = (TextView) findViewById(R.id.TextView3);
		// 文本滚动
		textView3.setMovementMethod(new ScrollingMovementMethod());
		// wifimanager
		manager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		// dialog属性
		dialog = new ProgressDialog(this);
		dialog.setTitle("提示信息");
		dialog.setMessage("正在定位,请稍后...");
		// 定位
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (manager.isWifiEnabled()) {
					dialog.show();
					new Thread() {
						public void run() {
							level = mytools.myscan(manager, nameString);
							Message message = new Message();
							message.obj = level;
							handler.sendMessage(message);
						};
					}.start();
				} else
					Toast.makeText(MainActivity.this, "请打开Wifi",
							Toast.LENGTH_SHORT).show();
			}
		});

		// 退出按钮
		button3 = (Button) this.findViewById(R.id.Button3);
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ExitApp.getInstance().onTerminate();
			}
		});

		// 跳转创建页面
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, CreateDB.class);
				startActivity(intent);
			}
		});
		//跳转删除页面
		button4 = (Button)findViewById(R.id.button4);
		button4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, DeleteDB.class);
				startActivity(intent);
			}
		});
	}

	// 查询RSSI函数
	public Cursor queryRssi(String[] selArgs, String table) {
		Cursor cursor = null;
		try {
			mMyDbDatabase = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE,
					null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			String sql = "select * from " + table;
			cursor = mMyDbDatabase.rawQuery(sql, selArgs);
			cursor.moveToFirst();
			// ceshi
			try {
				cursor.moveToFirst();
				// String aa=cursor.getString(3);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (mMyDbDatabase != null) {
				mMyDbDatabase.close();
			}
		}
		return cursor;
	}

	// 定位函数
	public String locationClient(int[] level, String tableName) {

		Cursor cursor = null;
		cursor = queryRssi(null, tableName);// 将数据库数据取出放进cursor中
		cursor.moveToFirst();
		int mini_dis = 0;// 最小RSSI距离
		String min_dis_pos = "no loc";
		String aa;
		try {
			min_dis_pos = cursor.getString(cursor.getColumnIndex("location"));// 最小RSSI距离对应的坐标
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		String[] ap1 = cursor.getString(1).split(",");
		String[] ap2 = cursor.getString(2).split(",");
		String[] ap3 = cursor.getString(3).split(",");
		// 判断所在区域中是否包含有指纹数据库中的指定的热点
		if (level[0] != 0 && level[1] != 0 && level[2] != 0) {
			mini_dis = Math.abs(level[0] - Integer.parseInt(ap1[1]))
					+ Math.abs(level[1] - Integer.parseInt(ap2[1]))
					+ Math.abs(level[2] - Integer.parseInt(ap3[1]));
			while (cursor.moveToNext()) {
				ap1 = cursor.getString(1).split(",");
				ap2 = cursor.getString(2).split(",");
				ap3 = cursor.getString(3).split(",");
				int temp_dis = Math.abs(level[0] - Integer.parseInt(ap1[1]))
						+ Math.abs(level[1] - Integer.parseInt(ap2[1]))
						+ Math.abs(level[2] - Integer.parseInt(ap3[1]));
				if (temp_dis < mini_dis) {
					mini_dis = temp_dis;
					min_dis_pos = cursor.getString(cursor
							.getColumnIndex("location"));
				}
			}
			// 在textview上显示定位的坐标
			aa = min_dis_pos;
		} else {
			// 如果当前扫描不到全部的指纹数据库中的热点，则在TextView上显示无热点
			textView3.setText("扫描不到全部的热点！");
			aa = "扫描不到全部的热点！";
		}
		return aa;
	}

	@Override
	protected void onPause() {
		// unregisterReceiver(receiverWifi);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}