package com.versacomllc.audit.data;

import com.versacomllc.audit.model.AuditDefect;

public class LocalAuditDefect extends AuditDefect {

	private long localId;

	private long sowId;
	
	private long auditId;

	private String techName;

	private String defectPicBefore;

	private String defectPicAfter;

	private int sync;

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	public String getTechName() {
		return techName;
	}

	public void setTechName(String techName) {
		this.techName = techName;
	}

	public String getDefectPicBefore() {
		return defectPicBefore;
	}

	public void setDefectPicBefore(String defectPicBefore) {
		this.defectPicBefore = defectPicBefore;
	}

	public String getDefectPicAfter() {
		return defectPicAfter;
	}

	public void setDefectPicAfter(String defectPicAfter) {
		this.defectPicAfter = defectPicAfter;
	}

	public int getSync() {
		return sync;
	}

	public void setSync(int sync) {
		this.sync = sync;
	}



	public AuditDefect toAuditDefect() {

		AuditDefect auditDefect = new AuditDefect();

		auditDefect.setCount(this.count);
		auditDefect.setDefectCode(this.defectCode);
		auditDefect.setDefectDescription(this.defectDescription);
		auditDefect.setDefectId(this.defectId);
		auditDefect.setDefectSeverity(this.defectSeverity);

		auditDefect.setDefectPicAfteOnServer(this.defectPicAfteOnServer);
		auditDefect.setDefectPicBeforeOnServer(this.defectPicBeforeOnServer);

		auditDefect.setFixed(fixed);
		auditDefect.setNote(note);
		auditDefect.setRid(rid);
		//auditDefect.setTechId(techId);

		return auditDefect;
	}

	public long getSowId() {
		return sowId;
	}

	public void setSowId(long sowId) {
		this.sowId = sowId;
	}

	public long getAuditId() {
		return auditId;
	}

	public void setAuditId(long auditId) {
		this.auditId = auditId;
	}

}
