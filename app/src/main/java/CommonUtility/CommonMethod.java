package CommonUtility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;


/**
 * Created by Mehul Panchal on 9/14/2015.
 */
public class CommonMethod {

    @SuppressLint("StaticFieldLeak")
    private static Context ctx;

    public static boolean IS_LOGIN = false;

    public CommonMethod(Context context) {
        ctx = context;
    }


    public static String Main_URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";


    public static String zero(int num) {
        return (num < 10) ? ("0" + num) : ("" + num);

    }

    public static boolean notEmptyEDT(EditText edt) {
        String text = edt.getText().toString();

        if (text.trim().length() > 0) {
            // edt.setBackgroundResource(R.color.green);
            return true;

        } else {

            edt.setError("FIll THE FIELD!!!");
            edt.requestFocus();
            return false;
        }


    }

    public static boolean notEmptyAuto(AutoCompleteTextView completeTextView) {
        String text = completeTextView.getText().toString();

        if (text.trim().length() > 0) {
            // edt.setBackgroundResource(R.color.green);
            return true;

        } else {

            completeTextView.setError("FIll THE FIELD!!!");
            completeTextView.requestFocus();
            return false;
        }


    }

    public static boolean isTen(EditText edt) {
        if (edt.getText().toString().trim().length() == 10) {

            return true;
        } else {
            edt.setError("MUST BE 10 DIGIT");
            return false;
        }
    }

    public boolean isLessZero(EditText edt, Context ctx) {
        if (Integer.parseInt(edt.getText().toString()) <= 0) {
            edt.setError("MUST BE GRATER THAN 0");
            //  Toast.makeText(ctx, "MUST BE GRATER THAN",Toast.LENGTH_LONG).show();

            return true;
        } else {

            return false;
        }

    }

    public boolean isGrater(EditText edtFull, EditText edt) {
        if (Integer.parseInt(edtFull.getText().toString()) < Integer.parseInt(edt.getText().toString())) {
            edt.setError("MUST BE GRATER THAN 0");
            return false;
        } else {
            return true;
        }
    }

    public static boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }



}
