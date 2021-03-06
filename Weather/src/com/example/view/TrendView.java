package com.example.view;

import java.util.ArrayList;
import java.util.List;

import com.example.util.Constants;
import com.example.util.WeatherPic;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * @author Mark
 * 
 */
public class TrendView extends View {

	private Paint mPointPaint;
	private Paint mTextPaint;
	private Paint mLinePaint1;
	private Paint mLinePaint2;
	private Paint mbackLinePaint;

	private int count = 5;
	private int x[] = new int[count];
	private float radius = 8;
	private int h;
	private List<Integer> topTem;
	private List<Integer> lowTem;
	private Bitmap[] topBmps;
	private Bitmap[] lowBmps;

	private Context c;

	public TrendView(Context context) {
		super(context);
		this.c = context;
		init();
	}

	public TrendView(Context context, AttributeSet attr) {
		super(context, attr);
		this.c = context;
		init();
	}

	private void init() {
		topBmps = new Bitmap[count];
		lowBmps = new Bitmap[count];

		topTem = new ArrayList<Integer>();
		lowTem = new ArrayList<Integer>();

		mbackLinePaint = new Paint();
		mbackLinePaint.setColor(Color.WHITE);

		mPointPaint = new Paint();
		mPointPaint.setAntiAlias(true);
		mPointPaint.setColor(Color.BLUE);

		mLinePaint1 = new Paint();
		mLinePaint1.setColor(Color.RED);
		mLinePaint1.setAntiAlias(true);
		mLinePaint1.setStrokeWidth(4);
		mLinePaint1.setStyle(Style.FILL);

		mLinePaint2 = new Paint();
		mLinePaint2.setColor(Color.GREEN);
		mLinePaint2.setAntiAlias(true);
		mLinePaint2.setStrokeWidth(4);
		mLinePaint2.setStyle(Style.FILL);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(getResources().getColor(com.example.absdemo.R.color.my__holo_blue_light));
		mTextPaint.setTextSize(25F);
		mTextPaint.setTextAlign(Align.CENTER);
	}

	public void setPosition(int... a) {

		for (int i : a) {
			x[i] = a[i];
		}

	}

	public void setWidthHeight(int w, int h) {

		int parts = 2 * count;
		int distanceUnit = w / parts;

		int pointDistance = 2 * distanceUnit;

		x[0] = distanceUnit;
		for (int i = 1; i < count; i++) {

			x[i] = x[i - 1] + pointDistance;

		}

		this.h = h;
	}

	public void setTemperature(List<Integer> top, List<Integer> low) {
		this.topTem = top;
		this.lowTem = low;

		postInvalidate();
	}

	public void setBitmap(List<Integer> topList, List<Integer> lowList) {
		topBmps[0] = WeatherPic.getSmallPic(c, topList.get(0), 0);
		topBmps[1] = WeatherPic.getSmallPic(c, topList.get(1), 0);
		topBmps[2] = WeatherPic.getSmallPic(c, topList.get(2), 0);
		topBmps[3] = WeatherPic.getSmallPic(c, topList.get(3), 0);
		topBmps[4] = WeatherPic.getSmallPic(c, topList.get(4), 0);

		lowBmps[0] = WeatherPic.getSmallPic(c, lowList.get(0), 1);
		lowBmps[1] = WeatherPic.getSmallPic(c, lowList.get(1), 1);
		lowBmps[2] = WeatherPic.getSmallPic(c, lowList.get(2), 1);
		lowBmps[3] = WeatherPic.getSmallPic(c, lowList.get(3), 1);
		lowBmps[4] = WeatherPic.getSmallPic(c, lowList.get(4), 1);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float space = 0f;
		float space1 = 0f;
		int temspace = 10;

		FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		float fontHeight = fontMetrics.bottom - fontMetrics.top;

		int h = this.h / 2;
		int h2 = (int) (h - fontHeight / 2);
		int h3 = (int) (h - fontHeight - Constants.picSize);

		int h4 = (int) (h + fontHeight);
		int h5 = (int) (h + fontHeight);

		
		canvas.drawLine(x[0], 50 , x[0]+60, 50, mLinePaint1);
		canvas.drawText("最高温度 ℃", x[0]+140, 57, mTextPaint);
		
		canvas.drawLine(x[0], 80 , x[0]+60, 80, mLinePaint2);
		canvas.drawText("最低温度 ℃", x[0]+140, 87, mTextPaint);

		for (int i = 0; i < topTem.size(); i++) {
			space = (-topTem.get(i)) * temspace;
			if (topTem.get(i) != 100) {
				if (i != topTem.size() - 1) {
					space1 = (-topTem.get(i + 1)) * temspace;
					canvas.drawLine(x[i], h + space, x[i + 1], h + space1,
							mLinePaint1);
				}
				if (i == 0) {
					mTextPaint.setColor(getResources().getColor(
							com.example.absdemo.R.color.my__holo_blue_light));
					mPointPaint.setColor(getResources().getColor(
							com.example.absdemo.R.color.my__holo_blue_light));
				} else {
					mTextPaint.setColor(Color.WHITE);
					mPointPaint.setColor(Color.WHITE);
				}
				canvas.drawText(topTem.get(i) + "℃", x[i], h2 + space,
						mTextPaint);
				canvas.drawCircle(x[i], h + space, radius, mPointPaint);
				canvas.drawBitmap(topBmps[i], x[i] - topBmps[i].getWidth() / 2,
						h3 + space, null);
			}
		}

		for (int i = 0; i < lowTem.size(); i++) {
			space = (-lowTem.get(i)) * temspace;
			if (i != lowTem.size() - 1) {
				space1 = (-lowTem.get(i + 1)) * temspace;
				canvas.drawLine(x[i], h + space, x[i + 1], h + space1,
						mLinePaint2);
			}
			if (i == 0) {
				mTextPaint.setColor(getResources().getColor(
						com.example.absdemo.R.color.my__holo_blue_light));
				mPointPaint.setColor(getResources().getColor(
						com.example.absdemo.R.color.my__holo_blue_light));
			} else {
				mTextPaint.setColor(Color.WHITE);
				mPointPaint.setColor(Color.WHITE);
			}

			canvas.drawText(lowTem.get(i) + "℃", x[i], h4 + space, mTextPaint);
			canvas.drawCircle(x[i], h + space, radius, mPointPaint);
			canvas.drawBitmap(lowBmps[i], x[i] - lowBmps[i].getWidth() / 2, h5
					+ space, null);
		}
	}

}
