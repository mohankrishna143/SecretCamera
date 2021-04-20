package com.android.krishsecretcamera;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import listeners.CallBackListener;

public class DevicePolicyDemoActivity extends Activity implements
		OnCheckedChangeListener, CallBackListener , ActivityCompat.OnRequestPermissionsResultCallback{
	static final String TAG = "DevicePolicyDemoActivity";
	static final int ACTIVATION_REQUEST = 47; // identifies our request id
	DevicePolicyManager devicePolicyManager;
	ComponentName demoDeviceAdmin;
	ToggleButton toggleButton;

    private static final String[] requiredPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,Manifest.permission.GET_ACCOUNTS,Manifest.permission.MODIFY_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;
	private NetworkChangeReceiver receiver;
	Switch action,loction,flash;
	String LOCATION="Location";
	String FLASH="Flash";
	String ADMIN="Admin";
    String MAILID="Mail_Id";
	Spinner spinner;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		if (Build.VERSION.SDK_INT >= 23) {
			initializePermissionForMarsh();
		}
	/*	toggleButton = (ToggleButton) super
				.findViewById(R.id.toggle_device_admin);*/
		//toggleButton.setOnCheckedChangeListener(this);

		/*IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkChangeReceiver();
		registerReceiver(receiver, filter);*/
		// Initialize Device Policy Manager service and our receiver class
		devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		demoDeviceAdmin = new ComponentName(this, DemoDeviceAdminReceiver.class);
	}

	private  void init(){
		action=findViewById(R.id.action);
		loction=findViewById(R.id.loction);
		flash=findViewById(R.id.flash);
		spinner=findViewById(R.id.spinner);

		boolean admin_enabled=getPreferenceData(ADMIN);
		boolean location_Enabled=getPreferenceData(LOCATION);
		boolean flash_Enabled=getPreferenceData(FLASH);
        if(admin_enabled){
        	action.setChecked(true);
		}else{
			action.setChecked(false);
		}
        int i=0;
		action.setOnCheckedChangeListener(this);
        loction.setOnCheckedChangeListener(this);
        flash.setOnCheckedChangeListener(this);

        if(flash_Enabled){
            flash.setChecked(true);
        }else{
            flash.setChecked(false);
        }
        if(location_Enabled){
            loction.setChecked(true);
        }else{
            loction.setChecked(false);
        }
        int hasPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.GET_ACCOUNTS);
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            //Do smthng
            getMailId();
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveStringPreferenceData(MAILID,parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       /* spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // parent.getItemAtPosition(position).toString()
                saveStringPreferenceData(MAILID,parent.getItemAtPosition(position).toString());
            }
        });*/
        /*if(getMailId().length>0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, getMailId());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }*/
        /*action.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					et.putBoolean("Admin", true);
				}else{
					et.putBoolean("Admin", false);

				}
				et.commit();
			}
		});*/

		/*loction.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					savePreferenceData(LOCATION,true);
					//et.putBoolean("Location", true);
				}else{
					savePreferenceData(LOCATION,false);
					//et.putBoolean("Location", false);

				}
				//et.commit();
			}
		});

		flash.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					savePreferenceData(FLASH,true);
				}else{
					savePreferenceData(FLASH,false);
					//et.putBoolean("Flash", false);

				}
				//et.commit();
			}
		});*/
	}
	/**
	 * Called when a button is clicked on. We have Lock Device and Reset Device
	 * buttons that could invoke this method.
	 */
	public void onClick(View v) {
		/*switch (v.getId()) {
		case R.id.button_lock_device:
			// We lock the screen
			Toast.makeText(this, "Locking device...", Toast.LENGTH_LONG).show();
			Log.d(TAG, "Locking device now");
			//devicePolicyManager.lockNow();
			*//*Intent intent = new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);*//*
			Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
			intent.putExtra("enabled", true);
			sendBroadcast(intent);
			break;
		case R.id.button_reset_device:
			// We reset the device - this will erase entire /data partition!
			Toast.makeText(this, "Locking device...", Toast.LENGTH_LONG).show();
			Log.d(TAG,
					"RESETing device now - all user data will be ERASED to factory settings");
			devicePolicyManager.wipeData(ACTIVATION_REQUEST);
			break;
		}*/
	}

	/**
	 * Called when the state of toggle button changes. In this case, we send an
	 * intent to activate the device policy administration.
	 */
	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {

        switch (view.getId()) {
            case R.id.action:
                // Do something
                if (isChecked) {
                    // Activate device administration
                    Intent intent = new Intent(
                            DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                            demoDeviceAdmin);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "Your boss told you to do this");
                    startActivityForResult(intent, ACTIVATION_REQUEST);
                    savePreferenceData(ADMIN, true);
                    action.setChecked(true);
			/*Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminSample);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					activity.getString(R.string.add_admin_extra_app_text));
			startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);*/
                } else {
                    savePreferenceData(ADMIN, false);
                    action.setChecked(false);
                    ComponentName devAdminReceiver = new ComponentName(this, DemoDeviceAdminReceiver.class);
                    DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                    mDPM.removeActiveAdmin(devAdminReceiver);
                }
                break;
            case R.id.loction:
                if (isChecked) {
                    savePreferenceData(LOCATION, true);
                    //et.putBoolean("Location", true);
                } else {
                    savePreferenceData(LOCATION, false);
                    //et.putBoolean("Location", false);

                }
                break;
            case R.id.flash:
                if (isChecked) {
                    savePreferenceData(FLASH, true);
                } else {
                    savePreferenceData(FLASH, false);
                    //et.putBoolean("Flash", false);

                }
                break;
            default:
                break;
        }

        //Log.d(TAG, "onCheckedChanged to: " + isChecked);
    }

	/**
	 * Called when startActivityForResult() call is completed. The result of
	 * activation could be success of failure, mostly depending on user okaying
	 * this app's request to administer the device.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVATION_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				//Log.i(TAG, "Administration enabled!");
				action.setChecked(true);
			} else {
				//Log.i(TAG, "Administration enable FAILED!");
                action.setChecked(false);
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

    @Override
    public void passwordFailed() {
        Log.d("password fail","failed Call back");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CODE: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    checkPermissions();
                }else {
                    int hasPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.GET_ACCOUNTS);
                    if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                        //Do smthng
                        getMailId();
                    }

                }
            }
        }
    }

    /**
     * checking  permissions at Runtime.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        final List<String> neededPermissions = new ArrayList<>();
        for (final String permission : requiredPermissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DevicePolicyDemoActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

            }else
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }
        if (!neededPermissions.isEmpty()) {
            requestPermissions(neededPermissions.toArray(new String[]{}),
                    MY_PERMISSIONS_REQUEST_ACCESS_CODE);
        }
    }
	@TargetApi(Build.VERSION_CODES.M)
	private void initializePermissionForMarsh() {
		final List<String> permissionList = new ArrayList<String>();
		addPermission(permissionList, Manifest.permission.READ_EXTERNAL_STORAGE);
		addPermission(permissionList, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		addPermission(permissionList, Manifest.permission.GET_ACCOUNTS);
		addPermission(permissionList, Manifest.permission.RECORD_AUDIO);
		addPermission(permissionList, Manifest.permission.INTERNET);
        addPermission(permissionList, Manifest.permission.CAMERA);
        addPermission(permissionList, Manifest.permission.MODIFY_PHONE_STATE);
        addPermission(permissionList, Manifest.permission.CHANGE_NETWORK_STATE);

		if (permissionList.size() > 0) {
			requestPermissions(permissionList.toArray(new String[permissionList.size()]), MY_PERMISSIONS_REQUEST_ACCESS_CODE);
		}
	}

	@TargetApi(Build.VERSION_CODES.M)
	private boolean addPermission(List<String> permissionsList, String permission) {
		if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
			permissionsList.add(permission);
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		//unregisterReceiver(receiver);
	}


	private  void savePreferenceData(String type,boolean isExist){
		SharedPreferences sp =getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
		SharedPreferences.Editor et = sp.edit();
		et.putBoolean(type, isExist);
		et.commit();
	}
    private  void saveStringPreferenceData(String type,String value){
        SharedPreferences sp =getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString(type, value);
        et.commit();
    }
    private  String getStringPreferenceData(String type){
        SharedPreferences sp = getSharedPreferences(getResources().getString(R.string.app_name), 0);
        String cb1 = sp.getString(type, "");
        return cb1;
    }
	private  boolean getPreferenceData(String type){
		SharedPreferences sp = getSharedPreferences(getResources().getString(R.string.app_name), 0);
		boolean cb1 = sp.getBoolean(type, false);
		return cb1;
	}

	private void getMailId(){
        String[] accounts ;
        AccountManager accManager = AccountManager.get(getApplicationContext());
        Account acc[] = accManager.getAccountsByType("com.google");
        int accCount = acc.length;
        accounts = new String[acc.length];
        for(int i = 0; i < accCount; i++)
        {
            accounts[i]=acc[i].name.toString();
            //Do your task here...
           // Toast.makeText(getApplicationContext(),acc[i].name, Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, accounts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String valu=getStringPreferenceData(MAILID);
        if(valu!=null&&valu.length()>0){
            for(int i=0;i<spinner.getAdapter().getCount();i++){
                String name=spinner.getAdapter().getItem(i).toString();
                if(name.equalsIgnoreCase(valu)){
                    spinner.setSelection(i);
                }
            }

        }

    }

    private void getAccount(){
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;

            }
        }
    }
}