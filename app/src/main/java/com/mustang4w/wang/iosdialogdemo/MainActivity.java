package com.mustang4w.wang.iosdialogdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mustang4w.wang.iosdialog.widget.IOSDialog;

/**
 * @author Wangym
 */
public class MainActivity extends AppCompatActivity {

    private EditText edtTitle, edtMessage, edtHint, edtLButton, edtRButton;
    private Button btnShow;
    private CheckBox cbxOutside;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTitle = findViewById(R.id.edt_title);
        edtMessage = findViewById(R.id.edt_message);
        edtHint = findViewById(R.id.edt_hint);
        edtLButton = findViewById(R.id.edt_left_button);
        edtRButton = findViewById(R.id.edt_right_button);
        cbxOutside = findViewById(R.id.cbx_outside);
        btnShow = findViewById(R.id.btn_show);

        edtTitle.setText("“IOSDialogDemo”想给您发送推送通知");
        edtMessage.setText("“通知”可能包括提醒、声音、和图标标记。\n这些可以在“设置”中配置。");
        edtLButton.setText("不允许");
        edtRButton.setText("好");

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 初始化Dialog
                IOSDialog iosDialog = IOSDialog.with(MainActivity.this);
                // 设置标题
                if (!"".equals(edtTitle.getText().toString())) {
                    iosDialog.setTitle(edtTitle.getText().toString());
                }
                // 设置内容
                iosDialog.setMessage(edtMessage.getText().toString());
                // 设置提示
                if (!"".equals(edtHint.getText().toString())) {
                    iosDialog.setInputHint(edtHint.getText().toString());
                }
                // 设置左按钮
                if (!"".equals(edtLButton.getText().toString())) {
                    iosDialog.setLeftButton(edtLButton.getText().toString(), null);
                }
                // 设置右按钮
                if (!"".equals(edtRButton.getText().toString())) {
                    iosDialog.setRightButton(edtRButton.getText().toString(), new IOSDialog.OnRightButtonClickListener() {
                        @Override
                        public void onClickListener(String input) {
                            Toast.makeText(MainActivity.this,  "输入内容为：" + input, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                // 设置外部触摸
                if (cbxOutside.isChecked()) {
                    iosDialog.setCanceledOnTouchOutside(cbxOutside.isChecked());
                }
                iosDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
