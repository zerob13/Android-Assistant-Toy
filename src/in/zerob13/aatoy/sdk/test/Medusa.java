/** 
 * Filename:    Medusa.java
 * Description: nothing just a test Activity
 * Copyright:   www.zerob13.in
 * @author:     zerob13 
 * @version:    1.0
 * Create at:   Sep 16, 2012 4:20:41 PM
 * 
 * Modification History: 
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------ 
 * Sep 16, 2012    zerob13      1.0         1.0 Version 
 */
package in.zerob13.aatoy.sdk.test;

import in.zerob13.aatoy.apps.R;
import in.zerob13.aatoy.sdk.SystemInfoReader;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Medusa extends Activity {

	TextView mTestText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medusa);
		mTestText = (TextView) findViewById(R.id.testText);
		StringBuffer testInfoBuffer = new StringBuffer();
		testInfoBuffer.append(SystemInfoReader.fetchCpuInfo() + "\n");
		testInfoBuffer.append("Cpu Max freq: " + SystemInfoReader.fetchCpuMaxFreq() + "Hz\n");
		testInfoBuffer.append("Cpu Min freq: " + SystemInfoReader.fetchCpuMinFreq() + "Hz\n");
		testInfoBuffer.append("Cpu Cores's Num: " + SystemInfoReader.fetchCpuCoresNum() + "\n");
		testInfoBuffer.append(SystemInfoReader.getProcMemoryInfo() + "\n");
		testInfoBuffer.append(SystemInfoReader.getSysMemoryInfo() + "\n");
		testInfoBuffer.append("IMEIInfo: " + SystemInfoReader.getIMEIString(this) + "\n");
		testInfoBuffer.append("IMSIInfo: " + SystemInfoReader.getIMSIString(this) + "\n");
		testInfoBuffer.append("Wifi Mac: " + SystemInfoReader.getWifiMacString(this) + "\n");
		testInfoBuffer.append("Wifi Ip: " + SystemInfoReader.getIpString() + "\n");
		mTestText.setText(testInfoBuffer.toString());
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Log.d("Medusa", item.getTitle().toString());

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
