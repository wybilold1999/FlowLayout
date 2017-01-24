package com.cyanbirds.flowlay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private String[] mVals = new String[]
			{ "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
					"Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
					"Android", "Weclome Hello", "Button Text", "TextView" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FlowLayout mFlowLayout = (FlowLayout) findViewById(R.id.flow_layout);
		LinearLayout.MarginLayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		List<View> mChildViews = new ArrayList<>();
		for (int i = 0; i < mVals.length; i++) {
			Button tv = new Button(this);
			tv.setText(mVals[i]);
			tv.setLayoutParams(params);
			mChildViews.add(tv);
		}
		mFlowLayout.setChildViews(mChildViews);

	}
}
