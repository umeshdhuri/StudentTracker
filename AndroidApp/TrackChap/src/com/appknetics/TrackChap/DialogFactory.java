package com.appknetics.TrackChap;

import com.appknetics.TrackChap.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager.BadTokenException;
public class DialogFactory {

	public static ProgressDialog showOnlySpinningWheel(Context mContext) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		try {
			dialog.show();
		} catch (BadTokenException e) {
		}
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_only_spinner);
		// dialog.setMessage(Message);
		return dialog;
	}
}
