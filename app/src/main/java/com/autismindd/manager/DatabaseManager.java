package com.autismindd.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.autismindd.dao.DaoSession;
import com.autismindd.dao.FirstLayerDao;
import com.autismindd.dao.FirstLayerTaskImage;
import com.autismindd.dao.FirstLayerTaskImageDao;
import com.autismindd.dao.GiftTbl;
import com.autismindd.dao.GiftTblDao;
import com.autismindd.dao.Result;
import com.autismindd.dao.ResultDao;
import com.autismindd.dao.StarDao;
import com.autismindd.dao.Task;
import com.autismindd.dao.TaskDao;
import com.autismindd.dao.TimeStatistics;
import com.autismindd.dao.TimeStatisticsDao;
import com.autismindd.dao.UserDao;
import com.autismindd.dao.DaoMaster;
import com.autismindd.dao.ErrorUserLog;
import com.autismindd.dao.ErrorUserLogDao;
import com.autismindd.dao.FirstLayer;
import com.autismindd.dao.Item;
import com.autismindd.dao.ItemDao;
import com.autismindd.dao.Star;
import com.autismindd.dao.TaskPack;
import com.autismindd.dao.TaskPackDao;
import com.autismindd.dao.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * @author Octa
 */
public class DatabaseManager implements IDatabaseManager, AsyncOperationListener {

    /**
     * Class tag. Used for debug.
     */
    private static final String TAG = DatabaseManager.class.getCanonicalName();
    /**
     * Instance of DatabaseManager
     */
    private static DatabaseManager instance;
    /**
     * The Android Activity reference for access to DatabaseManager.
     */
    private Context context;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private List<AsyncOperation> completedOperations;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     *
     * @param context The Android {@link android.content.Context}.
     */
    public DatabaseManager(final Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(this.context, "sample-database", null);
        completedOperations = new CopyOnWriteArrayList<AsyncOperation>();
    }

    /**
     * @param context The Android {@link android.content.Context}.
     * @return this.instance
     */
    public static DatabaseManager getInstance(Context context) {

        if (instance == null) {
            instance = new DatabaseManager(context);
        }

        return instance;
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        completedOperations.add(operation);
    }

    private void assertWaitForCompletion1Sec() {
        asyncSession.waitForCompletion(1000);
        asyncSession.isCompleted();
    }

    /**
     * Query for readable DB
     */
    public void openReadableDb() throws SQLiteException {
        database = mHelper.getReadableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    /**
     * Query for writable DB
     */
    public void openWritableDb() throws SQLiteException {
        database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    @Override
    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    public synchronized void dropDatabase() {
        try {
            openWritableDb();
            DaoMaster.dropAllTables(database, true); // drops all tables
            mHelper.onCreate(database);              // creates the tables
            asyncSession.deleteAll(User.class);    // clear all elements from a table
            asyncSession.deleteAll(Task.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***************************************
     * USER Table Operation
     ************************************/
    @Override
    public synchronized User insertUser(User user) {
        try {
            if (user != null) {
                openWritableDb();
                UserDao userDao = daoSession.getUserDao();
                userDao.insert(user);
                Log.d(TAG, "Inserted user: " + user.getName() + " to the schema.");
                Log.d(TAG, "Inserted userPic: " + user.getPic() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public synchronized ArrayList<User> listUsers() {
        List<User> users = null;
        try {
            openReadableDb();
            UserDao userDao = daoSession.getUserDao();
            users = userDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (users != null) {
            return new ArrayList<>(users);
        }
        return null;
    }

    @Override
    public synchronized Long updateUser(User user) {
        Long userKey = null;
        try {
            if (user != null) {
                openWritableDb();
                daoSession.update(user);
                Log.d(TAG, "Updated user: " + user.getName() + " from the schema.");
                userKey = user.getKey();
                daoSession.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return userKey;
    }

    @Override
    public synchronized User updateUsers(User user) {

        try {
            if (user != null) {
                openWritableDb();
                daoSession.update(user);
                Log.d(TAG, "Updated user: " + user.getName() + " from the schema.");
                daoSession.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return user;
    }

    @Override
    public synchronized void deleteUserByEmail(String email) {
        try {
            openWritableDb();
            UserDao userDao = daoSession.getUserDao();
            QueryBuilder<User> queryBuilder = userDao.queryBuilder().where(UserDao.Properties.Name.eq(email));
            List<User> userToDelete = queryBuilder.list();
            for (User user : userToDelete) {
                userDao.delete(user);
            }
            daoSession.clear();
            Log.d(TAG, userToDelete.size() + " entry. " + "Deleted user: " + email + " from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized boolean deleteUserById(Long userId) {
        try {
            openWritableDb();
            UserDao userDao = daoSession.getUserDao();
            userDao.deleteByKey(userId);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUserByKey(Long key) {
        boolean isRemoved = false;
        try {
            openWritableDb();
            UserDao userDao = daoSession.getUserDao();
            QueryBuilder<User> queryBuilder = userDao.queryBuilder().where(UserDao.Properties.Key.eq(key));
            List<User> userArray = queryBuilder.list();
            if (userArray.size() > 0) {
                for (User user : userArray) {
                    userDao.delete(user);
                }

                daoSession.clear();
                isRemoved = true;
            }
        } catch (Exception e) {
            isRemoved = false;
            e.printStackTrace();
        }
        return isRemoved;
    }

    @Override
    public synchronized User getUserById(Long userId) {
        User user = null;
        try {
            openReadableDb();
            UserDao userDao = daoSession.getUserDao();
            user = userDao.load(userId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public synchronized void deleteUsers() {
        try {
            openWritableDb();
            UserDao userDao = daoSession.getUserDao();
            userDao.deleteAll();
            daoSession.clear();
            Log.d(TAG, "Delete all users from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***************************************
     * End USER Table Operation
     ************************************/
    @Override
    public Long insertTask(Task task) {
        Long id = null;
        try {
            if (task != null) {
                openWritableDb();
                TaskDao taskDao = daoSession.getTaskDao();
                id = taskDao.insert(task);
                Log.d(TAG, "Inserted task: " + task.getName() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public List<Task> listTasks() {
        List<Task> tasks = null;
        try {
            openReadableDb();
            TaskDao taskDao = daoSession.getTaskDao();
            tasks = taskDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tasks != null) {
            return tasks = new ArrayList<>(tasks);
        }
        return tasks;
    }

    @Override
    public List<Task> listTasksByTAskPackId(long taskPackId) {
        List<Task> tasks = null;
        try {
            openWritableDb();
            TaskDao taskDao = daoSession.getTaskDao();
            QueryBuilder<Task> queryBuilder = taskDao.queryBuilder().where(TaskDao.Properties.TaskPackId.eq(taskPackId)).orderAsc(TaskDao.Properties.SlideSequence);
            tasks = queryBuilder.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tasks != null) {
            return tasks = new ArrayList<>(tasks);
        }
        return tasks;
    }

    @Override
    public void updateTask(Task task) {
        try {
            if (task != null) {
                openWritableDb();
                daoSession.update(task);
                Log.d(TAG, "Updated task: " + task.getName() + " from the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteTaskById(Long taskId) {
        try {
            openWritableDb();
            TaskDao taskDao = daoSession.getTaskDao();
            taskDao.deleteByKey(taskId);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Task getTaskById(Long taskId) {
        Task task = null;
        try {
            openReadableDb();
            TaskDao taskDao = daoSession.getTaskDao();
            task = taskDao.load(taskId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public void deleteTasks() {
        try {
            openWritableDb();
            TaskDao taskDao = daoSession.getTaskDao();
            taskDao.deleteAll();
            daoSession.clear();
            Log.d(TAG, "Delete all tasks from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public User userWiseTask() {
        User user = null;
        try {
            openReadableDb();
            ArrayList<User> users = new ArrayList<>();
            Cursor cursor = daoSession.getDatabase().rawQuery("Select * from User", null);

            try {
                DateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                if (cursor.moveToFirst()) {
                    do {
                        user = new User();
                        user.setId(Long.valueOf(cursor.getString(cursor.getColumnIndex(UserDao.Properties.Id.columnName))));
                        user.setName(cursor.getString(cursor.getColumnIndex(UserDao.Properties.Password.columnName)));
                        user.setPassword(cursor.getString(cursor.getColumnIndex(UserDao.Properties.Password.columnName)));
                        user.setActive(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(UserDao.Properties.Active.columnName))));
                        //user.setCreatedAt((Date) dateFormat.parse(cursor.getString(cursor.getColumnIndex(UserDao.Properties.CreatedAt.columnName))));
                        //user.setUpdatedAt((Date) dateFormat.parse(cursor.getString(cursor.getColumnIndex(UserDao.Properties.UpdatedAt.columnName))));
                        users.add(user);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
            return user;
        }
        return user;
    }

    @Override
    public long insertItem(Item item) {
        Long id = null;
        try {
            if (item != null) {
                openWritableDb();
                ItemDao itemDao = daoSession.getItemDao();
                id = itemDao.insert(item);
                Log.d(TAG, "Inserted task: " + item.getX() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public List<Item> listItems() {
        List<Item> items = null;
        try {
            openReadableDb();
            ItemDao itemDao = daoSession.getItemDao();
            items = itemDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (items != null) {
            return new ArrayList<>(items);
        }
        return items;
    }

    @Override
    public void updateItem(Item item) {
        try {
            if (item != null) {
                openWritableDb();
                daoSession.update(item);
                Log.d(TAG, "Updated item: " + item.getX() + " from the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteItemById(Long itemId) {
        try {
            openWritableDb();
            ItemDao itemDao = daoSession.getItemDao();
            itemDao.deleteByKey(itemId);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Item getItemById(Long itemId) {
        Item item = null;
        try {
            openReadableDb();
            ItemDao itemDao = daoSession.getItemDao();
            item = itemDao.load(itemId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public void deleteItems() {
        try {
            openWritableDb();
            ItemDao itemDao = daoSession.getItemDao();
            itemDao.deleteAll();
            daoSession.clear();
            Log.d(TAG, "Delete all items from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result insertResult(Result result) {
        try {
            if (result != null) {
                openWritableDb();
                ResultDao resultDao = daoSession.getResultDao();
                resultDao.insert(result);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Result> listResults() {
        List<Result> results = null;
        try {
            openReadableDb();
            ResultDao resultDao = daoSession.getResultDao();
            results = resultDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (results != null) {
            return new ArrayList<>(results);
        }
        return results;
    }

    @Override
    public void updateResult(Result result) {
        try {
            if (result != null) {
                openWritableDb();
                daoSession.update(result);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteResultById(Long resultId) {
        try {
            openWritableDb();
            ResultDao resultDao = daoSession.getResultDao();
            resultDao.deleteByKey(resultId);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Result getResultById(Long resultId) {
        Result result = null;
        try {
            openReadableDb();
            ResultDao resultDao = daoSession.getResultDao();
            result = resultDao.load(resultId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void deleteResults() {
        try {
            openWritableDb();
            ResultDao resultDao = daoSession.getResultDao();
            resultDao.deleteAll();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Checking correct ans
    public boolean getCorrectResult(Long key) {
        boolean result = false;
        try {
            openReadableDb();
            ArrayList<User> users = new ArrayList<>();
            Cursor cursor = daoSession.getDatabase().rawQuery("Select * from Result where key =" + key, null);

            try {
                if (cursor.moveToFirst()) {
                    do {
                        result = true;
                    } while (cursor.moveToNext());
                } else {
                    result = false;
                }
            } finally {
                cursor.close();
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
// read data Item wise

    @Override
    public LinkedHashMap<Long, Item> loadTaskWiseItem(Task task) {
        LinkedHashMap<Long, Item> items = new LinkedHashMap<>();
        try {
            openReadableDb();
            String query = "Select * from Item where task =" + task.getId();
            Cursor cursor = daoSession.getDatabase().rawQuery(query, null);

            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setId(cursor.getLong(cursor.getColumnIndex(ItemDao.Properties.Id.columnName)));
                        item.setX(cursor.getFloat(cursor.getColumnIndex(ItemDao.Properties.X.columnName)));
                        item.setY(cursor.getFloat(cursor.getColumnIndex(ItemDao.Properties.Y.columnName)));
                        item.setRotation(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.Rotation.columnName)));
                        item.setKey(cursor.getLong(cursor.getColumnIndex(ItemDao.Properties.Key.columnName)));
                        item.setIsCircleView(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.IsCircleView.columnName)));
                        item.setCircleColor(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.CircleColor.columnName)));
                        item.setUserText(cursor.getString(cursor.getColumnIndex(ItemDao.Properties.UserText.columnName)));
                        item.setTextColor(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.TextColor.columnName)));
                        item.setTextSize(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.TextSize.columnName)));
                        item.setBorderColor(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.BorderColor.columnName)));
                        item.setBackgroundColor(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.BackgroundColor.columnName)));
                        item.setDrawable(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.Drawable.columnName)));
                        item.setWidth(cursor.getFloat(cursor.getColumnIndex(ItemDao.Properties.Width.columnName)));
                        item.setHeight(cursor.getFloat(cursor.getColumnIndex(ItemDao.Properties.Height.columnName)));
                        item.setLeft(cursor.getFloat(cursor.getColumnIndex(ItemDao.Properties.Left.columnName)));
                        item.setRight(cursor.getFloat(cursor.getColumnIndex(ItemDao.Properties.Right.columnName)));
                        item.setTop(cursor.getFloat(cursor.getColumnIndex(ItemDao.Properties.Top.columnName)));
                        item.setBottom(cursor.getFloat(cursor.getColumnIndex(ItemDao.Properties.Bottom.columnName)));
                        item.setImagePath(cursor.getString(cursor.getColumnIndex(ItemDao.Properties.ImagePath.columnName)));
                        item.setTaskId(cursor.getLong(cursor.getColumnIndex(ItemDao.Properties.TaskId.columnName)));
                        item.setType(cursor.getString(cursor.getColumnIndex(ItemDao.Properties.Type.columnName)));
                        item.setResult(cursor.getString(cursor.getColumnIndex(ItemDao.Properties.Result.columnName)));
                        item.setAllowDragDrop(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.AllowDragDrop.columnName)));
                        item.setDragDropTarget(cursor.getLong(cursor.getColumnIndex(ItemDao.Properties.DragDropTarget.columnName)));
                        item.setNavigateTo(cursor.getLong(cursor.getColumnIndex(ItemDao.Properties.NavigateTo.columnName)));


                        item.setOpenApp(cursor.getString(cursor.getColumnIndex(ItemDao.Properties.OpenApp.columnName)));
                        item.setShowedBy(cursor.getLong(cursor.getColumnIndex(ItemDao.Properties.ShowedBy.columnName)));
                        item.setHideBy(cursor.getLong(cursor.getColumnIndex(ItemDao.Properties.HideBy.columnName)));
                        item.setCornerRound(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.CornerRound.columnName)));
                        item.setCloseApp(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.CloseApp.columnName)));
                        //item.setAllowAlign(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.AllowAlign.columnName)));
                        //item.setAlignTarget(cursor.getLong(cursor.getColumnIndex(ItemDao.Properties.AlignTarget.columnName)));
                        //item.setAlignType(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.AlignType.columnName)));
                        //this line add for get open Url added by reaz
                        item.setOpenUrl(cursor.getString(cursor.getColumnIndex(ItemDao.Properties.OpenUrl.columnName)));
                        item.setItemSound(cursor.getString(cursor.getColumnIndex(ItemDao.Properties.ItemSound.columnName)));
                        item.setFontTypeFace(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.FontTypeFace.columnName)));   // added reaz
                        item.setFontAlign(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.FontAlign.columnName)));        // added reaz

                        item.setAutoPlay(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.AutoPlay.columnName)));         // added reaz
                        item.setSoundDelay(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.SoundDelay.columnName)));    // added reaz
                        item.setBorderPixel(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.BorderPixel.columnName)));
                        item.setShowedByTarget(cursor.getString(cursor.getColumnIndex(ItemDao.Properties.ShowedByTarget.columnName)));
                        item.setHiddenByTarget(cursor.getString(cursor.getColumnIndex(ItemDao.Properties.HiddenByTarget.columnName)));
                        item.setHelper(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.Helper.columnName)));
                        // TUTORIAL TAG
                        item.setTutorialX(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.TutorialX.columnName)));
                        item.setTutorialY(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.TutorialY.columnName)));
//                        item.setTutorialLeft(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.TutorialLeft.columnName)));
//                        item.setTutorialRight(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.TutorialRight.columnName)));
                        item.setTutorialTag(cursor.getLong(cursor.getColumnIndex(ItemDao.Properties.TutorialTag.columnName)));
                        item.setTutorialAnimation(cursor.getInt(cursor.getColumnIndex(ItemDao.Properties.TutorialAnimation.columnName)));


                        items.put(cursor.getLong(cursor.getColumnIndex(ItemDao.Properties.Key.columnName)), item);


                    } while (cursor.moveToNext());

                }
            } finally {
                cursor.close();
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public Long insertTaskPack(TaskPack taskPask) {
        Long id = null;
        try {
            if (taskPask != null) {
                openWritableDb();
                TaskPackDao taskPackDao = daoSession.getTaskPackDao();
                id = taskPackDao.insert(taskPask);
                Log.d(TAG, "Inserted task: " + taskPask.getName() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public List<TaskPack> listTaskPacks() {
        List<TaskPack> taskPacks = null;
        try {
            openReadableDb();
            TaskPackDao taskPackDao = daoSession.getTaskPackDao();
            taskPacks = taskPackDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (taskPacks != null) {
            return taskPacks = new ArrayList<>(taskPacks);
        }
        return taskPacks;
    }

    //level wise get taskPack
    @Override
    public List<TaskPack> listTaskPacksLevel(int fLayerID, int levelID) {
        List<TaskPack> taskPacks = null;
        try {
            openWritableDb();
            TaskPackDao taskPackDao = daoSession.getTaskPackDao();
            QueryBuilder<TaskPack> queryBuilder = taskPackDao.queryBuilder().where(TaskPackDao.Properties.Level.eq(levelID), TaskPackDao.Properties.FirstLayerTaskID.eq(fLayerID)).orderAsc(TaskPackDao.Properties.Id);
            taskPacks = queryBuilder.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (taskPacks != null) {
            return taskPacks = new ArrayList<>(taskPacks);
        }
        return taskPacks;
    }

    //firstLayerID wise get taskPack
    @Override
    public List<TaskPack> listTaskPacksFirstLayerID(int firstLayerID) {
        List<TaskPack> taskPacks = null;
        try {
            openWritableDb();
            TaskPackDao taskPackDao = daoSession.getTaskPackDao();
            QueryBuilder<TaskPack> queryBuilder = taskPackDao.queryBuilder().where(TaskPackDao.Properties.FirstLayerTaskID.eq(firstLayerID)).orderAsc(TaskPackDao.Properties.FirstLayerTaskID).orderAsc(TaskPackDao.Properties.Level);
            taskPacks = queryBuilder.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (taskPacks != null) {
            return taskPacks = new ArrayList<>(taskPacks);
        }
        return taskPacks;
    }

    @Override
    public void updateTaskPack(TaskPack taskPack) {
        try {
            if (taskPack != null) {
                openWritableDb();
                daoSession.update(taskPack);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteTaskPackById(Long taskPackId) {
        try {
            openWritableDb();
            TaskPackDao taskPackDao = daoSession.getTaskPackDao();
            taskPackDao.deleteByKey(taskPackId);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // get taskPack by taskPack ID
    @Override
    public TaskPack getTaskPackById(Long taskPackId) {

        TaskPack taskPack = null;
        try {
            openReadableDb();
            TaskPackDao taskPackDao = daoSession.getTaskPackDao();
            taskPack = taskPackDao.load(taskPackId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskPack;
    }

    @Override
    public void deleteTaskPacks() {

    }

    @Override
    public void deleteTasksByTaskPack(long taskPackId) {
        try {
            openWritableDb();
            TaskDao taskDao = daoSession.getTaskDao();
            QueryBuilder<Task> queryBuilder = taskDao.queryBuilder().where(TaskDao.Properties.TaskPackId.eq(taskPackId));
            List<Task> tasks = queryBuilder.list();
            for (Task task : tasks) {
                taskDao.delete(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getMaxTaskPosition(long taskPackId) {
        openWritableDb();
        TaskDao taskDao = daoSession.getTaskDao();
        QueryBuilder<Task> queryBuilder = taskDao.queryBuilder().where(TaskDao.Properties.TaskPackId.eq(taskPackId)).orderDesc(TaskDao.Properties.SlideSequence).limit(1);
        List<Task> tasks = queryBuilder.list();
        if (tasks.size() > 0) {
            return tasks.get(0).getSlideSequence();
        } else {
            return 0;
        }


    }

    @Override
    public void deleteItemsByTask(long taskId) {
        try {
            openWritableDb();
            ItemDao itemDao = daoSession.getItemDao();
            QueryBuilder<Item> queryBuilder = itemDao.queryBuilder().where(ItemDao.Properties.Task.eq(taskId));
            List<Item> items = queryBuilder.list();
            for (Item task : items) {
                itemDao.delete(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Item> loadItemByTask(Task task) {
        try {
            openWritableDb();
            ItemDao itemDao = daoSession.getItemDao();
            QueryBuilder<Item> queryBuilder = itemDao.queryBuilder().where(ItemDao.Properties.Task.eq(task.getId()));
            ArrayList<Item> items = (ArrayList<Item>) queryBuilder.list();
            daoSession.clear();

            return items;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    //firstLayer insert
    @Override
    public synchronized FirstLayer insertFirstLayer(FirstLayer firstLayer) {
        try {
            if (firstLayer != null) {
                openWritableDb();
                FirstLayerDao firstLayerDao = daoSession.getFirstLayerDao();
                firstLayerDao.insert(firstLayer);
                Log.d(TAG, "Inserted user: " + firstLayer.getName() + " to the schema.");
                Log.d(TAG, "Inserted userPic: " + firstLayer.getImgUrl() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstLayer;
    }

    //  get all list of FirstLayer
    @Override
    public synchronized ArrayList<FirstLayer> listFirstLayer() {
        List<FirstLayer> firstLayers = null;
        try {
            openReadableDb();
            FirstLayerDao firstLayerDao = daoSession.getFirstLayerDao();
            firstLayers = firstLayerDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (firstLayers != null) {
            return new ArrayList<>(firstLayers);
        }
        return null;
    }


    @Override
    public synchronized void updateFirstLayer(FirstLayer firstLayer) {

        try {
            if (firstLayer != null) {
                openWritableDb();
                daoSession.update(firstLayer);
                daoSession.clear();
                Log.d("download", "Update firstLayer : " + firstLayer.getFileName() + " to the schema.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Status wise get firstLayer List
    @Override
    public synchronized ArrayList<FirstLayer> getStatusTrueFirstLayerList(boolean status) {
        List<FirstLayer> firstLayers = null;
        try {
            openReadableDb();
            FirstLayerDao firstLayerDao = daoSession.getFirstLayerDao();
            QueryBuilder<FirstLayer> queryBuilder = firstLayerDao.queryBuilder().where(FirstLayerDao.Properties.Locked.eq(status));
            firstLayers = queryBuilder.list();
//            firstLayers = firstLayerDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (firstLayers != null) {
            return new ArrayList<>(firstLayers);
        }
        return null;
    }

    /***************************************
     * ErrorUserLog Table Operation
     ************************************/
    //insert ErrorUserLog
    @Override
    public synchronized ErrorUserLog insertErrorUserLogByUserKey(ErrorUserLog errorUserLog) {
        try {
            if (errorUserLog != null) {
                openWritableDb();
                ErrorUserLogDao errorUserLogDao = daoSession.getErrorUserLogDao();
                errorUserLogDao.insert(errorUserLog);
                Log.d(TAG, "Inserted user: " + errorUserLog.getFirstLayerTaskId() + " to the schema.");
                Log.d(TAG, "Inserted userPic: " + errorUserLog.getLevelTaskPackId() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorUserLog;
    }

    //userKey wise delete errorUserLog
    @Override
    public boolean deleteErrorUserLogByUserKey(Long key) {
        try {
            openWritableDb();
            ErrorUserLogDao errorUserLogDao = daoSession.getErrorUserLogDao();
            QueryBuilder<ErrorUserLog> queryBuilder = errorUserLogDao.queryBuilder().where(ErrorUserLogDao.Properties.UserId.eq(key));
            List<ErrorUserLog> errorUserLogToDelete = queryBuilder.list();
            for (ErrorUserLog errorUserLog : errorUserLogToDelete) {
                errorUserLogDao.delete(errorUserLog);
            }
            daoSession.clear();
            Log.d(TAG, errorUserLogToDelete.size() + " entry. " + "Deleted ErrorUserLogBy Key: " + key + " from the schema.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    //userKey wise update errorUserLog
    @Override
    public Long updateUserErrorLog(ErrorUserLog UserLog) {
        Long userErrorId = null;
        try {
            if (UserLog != null) {
                openWritableDb();
                daoSession.update(UserLog);
                Log.d(TAG, "Updated user: " + UserLog.getFirstLayerTaskId() + " from the schema.");
                userErrorId = UserLog.getUserId();
                daoSession.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return userErrorId;
    }

    // userWise ErrorLog List
    @Override
    public synchronized ArrayList<ErrorUserLog> listErrorUserList() {
        List<ErrorUserLog> errorUser = null;
        try {
            openReadableDb();
            ErrorUserLogDao errorUserLogDao = daoSession.getErrorUserLogDao();
            errorUser = errorUserLogDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (errorUser != null) {
            return new ArrayList<>(errorUser);
        }
        return null;
    }

    //User Wise ErrorLog List
    @Override
    public synchronized ArrayList<ErrorUserLog> errorLogUserWiseList(Long userKey) {
        List<ErrorUserLog> errorUser = null;
        try {
            openWritableDb();
            ErrorUserLogDao errorUserLogDao = daoSession.getErrorUserLogDao();
            QueryBuilder<ErrorUserLog> queryBuilder = errorUserLogDao.queryBuilder().where(ErrorUserLogDao.Properties.UserId.eq(userKey));
            errorUser = queryBuilder.list();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (errorUser != null) {
            return new ArrayList<>(errorUser);
        }
        return null;
    }

    //User,FirstLayerID,Level Wise ErrorLog list
    @Override
    public ArrayList<ErrorUserLog> errorLogByKeyFIdAndLevel(Long key, int fLayer, int level) {
        List<ErrorUserLog> errorLogLevel = null;
        try {
            openWritableDb();
            ErrorUserLogDao errorUserLogDao = daoSession.getErrorUserLogDao();
            QueryBuilder<ErrorUserLog> queryBuilder = errorUserLogDao.queryBuilder().where(ErrorUserLogDao.Properties.UserId.eq(key), ErrorUserLogDao.Properties.FirstLayerTaskId.eq(fLayer), ErrorUserLogDao.Properties.LevelTaskPackId.eq(level));
            errorLogLevel = queryBuilder.list();
            daoSession.clear();
        } catch (Exception e) {

            e.printStackTrace();
        }
        if (errorLogLevel != null) {
            return new ArrayList<>(errorLogLevel);
        }
        return null;
    }

    @Override
    public boolean deleteErrorUserLogByUserKey(Long key, int fLayer, int level) {
        boolean isRemoved = false;
        try {
            openWritableDb();
            ErrorUserLogDao errorUserLogDao = daoSession.getErrorUserLogDao();
            QueryBuilder<ErrorUserLog> queryBuilder = errorUserLogDao.queryBuilder().where(ErrorUserLogDao.Properties.UserId.eq(key), ErrorUserLogDao.Properties.FirstLayerTaskId.eq(fLayer), ErrorUserLogDao.Properties.LevelTaskPackId.eq(level));
            List<ErrorUserLog> errorUserLogToUpdate = queryBuilder.list();
            if (errorUserLogToUpdate.size() > 0) {
                for (ErrorUserLog errorUserLog : errorUserLogToUpdate) {
                    errorUserLogDao.delete(errorUserLog);
                }

                daoSession.clear();
                isRemoved = true;
                Log.d(TAG, errorUserLogToUpdate.size() + " entry. " + "update ErrorUserLogBy Key: " + key + " from the schema.");

            }
        } catch (Exception e) {
            isRemoved = false;
            e.printStackTrace();
        }
        return isRemoved;

    }

    @Override
    public int listSizeErrorUserLog(Long key, int fLayer, int level) {
        int errorLogSize = -1;
        try {
            openWritableDb();
            ErrorUserLogDao errorUserLogDao = daoSession.getErrorUserLogDao();
            QueryBuilder<ErrorUserLog> queryBuilder = errorUserLogDao.queryBuilder().where(ErrorUserLogDao.Properties.UserId.eq(key), ErrorUserLogDao.Properties.FirstLayerTaskId.eq(fLayer), ErrorUserLogDao.Properties.LevelTaskPackId.eq(level));
            List<ErrorUserLog> errorUserLogList = queryBuilder.list();
            errorLogSize = errorUserLogList.size();
            daoSession.clear();
            Log.d(TAG, errorUserLogList.size() + "Error Size");

        } catch (Exception e) {
            e.printStackTrace();

        }
        return errorLogSize;
    }
    /***************************************   END ErrorUserLog Table Operation  *****************************/
    /***************************************
     * Star Table Operation
     ****************************************/
    //insertion Star Table
    @Override
    public synchronized Star insertStar(Star levelStar) {
        try {
            if (levelStar != null) {
                openWritableDb();
                StarDao levelStarDao = daoSession.getStarDao();
                levelStarDao.insert(levelStar);
                Log.d(TAG, "Inserted FLayerID: " + levelStar.getFirstLayerTaskId() + " to the schema.");
                Log.d(TAG, "Inserted level: " + levelStar.getLevelTaskPackId() + " to the schema.");
                Log.d(TAG, "Inserted Star: " + levelStar.getLevelStar() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return levelStar;
    }

    //userKey,fLayerID,Level wise Delete Star
    @Override
    public boolean deleteLevelWiseStar(Long key, int fLayer, int level) {
        boolean isRemoved = false;
        try {
            openWritableDb();
            StarDao levelStarDao = daoSession.getStarDao();
            QueryBuilder<Star> queryBuilder = levelStarDao.queryBuilder().where(StarDao.Properties.UserId.eq(key), StarDao.Properties.FirstLayerTaskId.eq(fLayer), StarDao.Properties.LevelTaskPackId.eq(level));
            List<Star> levelStarList = queryBuilder.list();
            if (levelStarList.size() > 0) {
                for (Star levelStar : levelStarList) {
                    levelStarDao.delete(levelStar);
                }

                daoSession.clear();
                isRemoved = true;
                Log.d(TAG, levelStarList.size() + " entry. ");

            }
        } catch (Exception e) {
            isRemoved = false;
            e.printStackTrace();
        }
        return isRemoved;
    }

    @Override
    public int getLevelWiseStar(Long key, int fLayer, int level) {
        int levelWiseStar = 0;
        try {
            openWritableDb();
            StarDao levelStarDao = daoSession.getStarDao();
            QueryBuilder<Star> queryBuilder = levelStarDao.queryBuilder().where(StarDao.Properties.UserId.eq(key), StarDao.Properties.FirstLayerTaskId.eq(fLayer), StarDao.Properties.LevelTaskPackId.eq(level));
            List<Star> levelStarList = queryBuilder.list();
            if (levelStarList.size() > 0) {
                for (Star levelStar : levelStarList) {
                    levelWiseStar = levelStar.getLevelStar();
                }
                daoSession.clear();
                Log.d(TAG, levelStarList.size() + " entry. ");
                Log.d(TAG, levelWiseStar + " Level Star. ");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return levelWiseStar;
    }

    @Override
    public int getLevelWisePoint(Long key, int fLayer, int level) {
        int levelWisePoint = 0;
        try {
            openWritableDb();
            StarDao levelStarDao = daoSession.getStarDao();
            QueryBuilder<Star> queryBuilder = levelStarDao.queryBuilder().where(StarDao.Properties.UserId.eq(key), StarDao.Properties.FirstLayerTaskId.eq(fLayer), StarDao.Properties.LevelTaskPackId.eq(level));
            List<Star> levelStarList = queryBuilder.list();
            if (levelStarList.size() > 0) {
                for (Star levelStar : levelStarList) {
                    levelWisePoint = levelStar.getLevelPoint();
                }
                daoSession.clear();
                Log.d(TAG, levelStarList.size() + " entry. ");
                Log.d(TAG, levelWisePoint + " Level Point. ");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return levelWisePoint;
    }

    @Override
    public ArrayList<Integer> getLevelWisePoint(Long key, int fLayer) {
        ArrayList<Integer> arr = new ArrayList<>();
        int levelWisePoint = 0;
        try {
            openWritableDb();
            StarDao levelStarDao = daoSession.getStarDao();
            QueryBuilder<Star> queryBuilder = levelStarDao.queryBuilder().where(StarDao.Properties.UserId.eq(key), StarDao.Properties.FirstLayerTaskId.eq(fLayer)).orderAsc(StarDao.Properties.LevelTaskPackId);
            List<Star> levelStarList = queryBuilder.list();

            for (int i = 0; i < levelStarList.size(); i++) {
                levelWisePoint = levelStarList.get(i).getLevelPoint();
                arr.add(levelWisePoint);
            }
            /*if (levelStarList.size() > 0) {
                for (Star levelStar : levelStarList) {
                    levelWisePoint = levelStar.getLevelPoint();
                }*/
            daoSession.clear();
            Log.d(TAG, levelStarList.size() + " entry. ");
            Log.d(TAG, levelWisePoint + " Level Star. ");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    // user getTotal Star
    @Override
    public int getTotalStar(Long key) {
        int levelWiseStar = 0;
        try {
            openWritableDb();
            StarDao levelStarDao = daoSession.getStarDao();
            QueryBuilder<Star> queryBuilder = levelStarDao.queryBuilder().where(StarDao.Properties.UserId.eq(key));
            List<Star> levelStarList = queryBuilder.list();
            if (levelStarList.size() > 0) {
                for (Star levelStar : levelStarList) {
                    int star = 0;
                    star = levelStar.getLevelStar();
                    levelWiseStar = levelWiseStar + star;
                }
                daoSession.clear();
                Log.d(TAG, levelStarList.size() + " entry. ");
                Log.d(TAG, levelWiseStar + " Total Star. ");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return levelWiseStar;
    }

    // user Key wise Star Information
    @Override
    public ArrayList<Star> getStarListUserKey(Long key) {
        List<Star> starDeleteAry = null;
        try {
            openWritableDb();
            StarDao starDao = daoSession.getStarDao();
            QueryBuilder<Star> queryBuilder = starDao.queryBuilder().where(StarDao.Properties.UserId.eq(key)).orderAsc(StarDao.Properties.FirstLayerTaskId);
            starDeleteAry = queryBuilder.list();
            daoSession.clear();
            Log.d(TAG, starDeleteAry.size() + " entry. " + "Delete Star Log: " + key + " from the schema.");
        } catch (Exception e) {

            e.printStackTrace();
        }
        if (starDeleteAry != null) {
            return new ArrayList<>(starDeleteAry);
        }
        return null;
    }

    @Override
    public ArrayList<Star> getLayerLevelWiseStar(Long key, int fLayer) {
//        ArrayList<Star> arr = new ArrayList<>();
//        int levelWiseStar = 0;
        try {
            openWritableDb();
            StarDao levelStarDao = daoSession.getStarDao();
            QueryBuilder<Star> queryBuilder = levelStarDao.queryBuilder().where(StarDao.Properties.UserId.eq(key), StarDao.Properties.FirstLayerTaskId.eq(fLayer)).orderAsc(StarDao.Properties.LevelTaskPackId);
            List<Star> levelStarList = queryBuilder.list();
            daoSession.clear();
            Log.d(TAG, levelStarList.size() + " entry. ");
            if (levelStarList != null) {
                return new ArrayList<>(levelStarList);
            }
           /* for (int i = 0; i < levelStarList.size(); i++) {



            }*/
            /*if (levelStarList.size() > 0) {
                for (Star levelStar : levelStarList) {
                    levelWisePoint = levelStar.getLevelPoint();
                }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Star> getStarListFlayerWise(Long key, int fLayer) {
        List<Star> starDeleteAry = null;
        try {
            openWritableDb();
            StarDao starDao = daoSession.getStarDao();
            QueryBuilder<Star> queryBuilder = starDao.queryBuilder().where(StarDao.Properties.UserId.eq(key), StarDao.Properties.FirstLayerTaskId.eq(fLayer)).orderAsc(StarDao.Properties.LevelTaskPackId);
            starDeleteAry = queryBuilder.list();
            daoSession.clear();
            Log.d(TAG, starDeleteAry.size() + " entry. " + "Delete Star Log: " + key + " from the schema.");
        } catch (Exception e) {

            e.printStackTrace();
        }
        if (starDeleteAry != null) {
            return new ArrayList<>(starDeleteAry);
        }
        return null;
    }

    @Override
    public boolean deleteStarUserKey(Long key) {
        boolean isRemoved = false;
        try {
            openWritableDb();
            StarDao starDao = daoSession.getStarDao();
            QueryBuilder<Star> queryBuilder = starDao.queryBuilder().where(StarDao.Properties.UserId.eq(key));
            List<Star> starDeleteAry = queryBuilder.list();
            if (starDeleteAry.size() > 0) {
                for (Star star : starDeleteAry) {
                    starDao.delete(star);
                }

                daoSession.clear();
                isRemoved = true;
                Log.d(TAG, starDeleteAry.size() + " entry. " + "Delete Star Log: " + key + " from the schema.");

            }
        } catch (Exception e) {
            isRemoved = false;
            e.printStackTrace();
        }
        return isRemoved;

    }
    /***************************************    END Star Table Operation  *************************************/

    /***************************************
     * First Layer Image Table Operation
     ************************************/
    // insert FirstLayer TaskImages
    @Override
    public synchronized FirstLayerTaskImage insertFLayerTaskImage(FirstLayerTaskImage firstLayerTaskImage) {
        try {
            if (firstLayerTaskImage != null) {
                openWritableDb();
                FirstLayerTaskImageDao fLayerDao = daoSession.getFirstLayerTaskImageDao();
                fLayerDao.insert(firstLayerTaskImage);

                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstLayerTaskImage;
    }

    @Override
    public ArrayList<FirstLayerTaskImage> getFirstLayerImageList(int fLayerID) {

        List<FirstLayerTaskImage> listTaskImage = null;
        try {
            openWritableDb();
            FirstLayerTaskImageDao fLayerImageDao = daoSession.getFirstLayerTaskImageDao();
            QueryBuilder<FirstLayerTaskImage> queryBuilder = fLayerImageDao.queryBuilder().where(FirstLayerTaskImageDao.Properties.FirstLayerTaskId.eq(fLayerID));
            listTaskImage = queryBuilder.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listTaskImage != null) {
            return new ArrayList<>(listTaskImage);
        }

        return null;
    }

    /******************************************
     * Time Statistics Table
     *******************************************/
    @Override
    public long getTotalTimeLayerWise(Long key, int fLayerID) {
        long totalTimeLayer = 0;
        try {
            openWritableDb();
            TimeStatisticsDao timeStatisticDao = daoSession.getTimeStatisticsDao();
            QueryBuilder<TimeStatistics> queryBuilder = timeStatisticDao.queryBuilder().where(TimeStatisticsDao.Properties.UserKey.eq(key), TimeStatisticsDao.Properties.FirstLayerTaskId.eq(fLayerID));
            List<TimeStatistics> timeStatisticsArray = queryBuilder.list();
            if (timeStatisticsArray.size() > 0) {
                for (TimeStatistics timeStatistic : timeStatisticsArray) {
                    long time = 0;
                    time = timeStatistic.getTime();
                    totalTimeLayer = totalTimeLayer + time;
                }
                daoSession.clear();
                Log.d(TAG, timeStatisticsArray.size() + " entry. ");
                Log.d(TAG, totalTimeLayer + " Total Time. ");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalTimeLayer;
    }

    @Override
    public synchronized TimeStatistics insertTime(TimeStatistics timeStatistics) {
        try {
            if (timeStatistics != null) {
                openWritableDb();
                TimeStatisticsDao timeStatisticsDao = daoSession.getTimeStatisticsDao();
                timeStatisticsDao.insert(timeStatistics);
                Log.d(TAG, "Inserted FLayerID: " + timeStatistics.getFirstLayerTaskId() + " to the schema.");
                Log.d(TAG, "Inserted level: " + timeStatistics.getLevel() + " to the schema.");
                Log.d(TAG, "Inserted Star: " + timeStatistics.getTime() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeStatistics;
    }

    //userKey,fLayerID,Level wise Delete Star
    @Override
    public boolean deleteTime(Long key, int fLayer, int level) {
        boolean isRemoved = false;
        try {
            openWritableDb();
            TimeStatisticsDao timeStatisticsDao = daoSession.getTimeStatisticsDao();
            QueryBuilder<TimeStatistics> queryBuilder = timeStatisticsDao.queryBuilder().where(TimeStatisticsDao.Properties.UserKey.eq(key), TimeStatisticsDao.Properties.FirstLayerTaskId.eq(fLayer), TimeStatisticsDao.Properties.Level.eq(level));
            List<TimeStatistics> timeStatisticsArray = queryBuilder.list();
            if (timeStatisticsArray.size() > 0) {
                for (TimeStatistics timeStatic : timeStatisticsArray) {
                    timeStatisticsDao.delete(timeStatic);
                }

                daoSession.clear();
                isRemoved = true;
                Log.d(TAG, timeStatisticsArray.size() + " entry. ");

            }
        } catch (Exception e) {
            isRemoved = false;
            e.printStackTrace();
        }
        return isRemoved;
    }

    @Override
    public long getLevelWiseTime(Long key, int fLayer, int level) {
        long levelWiseTime = 0;
        try {
            openWritableDb();
            TimeStatisticsDao timeStatisticsDao = daoSession.getTimeStatisticsDao();
            QueryBuilder<TimeStatistics> queryBuilder = timeStatisticsDao.queryBuilder().where(TimeStatisticsDao.Properties.UserKey.eq(key), TimeStatisticsDao.Properties.FirstLayerTaskId.eq(fLayer), TimeStatisticsDao.Properties.Level.eq(level));
            List<TimeStatistics> levelTimeList = queryBuilder.list();
            if (levelTimeList.size() > 0) {
                for (TimeStatistics levelTime : levelTimeList) {
                    levelWiseTime = levelTime.getTime();
                }
                daoSession.clear();
                Log.d(TAG, levelTimeList.size() + " entry. ");
                Log.d(TAG, levelTimeList + " Level Star. ");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return levelWiseTime;
    }

    @Override
    public boolean deleteUserTime(Long key) {
        boolean isRemoved = false;
        try {
            openWritableDb();
            TimeStatisticsDao timeStatisticsDao = daoSession.getTimeStatisticsDao();
            QueryBuilder<TimeStatistics> queryBuilder = timeStatisticsDao.queryBuilder().where(TimeStatisticsDao.Properties.UserKey.eq(key));
            List<TimeStatistics> timeStatisticsArray = queryBuilder.list();
            if (timeStatisticsArray.size() > 0) {
                for (TimeStatistics timeStatic : timeStatisticsArray) {
                    timeStatisticsDao.delete(timeStatic);
                }

                daoSession.clear();
                isRemoved = true;
                Log.d(TAG, timeStatisticsArray.size() + " entry. ");

            }
        } catch (Exception e) {
            isRemoved = false;
            e.printStackTrace();
        }
        return isRemoved;
    }
    @Override
    public boolean deleteImageList(int fLayerID) {
        boolean isRemoved = false;
        try {
            openWritableDb();
            FirstLayerTaskImageDao fLayerImageDao = daoSession.getFirstLayerTaskImageDao();
            QueryBuilder<FirstLayerTaskImage> queryBuilder = fLayerImageDao.queryBuilder().where(FirstLayerTaskImageDao.Properties.FirstLayerTaskId.eq(fLayerID));
            List<FirstLayerTaskImage> fLayerImageArray = queryBuilder.list();
            if (fLayerImageArray.size() > 0) {
                for (FirstLayerTaskImage fLayerImage : fLayerImageArray) {
                    fLayerImageDao.delete(fLayerImage);
                }
                isRemoved = true;
                daoSession.clear();
                Log.d(TAG, fLayerImageArray.size() + " entry. ");
            }

        } catch (Exception e) {
            isRemoved = false;
            e.printStackTrace();
        }
        return isRemoved;
    }


    /***************************************  Gift Box Table *******************************************/

    @Override
    public long hasUserForGiftBox(Long userKey) {
        long giftBoxId=0;
        List<GiftTbl> allDailyGrids = null;
        try {
            openReadableDb();
            GiftTblDao giftTblDao = daoSession.getGiftTblDao();
            QueryBuilder<GiftTbl> queryBuilder = giftTblDao.queryBuilder().where(GiftTblDao.Properties.UserKey .eq(userKey));
            allDailyGrids = queryBuilder.list();
            for (GiftTbl giftTbl : allDailyGrids) {
                giftBoxId = giftTbl.getId();
                break;
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return giftBoxId;
    }
    @Override
    public synchronized GiftTbl insertGiftTbl(GiftTbl giftTbl){
        try {
            if (giftTbl != null) {
                openWritableDb();
                GiftTblDao dailyTblDao = daoSession.getGiftTblDao();
                dailyTblDao.insert(giftTbl);
                Log.d(TAG, "Inserted DailyTbl: " + giftTbl.getUserId() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return giftTbl;
    }
    @Override
    public synchronized void  updateGiftTbl(GiftTbl giftTbl) {
        try {
            if (giftTbl != null) {
                openWritableDb();
                daoSession.update(giftTbl);
                Log.d("TAG", "Updated DailyTbl : " + giftTbl.getUserId()+ " from the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public synchronized GiftTbl getGiftBoxByUserId(Long userKey) {
        GiftTbl giftTbl=null;
        List<GiftTbl> getGiftTbls = null;
        try {
            openReadableDb();
            GiftTblDao giftTblDao = daoSession.getGiftTblDao();
            QueryBuilder queryBuilder = giftTblDao.queryBuilder().where(GiftTblDao.Properties.UserKey.eq(userKey));
            getGiftTbls = queryBuilder.list();
            for (GiftTbl giftBoxTbl : getGiftTbls) {
                giftTbl=giftBoxTbl;
                break;
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return giftTbl;
    }
}
