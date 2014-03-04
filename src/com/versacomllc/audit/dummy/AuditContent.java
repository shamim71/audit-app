package com.versacomllc.audit.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class AuditContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<Tab> ITEMS = new ArrayList<Tab>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, Tab> ITEM_MAP = new HashMap<String, Tab>();

	static {
		// Add 3 sample items.
		addItem(new Tab("1", "Audit Master"));
		addItem(new Tab("2", "Scope of Work"));

	}

	private static void addItem(Tab item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class Tab {
		public String id;
		public String content;

		public Tab(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
