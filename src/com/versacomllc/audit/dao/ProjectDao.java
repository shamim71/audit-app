package com.versacomllc.audit.dao;

import java.util.List;

import com.versacomllc.audit.data.LocalProject;
import com.versacomllc.audit.model.Project;

public interface ProjectDao {
	public static final String TABLE_NAME = "project";

	public static final String ID = "id";
	public static final String RID = "rid";
	public static final String NAME = "name";
	public static final String SYNC = "sync";


	// Create table
	public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE "
			+ TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY," + NAME + " TEXT ,"
			+ SYNC + " INTEGER ," + RID + " TEXT "
			+ ")";

	public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	List<LocalProject> getAllProjects();
	
	void addProjectList(List<Project> projects);

	void addProject(Project project);
	
	
	int update(LocalProject project);
	

}
