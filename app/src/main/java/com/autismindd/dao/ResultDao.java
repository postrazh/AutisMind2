package com.autismindd.dao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.autismindd.dao.Result;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RESULT".
*/
public class ResultDao extends AbstractDao<Result, Long> {

    public static final String TABLENAME = "RESULT";

    /**
     * Properties of entity Result.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Key = new Property(1, Long.class, "key", false, "KEY");
        public final static Property Active = new Property(2, Boolean.class, "active", false, "ACTIVE");
        public final static Property CreatedAt = new Property(3, java.util.Date.class, "createdAt", false, "CREATED_AT");
        public final static Property UpdatedAt = new Property(4, java.util.Date.class, "updatedAt", false, "UPDATED_AT");
        public final static Property TaskId = new Property(5, long.class, "taskId", false, "TASK_ID");
        public final static Property ItemId = new Property(6, long.class, "itemId", false, "ITEM_ID");
    };

    private Query<Result> task_TaskResultsQuery;
    private Query<Result> item_ResultsQuery;

    public ResultDao(DaoConfig config) {
        super(config);
    }
    
    public ResultDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RESULT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"KEY\" INTEGER," + // 1: key
                "\"ACTIVE\" INTEGER," + // 2: active
                "\"CREATED_AT\" INTEGER," + // 3: createdAt
                "\"UPDATED_AT\" INTEGER," + // 4: updatedAt
                "\"TASK_ID\" INTEGER NOT NULL ," + // 5: taskId
                "\"ITEM_ID\" INTEGER NOT NULL );"); // 6: itemId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RESULT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Result entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long key = entity.getKey();
        if (key != null) {
            stmt.bindLong(2, key);
        }
 
        Boolean active = entity.getActive();
        if (active != null) {
            stmt.bindLong(3, active ? 1L: 0L);
        }
 
        java.util.Date createdAt = entity.getCreatedAt();
        if (createdAt != null) {
            stmt.bindLong(4, createdAt.getTime());
        }
 
        java.util.Date updatedAt = entity.getUpdatedAt();
        if (updatedAt != null) {
            stmt.bindLong(5, updatedAt.getTime());
        }
        stmt.bindLong(6, entity.getTaskId());
        stmt.bindLong(7, entity.getItemId());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Result readEntity(Cursor cursor, int offset) {
        Result entity = new Result( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // key
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // active
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // createdAt
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // updatedAt
            cursor.getLong(offset + 5), // taskId
            cursor.getLong(offset + 6) // itemId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Result entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setKey(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setActive(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setCreatedAt(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setUpdatedAt(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setTaskId(cursor.getLong(offset + 5));
        entity.setItemId(cursor.getLong(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Result entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Result entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "taskResults" to-many relationship of Task. */
    public List<Result> _queryTask_TaskResults(long taskId) {
        synchronized (this) {
            if (task_TaskResultsQuery == null) {
                QueryBuilder<Result> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.TaskId.eq(null));
                task_TaskResultsQuery = queryBuilder.build();
            }
        }
        Query<Result> query = task_TaskResultsQuery.forCurrentThread();
        query.setParameter(0, taskId);
        return query.list();
    }

    /** Internal query to resolve the "results" to-many relationship of Item. */
    public List<Result> _queryItem_Results(long itemId) {
        synchronized (this) {
            if (item_ResultsQuery == null) {
                QueryBuilder<Result> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ItemId.eq(null));
                item_ResultsQuery = queryBuilder.build();
            }
        }
        Query<Result> query = item_ResultsQuery.forCurrentThread();
        query.setParameter(0, itemId);
        return query.list();
    }

}