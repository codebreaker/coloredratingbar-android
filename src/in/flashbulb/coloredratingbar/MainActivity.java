package in.flashbulb.coloredratingbar;

import in.flashbulb.coloredratingbar.ColoredRatingBar.OnRatingBarChangeListener;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	ColoredRatingBar ratingBar;
	TextView ratingText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ratingText = (TextView)findViewById(R.id.textView1);
		ratingBar = (ColoredRatingBar)findViewById(R.id.coloredRatingBar1);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(ColoredRatingBar ratingBar, float rating,
					boolean fromUser) {
				if(rating<=1.6){
					ratingText.setText("Poor");
				}else if(rating<=3.2){
					ratingText.setText("Average");
				}else{
					ratingText.setText("Excellent");
				}
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
