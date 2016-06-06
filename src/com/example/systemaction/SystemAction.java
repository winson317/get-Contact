package com.example.systemaction;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SystemAction extends Activity {
	
	private final int PICK_CONTACT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();  //创建Intent
				intent.setAction(Intent.ACTION_GET_CONTENT); //设置Intent的Action属性
				intent.setType("vnd.android.cursor.item/phone"); //设置Intent的Type属性
				startActivityForResult(intent, PICK_CONTACT);  //启动Activity，并希望获取该Activity的结果
			}
		});
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	switch (requestCode) 
    	{
		case PICK_CONTACT:
			if (resultCode == Activity.RESULT_OK)
			{
				Uri contactData = data.getData();  //获取返回的数据
				Cursor cursor = managedQuery(contactData, null, null, null, null);  //查询联系人信息
				if (cursor.moveToFirst())  //如果查询到指定的联系人
				{
					String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)); //获取联系人的姓名
					String phoneNumber = "此联系人暂未输入电话号码";  
					//根据联系人查询该联系人的详细信息
					Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
							null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
							null, null);
					//取出第一行
					if (phones.moveToFirst())
					{
						//取出电话号码
						phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					phones.close(); //关闭游标
					EditText show = (EditText)findViewById(R.id.show);
					show.setText(name); //显示联系人的姓名
					EditText phone = (EditText)findViewById(R.id.phone);
					phone.setText(phoneNumber); //显示联系人的电话号码
				}
				cursor.close(); //关闭游标
			}
			break;
		}
 	
    }
}
