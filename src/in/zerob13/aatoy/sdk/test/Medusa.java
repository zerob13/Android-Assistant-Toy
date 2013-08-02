/*
 * Copyright (C) 2013 Zerob13 (http://www.zerob13.in)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
