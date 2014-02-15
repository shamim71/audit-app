package com.versacomllc.audit.model;

public class AuditDefect {

	private String count;
	
	private String defectId;
	
	private String note;

	private String Id;
	
	private String techId;
	
	private String defectCode;
	
	private String defectSeverity;
	
	private String defectDescription;
	
	private String fixed;
	
	
	private String defectPicBeforeOnServer;
	
	private String defectPicAfteOnServer;
	
	
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getDefectId() {
		return defectId;
	}

	public void setDefectId(String defectId) {
		this.defectId = defectId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getDefectCode() {
		return defectCode;
	}

	public void setDefectCode(String defectCode) {
		this.defectCode = defectCode;
	}

	public String getDefectSeverity() {
		return defectSeverity;
	}

	public void setDefectSeverity(String defectSeverity) {
		this.defectSeverity = defectSeverity;
	}

	public String getDefectDescription() {
		return defectDescription;
	}

	public void setDefectDescription(String defectDescription) {
		this.defectDescription = defectDescription;
	}

	public String getTechId() {
		return techId;
	}

	public void setTechId(String techId) {
		this.techId = techId;
	}

	public String getFixed() {
		return fixed;
	}

	public void setFixed(String fixed) {
		this.fixed = fixed;
	}
	
}
