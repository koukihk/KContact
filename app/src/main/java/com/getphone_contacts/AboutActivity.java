package com.getphone_contacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.phone.apple.getphone_contacts.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends Activity {



    @OnClick(R.id.llya_back)
    public void llya_back() {
        finish();
    }

    @OnClick(R.id.but_acontact)
    public void but_contact() {
        String[] email = {"soruku@foxmail.com"};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_CC, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分");
        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分");
        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
    }

    @OnClick(R.id.but_asite)
    public void but_site() {
        Intent intent=new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse("https://github.com/koukihk/KContacts"));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
    }
}
