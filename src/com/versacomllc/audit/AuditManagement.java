package com.versacomllc.audit;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Application;

import com.google.gson.Gson;
import com.versacomllc.audit.model.AuthenticationResponse;
import com.versacomllc.audit.model.CheckedInventoryItem;

import com.versacomllc.audit.model.InventorySite;
import com.versacomllc.audit.model.ItemInventory;
import com.versacomllc.audit.utils.FileDataStorageManager;
import com.versacomllc.audit.utils.FileDataStorageManager.StorageFile;

/**
 * In-memory state cache
 */
public class AuditManagement extends Application {

	private static List<CheckedInventoryItem> checkedItems = new ArrayList<CheckedInventoryItem>();

	private static CheckedInventoryItem currentItem;

	private Gson jsonHelper = new Gson();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public static List<CheckedInventoryItem> getCheckedItems() {
		return checkedItems;
	}

	public static void setCheckedItems(List<CheckedInventoryItem> checkedItems) {
		AuditManagement.checkedItems = checkedItems;
	}

	public static void addCheckedItem(CheckedInventoryItem checkedItem) {

		boolean exist = false;

		for (CheckedInventoryItem item : checkedItems) {
			if (item.getListID().equals(checkedItem.getListID())) {
				item.setCount(item.getCount() + checkedItem.getCount());
				exist = true;
				break;
			}
		}
		if (!exist) {
			AuditManagement.checkedItems.add(checkedItem);
		}
	}

	public static void updateCheckedItem(CheckedInventoryItem checkedItem) {

		for (CheckedInventoryItem item : checkedItems) {
			if (item.getListID().equals(checkedItem.getListID())) {
				item.setCount(checkedItem.getCount());
				break;
			}
		}

	}

	public static CheckedInventoryItem getCurrentItem() {
		return currentItem;
	}

	public static void setCurrentItem(CheckedInventoryItem currentItem) {
		AuditManagement.currentItem = currentItem;
	}
	public void saveAuthentication(AuthenticationResponse object) {
		final String jsonContent = jsonHelper.toJson(object);
		FileDataStorageManager.saveContentToFile(getBaseContext(),
				StorageFile.USER_AUTHENTICATION, jsonContent);
	}

	public AuthenticationResponse getAuthentication(){
		return loadObject(AuthenticationResponse.class,
				StorageFile.USER_AUTHENTICATION);
	}
	


	public void saveInventorySites(InventorySite[] sites){
		final String jsonContent = jsonHelper.toJson(sites);
		FileDataStorageManager.saveContentToFile(getBaseContext(),
				StorageFile.INVENTORY_SITES, jsonContent);
	}
	public InventorySite[] getInventorySites(){
		return loadObject(InventorySite[].class,
				StorageFile.INVENTORY_SITES);
	}
	
	public void saveInventoryItems(ItemInventory[] items){
		final String jsonContent = jsonHelper.toJson(items);
		FileDataStorageManager.saveContentToFile(getBaseContext(),
				StorageFile.INVENTORY_ITEMS, jsonContent);
	}
	public ItemInventory[] getInventoryItems(){
		return loadObject(ItemInventory[].class,
				StorageFile.INVENTORY_ITEMS);
	}
	
	public InventorySite getInventorySite(final String listID){
		InventorySite[] sites = getInventorySites();
		if(sites == null) return null;
		InventorySite mSite = null;
		for(InventorySite site: sites){
			if(site.getListID().equals(listID)){
				mSite = site;
				break;
			}
		}
		return mSite;
	}
	private <T> T loadObject(Class<T> type, StorageFile name) {
		String jsonContent = FileDataStorageManager.getContentFromFile(
				getBaseContext(), name);

		if (StringUtils.isEmpty(jsonContent))
			return null;

		T dtos = jsonHelper.fromJson(jsonContent, type);

		return dtos;
	}


}
