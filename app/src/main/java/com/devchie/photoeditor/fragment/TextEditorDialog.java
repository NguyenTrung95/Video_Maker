package com.devchie.photoeditor.fragment;

import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.devchie.videomaker.R;
import com.devchie.photoeditor.activity.EditPhotoActivity;

public class TextEditorDialog extends Dialog implements View.OnClickListener {
    private EditPhotoActivity context;

    public EditText edtQuotes;
    private TextFragmentListener listener;

    public interface TextFragmentListener {
        void onText(String str);
    }

    public void setTextListener(TextFragmentListener textFragmentListener) {
        this.listener = textFragmentListener;
    }


    public TextEditorDialog(final EditPhotoActivity r4, String str) {
        super(r4);
        this.context = r4;
        setContentView(R.layout.fragment_text_editor);
        edtQuotes = (findViewById(R.id.edtQuotes));
        findViewById(R.id.btnCancel).setOnClickListener(this);
        findViewById(R.id.btnDone).setOnClickListener(this);
        if (!str.equals(r4.getString(R.string.doubletap))) {
            this.edtQuotes.setText(str);
        }
        if (this.edtQuotes.requestFocus()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ((InputMethodManager) r4.getSystemService("input_method")).showSoftInput(TextEditorDialog.this.edtQuotes, 1);
                }
            }, 200);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                this.edtQuotes.setText("");
                return;
            case R.id.btnDone:
                String obj = this.edtQuotes.getText().toString();
                if (obj == "" || obj.isEmpty()) {
                    dismiss();
                    return;
                }
                TextFragmentListener textFragmentListener = this.listener;
                if (textFragmentListener != null) {
                    textFragmentListener.onText(obj);
                    dismiss();
                    return;
                }
                return;
            default:
                return;
        }
    }
}
