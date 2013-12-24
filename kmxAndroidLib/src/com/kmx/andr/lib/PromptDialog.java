package com.kmx.andr.lib;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;

/*

PromptDialog dlg = new PromptDialog(MainActivity.this, R.string.title, R.string.enter_comment) {  
	 @Override  
	 public boolean onOkClicked(String input) {  
	  // do something  
	  return true; // true = close dialog  
	 }  
	};  
	dlg.show();

*/


/**
 * helper for Prompt-Dialog creation
 */
public abstract class PromptDialog extends AlertDialog.Builder implements OnClickListener {
 private final EditText input;
 
 
 public PromptDialog(Context context, String title, String message) {
	  super(context);
	  
	  setTitle(title);
	  setMessage(message);

	  input = new EditText(context);
	  //setView(input);

	  setPositiveButton("OK", this);
	  //setNegativeButton(cancelText, this);
	 }
 
 public PromptDialog(Context context, String title, String message, String okText) {
	  super(context);
	  
	  setTitle(title);
	  setMessage(message);

	  input = new EditText(context);
	  setView(input);

	  setPositiveButton(okText, this);
	  //setNegativeButton(cancelText, this);
	 }


 public PromptDialog(Context context, String title, String message, String okText, String cancelText) {
  super(context);
  
  setTitle(title);
  setMessage(message);

  input = new EditText(context);
  setView(input);

  setPositiveButton(okText, this);
  setNegativeButton(cancelText, this);
 }
 
 public PromptDialog(Context context, String title, String message, String okText, String cancelText, String defaultText) {
	  super(context);
	  
	  setTitle(title);
	  setMessage(message);

	  input = new EditText(context);
	  input.setText(defaultText);
	  setView(input);

	  setPositiveButton(okText, this);
	  setNegativeButton(cancelText, this);
	 }

 /**
  * will be called when "cancel" pressed.
  * closes the dialog.
  * can be overridden.
  * @param dialog
  */
 public void onCancelClicked(DialogInterface dialog) {
  dialog.dismiss();
 }

 @Override
 public void onClick(DialogInterface dialog, int which) {
  if (which == DialogInterface.BUTTON_POSITIVE) {
   if (onOkClicked(input.getText().toString())) {
    dialog.dismiss();
   }
  } else {
   onCancelClicked(dialog);
  }
 }
 

 public void showWin() {
	  AlertDialog alertToShow = this.create();
	  alertToShow.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	  alertToShow.show();
 }

 /**
  * called when "ok" pressed.
  * @param input
  * @return true, if the dialog should be closed. false, if not.
  */
 abstract public boolean onOkClicked(String input);
}
