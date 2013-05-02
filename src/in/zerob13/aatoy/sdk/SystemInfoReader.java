/** 
 * Filename:    SystemInfoReader.java
 * Description: Fetch some Information of Android System
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
package in.zerob13.aatoy.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SystemInfoReader {

	/** DEBUG mode */
	private static final boolean DEBUG = false;
	/** Log TAG */
	private static final String LOG_TAG = "SystemInfoReader";

	/** System Memory info List */
	public static final String[] FIELDS_SYS_MEMINFO = { "MemTotal:", "MemFree:", "Buffers:", "Cached:",
			"Active:", "Inactive:", "Dirty:" };
	/** Proc Memory info List */
	public static final String[] FIELDS_PROC_MEMINFO = { "VmLck:", "VmRSS:", "VmSize:", "VmExe:", "VmStk:",
			"VmLib", "Threads:" };

	/**
	 * Fetch CPU Info
	 * 
	 * @return Cpu Info
	 */
	public static String fetchCpuInfo() {
		return readSysInfo("/proc/cpuinfo");
	}

	/**
	 * Fetch Cpu Min Freq
	 * 
	 * @return Cpu Min Freq
	 */
	public static long fetchCpuMinFreq() {
		String min = readSysInfo("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq");
		long freq = 0;
		try {
			if (min != null && min.length() > 0 && min.charAt(min.length() - 1) == '\n') {
				min = min.substring(0, min.length() - 1);
			}
			freq = Long.parseLong(min);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			if (DEBUG) {
				Log.w(LOG_TAG, "fetchCpuMinFreq NumberFormatException Exception", e);
			}
			freq = 0;
		}
		return freq;
	}

	/**
	 * Fetch Cpu Max Freq
	 * 
	 * @return CPU Max Freq
	 */
	public static long fetchCpuMaxFreq() {
		String max = readSysInfo("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
		long freq = 0;
		try {
			if (max != null && max.length() > 0 && max.charAt(max.length() - 1) == '\n') {
				max = max.substring(0, max.length() - 1);
			}
			freq = Long.parseLong(max);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			if (DEBUG) {
				Log.w(LOG_TAG, "fetchCpuMaxFreq NumberFormatException Exception", e);
			}
			freq = 0;
		}
		return freq;
	}

	/**
	 * Fetch Cpu Cores' Number
	 * 
	 * @return the Number of Cpu Cores
	 */
	public static int fetchCpuCoresNum() {
		//Private Class to display only CPU devices in the directory listing
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				//Check if filename is "cpu", followed by a single digit number
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}
		try {
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new CpuFilter());
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			if (DEBUG) {
				Log.w(LOG_TAG, "fetchCpuCoresNum Exception", e);
			}
			return 1;
		}
	}

	/**
	 * reflect to fetch the data /proc/meminfo 
	 * 
	 * @return return an Map like this:
	 * 		MemTotal: long 
	 *		MemFree: long
	 *      Buffers: long 
	 *      Cached:long 
	 *		Active: long
	 *      Inactive: long
	 *      Dirty: long 
	 * 
	 */
	public static Map<String, Long> getSysMemoryInfo() {
		Map<String, Long> result = new HashMap<String, Long>();
		try {
			@SuppressWarnings("rawtypes")
			Class procClass = Class.forName("android.os.Process");
			@SuppressWarnings("rawtypes")
			Class[] parameterTypes = new Class[] { String.class, String[].class, long[].class };
			Method readProclines = procClass.getMethod("readProcLines", parameterTypes);
			if (readProclines != null) {
				Object arglist[] = new Object[3];
				long[] memInfoSizes = new long[FIELDS_SYS_MEMINFO.length];
				memInfoSizes[0] = 30;
				memInfoSizes[1] = -30;
				arglist[0] = new String("/proc/meminfo");
				arglist[1] = FIELDS_SYS_MEMINFO;
				arglist[2] = memInfoSizes;

				readProclines.invoke(null, arglist);
				for (int i = 0; i < memInfoSizes.length; i++) {
					result.put(FIELDS_SYS_MEMINFO[i], memInfoSizes[i]);
				}
			}
		} catch (ClassNotFoundException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getSysMemoryInfo Exception", e);
			}
			return null;
		} catch (SecurityException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getSysMemoryInfo Exception", e);
			}
			return null;
		} catch (IllegalArgumentException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getSysMemoryInfo Exception", e);
			}
			return null;
		} catch (IllegalAccessException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getSysMemoryInfo Exception", e);
			}
			return null;
		} catch (InvocationTargetException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getSysMemoryInfo Exception", e);
			}
			return null;
		} catch (NoSuchMethodException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getSysMemoryInfo Exception", e);
			}
			return null;
		}
		return result;
	}

	/**
	 * reflect to fetch the data /proc/pid/status 
	 * 
	 * @return return an ArrayList like this: 
	 * 			VmLck: long  
	 * 			VmSize: long
	 *          VmExe: long
	 *          VmStk: long 
	 *			VmLib:long 
	 *			Threads: long 
	 */
	public static Map<String, Long> getProcMemoryInfo() {
		Map<String, Long> result = new HashMap<String, Long>();
		try {
			@SuppressWarnings("rawtypes")
			Class procClass = Class.forName("android.os.Process");
			@SuppressWarnings("rawtypes")
			Class[] parameterTypes = new Class[] { String.class, String[].class, long[].class };
			Method readProclines = procClass.getMethod("readProcLines", parameterTypes);
			if (readProclines != null) {
				Object arglist[] = new Object[3];

				long[] memInfoSizes = new long[FIELDS_PROC_MEMINFO.length];
				memInfoSizes[0] = -1;
				int id = android.os.Process.myPid();
				arglist[0] = new String("/proc/" + String.valueOf(id) + "/status");
				arglist[1] = FIELDS_PROC_MEMINFO;
				arglist[2] = memInfoSizes;
				readProclines.invoke(null, arglist);
				for (int i = 0; i < memInfoSizes.length; i++) {
					result.put(FIELDS_PROC_MEMINFO[i], memInfoSizes[i]);
				}
			}
		} catch (ClassNotFoundException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getProcMemoryInfo Exception", e);
			}
			return null;
		} catch (SecurityException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getProcMemoryInfo Exception", e);
			}
			return null;
		} catch (IllegalArgumentException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getProcMemoryInfo Exception", e);
			}
			return null;
		} catch (IllegalAccessException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getProcMemoryInfo Exception", e);
			}
			return null;
		} catch (InvocationTargetException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getProcMemoryInfo Exception", e);
			}
			return null;
		} catch (NoSuchMethodException e) {
			if (DEBUG) {
				Log.w(LOG_TAG, "getProcMemoryInfo Exception", e);
			}
			return null;
		}
		return result;
	}

	/**
	 * read System Files to fetch Info
	 * 
	 * @param aFileString
	 * @return File data
	 */
	private static String readSysInfo(String aFileString) {
		StringBuffer sb = new StringBuffer();
		File cpuinfo = new File(aFileString);
		if (cpuinfo.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(cpuinfo));
				String aLine;
				while ((aLine = br.readLine()) != null) {
					sb.append(aLine + "\n");
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				if (DEBUG) {
					Log.w(LOG_TAG, "readSysInfo Exception", e);
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * get Phone's IMSI
	 * @param aContext
	 * @return IMSI String
	 */
    public static String getIMSIString(Context aContext) {
        TelephonyManager tm = (TelephonyManager) aContext
                  .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId();
        if (imsi == null) {
             return "";
        } else {
             return imsi;
        }
   }
    
    /**
	 * get Phone's IMEI
	 * @param aContext
	 * @return IMEI String
	 */
    public static String getIMEIString(Context aContext) {
        TelephonyManager tm = (TelephonyManager) aContext
                  .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (imei == null) {
             return "";
        } else {
             return imei;
        }
   }
    
    /**
     * get Wifi Mac Address
     * @param aContext
     * @return String Wifi Mac Addresss
     */
    public static String getWifiMacString(Context aContext) {
        WifiManager wifi = (WifiManager) aContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiMac = info.getMacAddress();
        if (wifiMac == null) {
             return "";
        } else {
             return wifiMac;
        }
   }
  
    /**
     * get Ip Address
     * @return String ip Address
     */
   public static String getIpString() {
        try {
             for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                       .hasMoreElements();) {
                  NetworkInterface intf = en.nextElement();
                  for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements();) {
                       InetAddress inetAddress = enumIpAddr.nextElement();
                       if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress().toString();
                       }
                  }
             }
        } catch (SocketException ex) {
             ex.printStackTrace();
        }
        return "";
   }
  
    
}
