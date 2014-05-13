package com.example.weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.example.absdemo.R;
import com.example.entity.Weather;
import com.example.util.Constants;
import com.example.util.DB;
import com.example.util.WeatherData;
import com.example.util.WeatherPic;
import com.example.util.WriteToSD;
import com.example.view.MyPagerAdapter;
import com.example.view.TrendView;

public class MainActivity extends SherlockActivity {
	//��߲˵���
	private MenuDrawer mMenuDrawer;
	private Weather weatherData;
	//ViewPager
	public ViewPager myViewPager;
	private MyPagerAdapter myAdapter;
	private LayoutInflater mInflater;
	private List<View> mListViews;
	private View layout1 = null;	//��һ������
	private View layout2 = null;	//�ڶ�������
	
	//��һ���������
	private TextView temperature;
	private TextView refreshTime;
	private TextView refreshDate;
	private TextView weather;
	private TextView wind;
	private TextView city;
	private TextView comfortable;
	private TextView tomorrowTemperature;
	private TextView tomorrowWeather;
	private ImageView weatherPic;
	
	//�ڶ����������
	private TrendView view;
	private TextView day1;
	private TextView day2;
	private TextView day3;
	private TextView day4;
	private TextView wea1;
	private TextView wea2;
	private TextView wea3;
	private TextView wea4;
	
	//����˵�����ַ�б�
	private List<Map<String, String>> addressList;
	private SimpleAdapter adapter;
	private ListView menuListView;
	
	private String id = "101190101";	//Ĭ���Ͼ�
	
	private Animation animation;	//���䶯��
	private LinearLayout layout;	//��������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		new WriteToSD(this);	//�����ݿ�д��SD��
		new Constants(this);
		
		initMenu();
		initPage();
		initAnim();
		initWidget();
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setLogo(R.drawable.icon);
		getSupportActionBar().setTitle("Weather");
		getSupportActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.base_actionbar_bg));
		setSupportProgressBarIndeterminateVisibility(false);	//���ؽ�������
		
	}
	//��ʼ���໬�˵�
	private void initMenu(){
    	mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW, Position.LEFT);	//WINDOW
        mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        mMenuDrawer.setContentView(R.layout.viewpager);
        mMenuDrawer.setMenuView(R.layout.menu);
        mMenuDrawer.setDropShadow(R.drawable.shadow);
        mMenuDrawer.setDropShadowSize((int)getResources().getDimension(R.dimen.shadow_width));
        mMenuDrawer.setMaxAnimationDuration(3000);
        mMenuDrawer.setHardwareLayerEnabled(false);
        mMenuDrawer.setMenuSize((int)getResources().getDimension(R.dimen.slidingmenu_offset));
    }
	//��ʼ����������
	private void initPage(){
		mListViews = new ArrayList<View>();
        mInflater = getLayoutInflater();
        layout1 = mInflater.inflate(R.layout.activity_main, null);
        layout2 = mInflater.inflate(R.layout.trend, null);
        //��������ӽ�ViewPager
        mListViews.add(layout1);
        mListViews.add(layout2);
		
        myViewPager = (ViewPager) findViewById(R.id.viewpagerLayout);
		myAdapter = new MyPagerAdapter(mListViews);
		myViewPager.setAdapter(myAdapter);
        
        myViewPager.setCurrentItem(0);	//��ʼ����ǰ��ʾ��view
        myViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:	
					mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
					break;
				case 1:	//������ǵ�һҳ������������Ч
					mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
					break;
                }
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	//��ʼ������
	private void initAnim(){
		// ���м�ͷ���䶯��
		ImageView imageView = (ImageView) layout1.findViewById(R.id.arrow);
		AnimationDrawable ad = (AnimationDrawable) imageView.getBackground();
		ad.start();
		
		// ͸���ȱ仯����
		animation = new AlphaAnimation(1, 0);
    	animation.setDuration(700);
    	animation.setRepeatCount(1);
    	animation.setRepeatMode(Animation.REVERSE);
	}
	//��ʼ���ؼ�
	private void initWidget(){
		//��һҳ��ؼ�
		layout = (LinearLayout) layout1.findViewById(R.id.addresslay);
		temperature = (TextView) layout1.findViewById(R.id.temperature);
		wind = (TextView) layout1.findViewById(R.id.wind);
		refreshDate = (TextView) layout1.findViewById(R.id.refreshDate);
		refreshTime = (TextView) layout1.findViewById(R.id.refreshTime);
		weather = (TextView) layout1.findViewById(R.id.weather);
		city = (TextView) layout1.findViewById(R.id.city);
		comfortable = (TextView) layout1.findViewById(R.id.comfortable);
		tomorrowTemperature = (TextView) layout1.findViewById(R.id.tomorrowtemperature);
		tomorrowWeather = (TextView) layout1.findViewById(R.id.tomorroweather);
		weatherPic = (ImageView) layout1.findViewById(R.id.weatherPic);
		
		//�ڶ�ҳ��ؼ�
		int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();		// ��Ļ�����أ��磺480px��
	    int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		view = (TrendView) layout2.findViewById(R.id.trendView);
	    view.setWidthHeight(screenWidth, screenHeight);
	    
	    day1 = (TextView) layout2.findViewById(R.id.day1);
		day2 = (TextView) layout2.findViewById(R.id.day2);
		day3 = (TextView) layout2.findViewById(R.id.day3);
		day4 = (TextView) layout2.findViewById(R.id.day4);
		wea1 = (TextView) layout2.findViewById(R.id.weather1);
		wea2 = (TextView) layout2.findViewById(R.id.weather2);
		wea3 = (TextView) layout2.findViewById(R.id.weather3);
		wea4 = (TextView) layout2.findViewById(R.id.weather4);
		
		
		//������У�������������
		city.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(menuListView.getCount() == 0){
					return;
				}
				int index = (menuListView.getCheckedItemPosition()+1)%menuListView.getCount();
				menuListView.setItemChecked(index, true);
				Map<String, String> map = addressList.get(index);
				id = map.get("id");
				refresh();
			}
		});
		//�˵��������б�
		menuListView = (ListView) findViewById(R.id.menuaddresslist);
		addressList = new ArrayList<Map<String,String>>();
		String[] from = new String[]{"address"};
		int[] to = new int[]{android.R.id.text1};
		adapter = new SimpleAdapter(this, addressList, android.R.layout.simple_list_item_single_choice, from, to);
		menuListView.setAdapter(adapter);
		
		//�˵���������Ӱ�ť
		Button add = (Button) mMenuDrawer.findViewById(R.id.addaddress);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				add();
			}
		});
		//����б������������
		menuListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Map<String, String> map = addressList.get(arg2);
				id = map.get("id");
				mMenuDrawer.toggleMenu();
				refresh();
			}
		});
		//����ɾ������
		menuListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final int index = arg2;
				final MyDialog d = new MyDialog(MainActivity.this, "��ʾ", "ȷ��ɾ����");
				System.out.println(index);
				d.show();
				d.getButton1().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(menuListView.getCount() <= 1){
							Toast.makeText(getApplicationContext(), "����Ҫ����һ������", Toast.LENGTH_SHORT).show();
							return;
						}
						DB.deleteCityAndId(addressList.get(index).get("id"));
						addressList.remove(index);
						//int index2 = (menuListView.getCheckedItemPosition()+1)%menuListView.getCount();
						menuListView.setItemChecked(0, true);
						adapter.notifyDataSetChanged();
						Toast.makeText(getApplicationContext(), "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
						d.dismiss();
					}
				});
				return false;
			}
		});
		
		//��ʼ���������ݿ�������ݲ�����
		addressList.clear();
		addressList.addAll(DB.getCityAndId());
		id = addressList.get(0).get("id");
		adapter.notifyDataSetChanged();
		menuListView.setItemChecked(0, true);
		refresh();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case 1:
			if(data != null){
				id = data.getExtras().getString("id");
				Map<String, String> map = new HashMap<String, String>();
				map.put("address", data.getExtras().getString("address"));
				map.put("id", id);
				if(addressList.contains(map)){
					Toast.makeText(getApplicationContext(), "�����Ѵ���", Toast.LENGTH_SHORT).show();
					break;
				}
				addressList.add(map);
				refresh();
				adapter.notifyDataSetChanged();
				menuListView.setItemChecked(menuListView.getCount()-1, true);
				
				DB.saveCityAndId(map.get("address"), id);	//������ӵĵ���
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void finish() {
		super.finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//�������˵���ť�¼�
		switch(item.getItemId()){
		case android.R.id.home:
			mMenuDrawer.toggleMenu();
			break;
		case 1:	//���
			add();
			break;
		case 0:	//����
			refresh();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	//ת������ѡ�����
	private void add(){
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, AddressActivity.class);
		startActivityForResult(intent, 1);
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}
	//��������
	private void refresh(){
		setSupportProgressBarIndeterminateVisibility(true);	//����
		MenuTask task = new MenuTask();
		task.execute(0);
		layout.startAnimation(animation);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "add")
        .setIcon(R.drawable.ic_search)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
        menu.add(0, 0, 0, "Refresh")
        .setIcon(R.drawable.ic_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
	}
	/**
	 * �첽��ѯ����
	 * @author Dave
	 *
	 */
	class MenuTask extends AsyncTask<Integer,Integer,Integer> {
        @Override  
        protected Integer doInBackground(Integer... i) {//�����ִ̨�е������ں�̨�߳�ִ��  
        	WeatherData data = new WeatherData(MainActivity.this);
			weatherData = data.getData("http://m.weather.com.cn/data/"+id+".html");
            return 0;  
        }  
        @Override  
        protected void onPostExecute(Integer result) {//��̨����ִ����֮�󱻵��ã���ui�߳�ִ��
        	//���½���ؼ�ֵ
        	city.setText(weatherData.getCity());
        	temperature.setText(weatherData.getTodayTemperature());
        	if(weatherData.getWeather().get(0).equals("")){
        		weather.setText(weatherData.getTodayWeather());
        	}else{
        		weather.setText(weatherData.getWeather().get(0));
        	}
        	wind.setText(weatherData.getWind().get(0));
        	comfortable.setText(weatherData.getComfortable());
        	tomorrowTemperature.setText(weatherData.getTomorrowTemperature());
        	tomorrowWeather.setText(weatherData.getTomorrowWeather());
        	refreshDate.setText(weatherData.getRefreshDate());
        	refreshTime.setText(weatherData.getRefreshTime());
        	weatherPic.setImageBitmap(WeatherPic.getPic(getApplicationContext(), weatherData.getPicIndex(), weatherData.isNight()?1:0));
        	
        	view.setTemperature(weatherData.getMaxlist(), weatherData.getMinlist());
        	view.setBitmap(weatherData.getTopPic(), weatherData.getLowPic());
        	/*
        	day1.setText(text);
        	day1.setText(text);
        	day1.setText(text);
        	day1.setText(text);*/
        	if(!weatherData.getWeather().get(0).equals("")){
        		wea1.setText(weatherData.getWeather().get(0));
        	}else{
        		wea1.setText(weatherData.getTodayWeather());
        	}
        	wea2.setText(weatherData.getWeather().get(1));
        	wea3.setText(weatherData.getWeather().get(2));
        	wea4.setText(weatherData.getWeather().get(3));
        	
        	//���
        	setSupportProgressBarIndeterminateVisibility(false);
        }  
    }  
}
