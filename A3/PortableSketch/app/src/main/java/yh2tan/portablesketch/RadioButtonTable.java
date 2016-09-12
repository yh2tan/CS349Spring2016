package yh2tan.portablesketch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * @author diego
 *
 */
public class RadioButtonTable extends TableLayout  implements OnClickListener {

    private static final String TAG = "ToggleButtonGroupTableLayout";
    private RadioButton activeRadioButton;
    private MainActivity activity;

    /**
     * @param context
     */
    public RadioButtonTable(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

    }

    /**
     * @param context
     * @param attrs
     */
    public RadioButtonTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onClick(View v) {
        final RadioButton rb = (RadioButton) v;
        if ( activeRadioButton != null ) {
            activity.changeBackGroundImage(activeRadioButton.getId(), false);
            activeRadioButton.setChecked(false);
        }

        rb.setChecked(true);
        activeRadioButton = rb;
        activity.changeBackGroundImage(activeRadioButton.getId(), true);
        activity.makeChange();
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setChildrenOnClickListener((TableRow)child);
    }

    public void setActiveRadioButton(RadioButton active){
        if (activeRadioButton != null) {
            activity.changeBackGroundImage(activeRadioButton.getId(), false);
            activeRadioButton.setChecked(false);
        }

        active.setChecked(true);
        activeRadioButton = active;
        activity.changeBackGroundImage(activeRadioButton.getId(), true);
    }

    public void setActivity(MainActivity a){
        activity = a;
    }


    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setChildrenOnClickListener((TableRow)child);
    }


    private void setChildrenOnClickListener(TableRow tr) {
        final int c = tr.getChildCount();
        for (int i=0; i < c; i++) {
            final View v = tr.getChildAt(i);
            if ( v instanceof RadioButton ) {
                v.setOnClickListener(this);
            }
        }
    }

    public int getCheckedRadioButtonId() {
        if ( activeRadioButton != null ) {
            return activeRadioButton.getId();
        }
        return -1;
    }
}