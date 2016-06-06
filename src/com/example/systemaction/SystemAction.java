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
				Intent intent = new Intent();  //����Intent
				intent.setAction(Intent.ACTION_GET_CONTENT); //����Intent��Action����
				intent.setType("vnd.android.cursor.item/phone"); //����Intent��Type����
				startActivityForResult(intent, PICK_CONTACT);  //����Activity����ϣ����ȡ��Activity�Ľ��
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
				Uri contactData = data.getData();  //��ȡ���ص�����
				Cursor cursor = managedQuery(contactData, null, null, null, null);  //��ѯ��ϵ����Ϣ
				if (cursor.moveToFirst())  //�����ѯ��ָ������ϵ��
				{
					String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)); //��ȡ��ϵ�˵�����
					String phoneNumber = "����ϵ����δ����绰����";  
					//������ϵ�˲�ѯ����ϵ�˵���ϸ��Ϣ
					Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
							null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
							null, null);
					//ȡ����һ��
					if (phones.moveToFirst())
					{
						//ȡ���绰����
						phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					phones.close(); //�ر��α�
					EditText show = (EditText)findViewById(R.id.show);
					show.setText(name); //��ʾ��ϵ�˵�����
					EditText phone = (EditText)findViewById(R.id.phone);
					phone.setText(phoneNumber); //��ʾ��ϵ�˵ĵ绰����
				}
				cursor.close(); //�ر��α�
			}
			break;
		}
 	
    }
}
