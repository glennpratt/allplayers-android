package com.allplayers.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupPageActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grouppage);
		
		GroupData group = Globals.currentGroup;
		String title = group.getTitle();
		String desc = group.getDescription();
		String logoURL = group.getLogo();
		
		Bitmap logo = Globals.getRemoteImage(logoURL);
		
		ImageView imView = (ImageView)findViewById(R.id.groupLogo);
		imView.setImageBitmap(logo);
		
		TextView groupInfo = (TextView)findViewById(R.id.groupDetails);
		groupInfo.setText("Title: " + title + "\nDescription: " + desc);
		
		final Button groupMembersButton = (Button)findViewById(R.id.groupMembersButton);
		groupMembersButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
            	startActivity(new Intent(GroupPageActivity.this, GroupMembersActivity.class));
            }
        });
        
        final Button groupEventsButton = (Button) findViewById(R.id.groupEventsButton);
        groupEventsButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
            	startActivity(new Intent(GroupPageActivity.this, GroupEventsActivity.class));
            }
        });
        
        final Button groupPhotosButton = (Button) findViewById(R.id.groupPhotosButton);
        groupPhotosButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
            	startActivity(new Intent(GroupPageActivity.this, GroupPhotosActivity.class));
            }
        });
	}
}