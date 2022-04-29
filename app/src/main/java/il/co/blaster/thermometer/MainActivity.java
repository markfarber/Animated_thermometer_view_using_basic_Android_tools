package il.co.blaster.thermometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import java.text.NumberFormat;
import java.text.ParseException;

public class MainActivity extends AppCompatActivity {

    //Setup
    private final long ANIMATION_DURATION = 500;
    private final TimeInterpolator ANIMATION_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    

    private Number currentThermometerValue = 100;
    private final ValueAnimator animator = new ValueAnimator();
    private final NumberFormat nf = NumberFormat.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configure animator
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(ANIMATION_INTERPOLATOR);

        View thermometerScaleView = findViewById(R.id.thermometer_scale);

        findViewById(R.id.btn_set_new_value).setOnClickListener(v -> {
            Number targetThermometerValue;
            EditText input = findViewById(R.id.input_new_value);
            Number inputValue;
            if (!input.getText().toString().trim().equals("")) {
                try {
                    inputValue = nf.parse(input.getText().toString());
                } catch (Exception e) {
                    Log.e(":::MainActivity", "Parsing exception " + e.getMessage());
                    return;
                }

                assert inputValue != null;
                if (inputValue.intValue() > 100) {
                    targetThermometerValue = 100;
                } else if (inputValue.intValue() < 0) {
                    targetThermometerValue = 0;
                } else {
                    targetThermometerValue = inputValue;
                }

                if (!currentThermometerValue.equals(targetThermometerValue)) {
                    animateValueChange(thermometerScaleView, targetThermometerValue);
                }
            }
        });
    }

    private void animateValueChange(View thermometerScaleView, Number targetThermometerValue) {
        try {
            if (animator.isStarted()){
                animator.cancel();
            }
            animator.setFloatValues(currentThermometerValue.floatValue(), targetThermometerValue.floatValue());
            animator.addUpdateListener(valueAnimator -> {
                Number currentValue = (float) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) thermometerScaleView.getLayoutParams();
                //This is the parameter we are animating, app:layout_constraintHeight_percent
                //In XML its range is 0..1, hence the division by 100
                lp.matchConstraintPercentHeight = currentValue.floatValue() / 100;
                thermometerScaleView.setLayoutParams(lp);
                //Note that we only save the *representation* value here for animation consistency
                //in case animation is interrupted.
                currentThermometerValue = currentValue;
            });
            animator.start();
        } catch (Exception e) {
            Log.e(":::MainActivity", "Exception while animating: " + e.getMessage());
        }
    }
}