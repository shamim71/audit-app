package com.versacomllc.audit.data;

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
public class AuditListContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<AuditListPanel> ITEMS = new ArrayList<AuditListPanel>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, AuditListPanel> ITEM_MAP = new HashMap<String, AuditListPanel>();

	static {
		// Add 3 sample items.
		addItem(new AuditListPanel("1", "Recent Audits"));
		addItem(new AuditListPanel("2", "My Audits"));
		addItem(new AuditListPanel("3", "Advance"));
	}

	private static void addItem(AuditListPanel item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class AuditListPanel {
		public String id;
		public String content;

		public AuditListPanel(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
