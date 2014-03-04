package com.versacomllc.audit.dao.impl;

import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.versacomllc.audit.dao.ProjectDao;
import com.versacomllc.audit.data.LocalProject;
import com.versacomllc.audit.model.Project;


public class ProjectDaoImpl implements ProjectDao {

	private SQLiteOpenHelper helper;

	public ProjectDaoImpl(SQLiteOpenHelper helper) {
		super();
		this.helper = helper;
	}




	private ContentValues createContentValues(LocalProject obj) {

		ContentValues values = new ContentValues();
		values.put(NAME, obj.getName());
		values.put(SYNC, obj.getSync());
		values.put(RID, obj.getRid());

		return values;
	}



	public LocalProject findLocalProjectByRId(SQLiteDatabase db,String rid) {
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where " + RID
				+ "=" + rid + "";

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalProject> list = loadAllProjects(cursor);
		cursor.close();

		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}




	private List<LocalProject> loadAllProjects(Cursor cursor) {
		List<LocalProject> list = new ArrayList<LocalProject>();

		if (cursor.moveToFirst()) {
			do {
				int index = 0;
				LocalProject obj = new LocalProject();
				obj.setId(cursor.getLong(index++));
				obj.setName(cursor.getString(index++));
				obj.setSync(cursor.getLong(index++));
				obj.setRid(cursor.getString(index++));
				list.add(obj);

			} while (cursor.moveToNext());
		}

		return list;
	}

	@Override
	public List<LocalProject> getAllProjects() {

		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalProject> projects = loadAllProjects(cursor);
		cursor.close();
		db.close();

		return projects;

	}


	@Override
	public void addProjectList(List<Project> projects) {
		SQLiteDatabase db = helper.getWritableDatabase();
		for (Project p : projects) {
			LocalProject existing = findLocalProjectByRId(db, p.getRid());
			
			
			if (existing == null) {
				LocalProject lp = new LocalProject();
				lp.setName(p.getName());
				lp.setRid(p.getRid());
				lp.setSync(1);
				
				ContentValues values = createContentValues(lp);
				
				db.insert(TABLE_NAME, null, values);
			} else {
				existing.setName(p.getName());
				existing.setSync(1);
				ContentValues values = createContentValues(existing);
				int rowEffected = db.update(TABLE_NAME, values, RID + " = ?",
						new String[] { String.valueOf(existing.getRid()) });
				Log.d(LOG_TAG, "Record updated: " + rowEffected);
			}
		}

		db.close();

		
	}


	@Override
	public int update(LocalProject project) {
		ContentValues values = createContentValues(project);
		// updating row
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected = db.update(TABLE_NAME, values, ID + " = ?",
				new String[] { String.valueOf(project.getId()) });
		Log.d(LOG_TAG, "Record updated: " + rowEffected);
		db.close();
		return rowEffected;
	}




	@Override
	public void addProject(Project p) {
		SQLiteDatabase db = helper.getWritableDatabase();
		LocalProject lp = new LocalProject();
		lp.setName(p.getName());
		lp.setRid(p.getRid());
		lp.setSync(1);
		
		ContentValues values = createContentValues(lp);
		
		db.insert(TABLE_NAME, null, values);
		db.close();
	}

}
