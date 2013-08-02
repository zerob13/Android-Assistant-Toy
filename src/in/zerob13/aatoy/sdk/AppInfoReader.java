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

package in.zerob13.aatoy.sdk;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

/**
 * Use To Read Application's Information
 */
public final class AppInfoReader {

	/**
	 * constructor
	 */
	private AppInfoReader() {

	}

	/**
	 * get Installed App Info List
	 * 
	 * @param aContext
	 *            a Context
	 * @return aList of all installed app info
	 */
	public static List<PackageInfo> getInstalledPackage(Context aContext) {
		List<PackageInfo> packages = aContext.getPackageManager().getInstalledPackages(0);
		return packages;

	}

	/**
	 * send intent to android to uninstall a package
	 * 
	 * @param aContext
	 *            a Context
	 * @param packageName
	 *            packageName need to be uninstalled
	 */
	public static void uninstallApp(Context aContext, String packageName) {
		Uri packageURI = Uri.parse("package:" + packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		aContext.startActivity(uninstallIntent);
	}

	/**
	 * send intent to android to install a package (application)
	 * 
	 * @param aContext
	 *            a Context
	 * @param appFile
	 *            app file
	 */
	public static void installApp(Context aContext, File appFile) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//install source
		intent.putExtra("android.intent.extra.INSTALLER_PACKAGE_NAME", aContext.getPackageName());
		intent.setAction(android.content.Intent.ACTION_VIEW);

		intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
		aContext.startActivity(intent);
	}

	/**
	 * send intent to android to install a package
	 * 
	 * @param aContext
	 *            a Context
	 * @param appFilePath
	 *            app file path
	 */
	public static void installApp(Context aContext, String appFilePath) {
		installApp(aContext, new File(appFilePath));
	}
}
