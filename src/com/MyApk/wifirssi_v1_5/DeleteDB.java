package com.MyApk.wifirssi_v1_5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteDB extends ListActivity {
	private Button button;
	private MyAdapter adapter;
	private List<Map<String, Object>> tableName = new ArrayList<Map<String, Object>>();
	private MyDB db;
	private String chooseTable = "ghj";
	private Toast toast;
	private String deleteTable;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deletedb);
		ExitApp.getInstance().addActivity(this);
		db = new MyDB(this);
		tableName = db.tableNameQuery();
		adapter = new MyAdapter(this);
		setListAdapter(adapter);
		button = (Button) findViewById(R.id.buttonback);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(DeleteDB.this, MainActivity.class);
				intent.putExtra("Tablename", chooseTable);
				startActivity(intent);
			}
		});

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		chooseTable = (String)tableName.get(position).get("title");
		if (toast == null) {
			toast = Toast.makeText(DeleteDB.this, chooseTable,
					Toast.LENGTH_SHORT);
		} else {
			toast.setText(chooseTable);
		}
		toast.show();

	}

	public final class ViewHolder {
		// public ImageView img;
		public TextView title;
		public TextView info;
		public Button viewBtn;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return tableName.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		/**
		 * listView在开始绘制的时候，系统首先调用getCount（）函数，根据他的返回值得到listView的长度
		 * 然后根据这个长度，调用getView（）逐一绘制每一行。 如果你的getCount（）返回值是0的话，列表将不显示同样return
		 * 1，就只显示一行。 　系统显示列表时，首先实例化一个适配器。
		 * 
		 * 当手动完成适配时，必须手动映射数据，这需要重写getView（）方法。 系统在绘制列表的每一行的时候都将调用此方法，（可以设个变量试试）
		 * getView()有三个参数 position表示将显示的是第几行 covertView是从布局文件中inflate来的布局
		 * 我们用LayoutInflater的方法将定义好的vlist2.xml文件提取成View实例用来显示。
		 * 然后将xml文件中的各个组件实例化（简单的findViewById()方法）。 这样便可以将数据对应到各个组件上了
		 * 
		 * 但是按钮为了响应点击事件，需要为它添加点击监听器，这样就能捕获点击事件。 至此一个自定义的listView就完成了
		 * 
		 * 现在让我们回过头从新审视这个过程 系统要绘制ListView了，他首先获得要绘制的这个列表的长度，然后开始绘制第一行，怎么绘制呢？
		 * 调用getView()函数。在这个函数里面首先获得一个View（实际上是一个ViewGroup） 然后再实例并设置各个组件并显示。
		 * 绘制完这一行了。再绘制下一行，直到绘完为止。
		 */
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.item, null);
				
				holder.title = (TextView) convertView
						.findViewById(R.id.listitem3ItemTitle);
				holder.info = (TextView) convertView
						.findViewById(R.id.listitem3ItemText);
				holder.viewBtn = (Button) convertView
						.findViewById(R.id.listitem3button);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			holder.title.setText((String) tableName.get(position).get("title"));
			holder.info.setText((String) tableName.get(position).get("info"));
			holder.viewBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// showInfo(position);
					deleteTable = "DROP TABLE " + chooseTable;
					db.deleteTable(deleteTable);
					tableName.remove(position);
					// 通过程序我们知道删除了，但是怎么刷新ListView呢？
					// 只需要重新设置一下adapter
					setListAdapter(adapter);
				}
			});
			return convertView;
		}
	}
}
