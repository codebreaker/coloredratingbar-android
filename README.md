coloredratingbar-android
========================

Multi-colored Ratingbar for Android.

Right now this is very basic with few values hardcoded into the widget.

![ColoredRatingbar Sample Screenshot][1]

Usage
=====

*You can make this library project and use the rating bar.*

1. You can include in your layout xml 
    
        <in.flashbulb.coloredratingbar.ColoredRatingBar
          android:id="@+id/coloredRatingBar1"
          style="@style/coloredRatingBarStyle"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:layout_alignParentLeft="true"
          android:layout_marginLeft="16dp"
          android:layout_marginTop="14dp"
          rb:rating="3.8" />

2. There are 4 different styles included
        *coloredRatingBarStyle,
        coloredRatingBarStyleIndicator,
        coloredRatingBarStyleSmall,
        coloredRatingBarStyleSmallIndicator.*

3. Can add listener for rating change.
      
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

Limitations
-----------

Right now the widget is very much limited.

1. Number of stars fixed to 5. 
2. Color ranges hardcoded in the widget.
3. Rating from UI can only be in integers. i.e stepsize is fixed to 1.

Including In Your Project
-------------------------

Download/clone and use it as android project library.


Used icons from [iconexpo.com](http://iconexpo.com/2008/12/free-8color-star-icons/)
if its violation to use them please tell me will remove them.

 
[1]: https://raw.github.com/codebreaker/coloredratingbar-android/master/screens.png
