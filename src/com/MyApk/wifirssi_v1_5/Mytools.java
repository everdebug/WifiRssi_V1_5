package com.MyApk.wifirssi_v1_5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class Mytools {
	private static int count = 3;

	public Mytools(Context context) {
	}

	public int[] myscan(WifiManager manager, String[] nameString) {
		int[] ave = new int[count];
		List<ScanResult> objList = new ArrayList<ScanResult>();
		List<ScanResult> objList1 = new ArrayList<ScanResult>();
		List<ScanResult> objList2 = new ArrayList<ScanResult>();
		for (int i = 0; i < 30; i++) {
			manager.startScan();
			List<ScanResult> scanlist = manager.getScanResults();
			Iterator<ScanResult> scaniterator = scanlist.iterator();
			for (; scaniterator.hasNext();) {
				ScanResult result = scaniterator.next();
				if (result.SSID.equals(nameString[0])) {
					objList.add(result);
				}
				if (result.SSID.equals(nameString[1])) {
					objList1.add(result);
				}
				if (result.SSID.equals(nameString[2])) {
					objList2.add(result);
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ave[0] = mydeal(objList);
		ave[1] = mydeal(objList1);
		ave[2] = mydeal(objList2);
		return ave;
	}

	public int mydeal(List<ScanResult> list1) {
		int[] lev1 = new int[30];
		for (int j = 0; j < lev1.length; j++) {
			lev1[j] = -200;
		}
		Iterator<ScanResult> iterator1 = list1.iterator();
		for (int i = 0; iterator1.hasNext(); i++) {
			ScanResult result1 = iterator1.next();
			lev1[i] = result1.level;
		}
		int ave = Ave.average(lev1);
		return ave;
	}
}
