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
public class AuditDefectContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<DefectTab> ITEMS = new ArrayList<DefectTab>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, DefectTab> ITEM_MAP = new HashMap<String, DefectTab>();

	static {
		// Add 3 sample items.
		addItem(new DefectTab("1", "Audit Defect"));
		addItem(new DefectTab("2", "Defect Photos"));
	
	}

	private static void addItem(DefectTab item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DefectTab {
		public String id;
		public String content;

		public DefectTab(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
