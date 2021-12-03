package com.getphone_contacts;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.phone.apple.getphone_contacts.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddActivity extends Activity {

    @BindView(R.id.etd_phone)
    TextView etd_phone;
    @BindView(R.id.etd_name)
    TextView etd_name;

    @OnClick(R.id.llyd_back)
    public void llyd_back() {
        finish();
    }

    @OnClick(R.id.llyd_add)
    public void llyd_add() {
        String name = etd_name.getText().toString();
        String phone = etd_phone.getText().toString();
        PhoneUtil.insertConstacts(this,name,phone);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
    }
}
