
# Chat Kit Layout

An Android Library to create a complete chat layout.

[![](https://jitpack.io/v/Vikram2921/ChatKitLayout.svg)](https://jitpack.io/#Vikram2921/ChatKitLayout)

### Installation
Add it in your root build.gradle at the end of repositories:
        

    allprojects {
        		repositories {
        			...
        			maven { url 'https://jitpack.io' }
        		}
        	}


Add the dependency

```css
dependencies {
	        implementation 'com.github.Vikram2921:ChatKitLayout:<Latest Version>'
	}
```
**How to use :** 

**Step 1 .** In **layout.xml** file.

    <com.nobodyknows.chatlayoutview.ChatLayoutView  
      android:layout_width="match_parent"  
      android:id="@+id/chatlayout_view"  
      app:view_mode="RecyclerView"  //There is two option ListView,RecycleView
      app:useDatabase="true"  //If you want to in library database 
      android:layout_above="@id/messageentry"  
      android:layout_height="match_parent">  
    </com.nobodyknows.chatlayoutview.ChatLayoutView>

**Step 2.** In **ChatActivity.java** file
	

 In **onCreate** file

    uploadAndDownloadViewHandler = new UploadAndDownloadViewHandler(getApplicationContext()); //Create this object in MainActivity not in Chat Activity
    ChatLayoutView chatLayoutView = findViewById(R.id.chatlayout_view);  
    chatLayoutView.setChatLayoutListener(this);  
    chatLayoutView.setUploadAndDownloadViewHandler(uploadAndDownloadViewHandler);  
    chatLayoutView.setMainActivityContext(getApplicationContext());
    chatLayoutView.setIds(<Chatroom Id>,<Your Id>);

In **ChatActivity**

    public class ChatActivity extends AppCompatActivity implements ChatLayoutListener

**Step 3.** Add Users in Chat

    User myUserObject = new User();  
    myUserObject.setName(<Your Name>);  
    myUserObject.setColor(Color.BLUE);  
    myUserObject.setUserId(<Your Id>);  
    User freindUserObject = new User();  
    freindUserObject.setName(<Freind Name>);  
    freindUserObject.setProfileUrl(<Freind Profile Url>);  
    freindUserObject.setColor(Color.RED);  
    freindUserObject.setUserId(<Freind Id>);  
    chatLayoutView.addUser(myUserObject);  
    chatLayoutView.addUser(freindUserObject);

**Step 4.** If you are using inLibrary Database then use this load all previus saved chat

    chatLayoutView.loadAllDBMessage();

**Step 5.** Now Add Message to layout like this :-

    chatLayoutView.addMessage(<MessageObject>);

**Step 6.** Update Message Status Like :

    chatLayoutView.updateMessageStatus(<MessageId>,<MessageStatus>);

**

## Message Object
| Fields | Data Type |
|--|--|
| Message Id| String |
| Sender | String|
| Receiver | String|
| Message| String |
| Room Id| String |
| Shated Files| List of SharedFile
| Contacts| List of Contacts
|Message Type | MessageType |
| Created Timestamp| Date |
| Updated Timestamp| Date |
| Sent At | Date |
| Receive At|Date|
| Seen At|Date|
| Is Replied Message|Boolean //Default False| 
| Replied Message Id |String|
| Message Status | MessageStatus |
| Message Configuration | MessageConfiguration |
| Reply Message View | View //If message is replied is true then pass Reply View here |
| Custom View | View //If your Message Type MessageType.CUSTOM pass your view here


## Message Type Supported
| Message Type |
|--|
| IMAGE |
| GIF |
| VIDEO |
| AUDIO |
| RECORDING |
| TEXT |
| DOCUMENT |
| CONTACT |
| DATE |
| STICKER | 
| CUSTOM |


## Message Status Avaialable

| Message Status |
|--|
| SENDING |
| SENT |
| RECEIVED |
| SEEN |
| DELETED |
