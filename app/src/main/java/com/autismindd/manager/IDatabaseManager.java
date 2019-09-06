package com.autismindd.manager;

import com.autismindd.dao.FirstLayerTaskImage;
import com.autismindd.dao.GiftTbl;
import com.autismindd.dao.Result;
import com.autismindd.dao.Task;
import com.autismindd.dao.ErrorUserLog;
import com.autismindd.dao.FirstLayer;
import com.autismindd.dao.Item;
import com.autismindd.dao.Star;
import com.autismindd.dao.TaskPack;
import com.autismindd.dao.TimeStatistics;
import com.autismindd.dao.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author Octa
 */
public interface IDatabaseManager {

    /**
     * Closing available connections
     */
    void closeDbConnections();

    /**
     * Delete all tables and content from our database
     */
    void dropDatabase();

    /**
     * Insert a user into the DB
     *
     * @param user to be inserted
     */
    User insertUser(User user);


    /**
     * List all the users from the DB
     *
     * @return list of users
     */
    ArrayList<User> listUsers();

    /**
     * Update a user from the DB
     *
     * @param user to be updated
     */
    Long updateUser(User user);

    User updateUsers(User user);

    /**
     * Delete all users with a certain email from the DB
     *
     * @param email of users to be deleted
     */
    void deleteUserByEmail(String email);

    /**
     * Delete a user with a certain id from the DB
     *
     * @param userId of users to be deleted
     */
    boolean deleteUserById(Long userId);

    /**
     * Delete a user with a certain id from the DB
     *
     * @param key of users to be deleted
     */
    boolean deleteUserByKey(Long key);

    /**
     * @param userId - of the user we want to fetch
     * @return Return a user by its id
     */
    User getUserById(Long userId);

    /**
     * Delete all the users from the DB
     */
    void deleteUsers();

    /**
     * Insert a user into the DB
     *
     * @param task to be inserted
     */
    Long insertTask(Task task);

    /**
     * List all the users from the DB
     *
     * @return list of users
     */
    List<Task> listTasks();

    /**
     * List all the users from the DB
     *
     * @return list of tasks according to Task Pack Id
     */
    List<Task> listTasksByTAskPackId(long taskPackId);

    /**
     * Update a user from the DB
     *
     * @param task to be updated
     */
    void updateTask(Task task);

    /**
     * Delete a user with a certain id from the DB
     *
     * @param TaskId of users to be deleted
     */
    boolean deleteTaskById(Long TaskId);

    /**
     * @param TaskId - of the user we want to fetch
     * @return Return a user by its id
     */
    Task getTaskById(Long TaskId);

    /**
     * Delete all the users from the DB
     */
    void deleteTasks();

    /**
     * List all the users task from the DB
     *
     * @return list of users
     */
    User userWiseTask();


    /**
     * Insert a user into the DB
     *
     * @param item to be inserted
     */
    long insertItem(Item item);

    /**
     * List all the users from the DB
     *
     * @return list of items
     */
    List<Item> listItems();

    /**
     * Update a user from the DB
     *
     * @param item to be updated
     */
    void updateItem(Item item);

    /**
     * Delete a user with a certain id from the DB
     *
     * @param itemId of users to be deleted
     */
    boolean deleteItemById(Long itemId);

    /**
     * @param itemId - of the user we want to fetch
     * @return Return a user by its id
     */
    Item getItemById(Long itemId);

    /**
     * Delete all the users from the DB
     */
    void deleteItems();

    /**
     * Insert a user into the DB
     *
     * @param result to be inserted
     */
    Result insertResult(Result result);

    /**
     * List all the results from the DB
     *
     * @return list of results
     */
    List<Result> listResults();

    /**
     * Update a result from the DB
     *
     * @param result to be updated
     */
    void updateResult(Result result);

    /**
     * Delete a user with a certain id from the DB
     *
     * @param resultId of users to be deleted
     */
    boolean deleteResultById(Long resultId);

    /**
     * @param resultId - of the user we want to fetch
     * @return Return a user by its id
     */
    Result getResultById(Long resultId);

    /**
     * Delete all the users from the DB
     */
    void deleteResults();

    /**
     * Checking correct answer
     */
    boolean getCorrectResult(Long key);

    /**
     * Load task wise item
     */
    LinkedHashMap<Long, Item> loadTaskWiseItem(Task task);

    /**
     * Insert a user into the DB
     *
     * @param taskPask to be inserted
     */
    Long insertTaskPack(TaskPack taskPask);

    /**
     * List all the users from the DB
     *
     * @return list of TaskPacks
     */
    List<TaskPack> listTaskPacks();

    /**
     * List all the users from the DB
     *
     * @return list of TaskPacks by firstLayerID and level
     */

    List<TaskPack> listTaskPacksLevel(int fLayerID, int level);

    /**
     * List all the users from the DB
     *
     * @return list of TaskPacks by level
     */
    List<TaskPack> listTaskPacksFirstLayerID(int firstLayer);

    /**
     * Update a user from the DB
     *
     * @param taskPack to be updated
     */
    void updateTaskPack(TaskPack taskPack);

    /**
     * Delete a user with a certain id from the DB
     *
     * @param taskPackId of taskPack to be deleted
     */
    boolean deleteTaskPackById(Long taskPackId);

    /**
     * @param taskPackId - of the user we want to fetch
     * @return Return a user by its id
     */
    TaskPack getTaskPackById(Long taskPackId);

    /**
     * Delete all the users from the DB
     */
    void deleteTaskPacks();

    /**
     * Delete tasks by TaskPackId
     */
    void deleteTasksByTaskPack(long taskPackId);

    /**
     * Get Task position to insert
     */
    int getMaxTaskPosition(long taskPackId);

    /**
     * Delete items by TaskId
     */
    void deleteItemsByTask(long taskId);


    /**
     * insert firstLayer table from Server
     *
     * @return insert FirstLayer
     */

    FirstLayer insertFirstLayer(FirstLayer firstLayer);

    /**
     * List all the users from the DB
     *
     * @return list of users
     */
    ArrayList<FirstLayer> listFirstLayer();

    /**
     * List all the users from the DB
     *
     * @return list of firstLayerv State wise
     */
    ArrayList<FirstLayer> getStatusTrueFirstLayerList(boolean status);

    /**
     * @param firstLayer to be updated
     */
    void updateFirstLayer(FirstLayer firstLayer);

    /**
     * Insert a ErrorUserLog into the DB
     *
     * @param errorUserLog to be inserted
     */
    ErrorUserLog insertErrorUserLogByUserKey(ErrorUserLog errorUserLog);

    /**
     * Delete userErrorLog by userKey
     */
    boolean deleteErrorUserLogByUserKey(Long userKey);

    /**
     * Update userErrorLog by userKey
     */
    Long updateUserErrorLog(ErrorUserLog errorUserLog);

    /**
     * list of userErrorLog by userKey
     */
    ArrayList<ErrorUserLog> listErrorUserList();

    /**
     * list of userErrorLog by userKey
     */
    ArrayList<ErrorUserLog> errorLogUserWiseList(Long userKey);

    /**
     * list of userErrorLog by userKey,FlayerID, Level
     */
    ArrayList<ErrorUserLog> errorLogByKeyFIdAndLevel(Long key, int fLayer, int level);

    /**
     * delete userErrorLog by userKey,FlayerID, Level
     */
    boolean deleteErrorUserLogByUserKey(Long key, int fLayer, int level);

    int listSizeErrorUserLog(Long key, int fLayer, int level);

    /**
     * Insert Level wise Star
     */
    Star insertStar(Star levelStar);

    /**
     * Delete Star each user and fLayerID,and Level Star log
     */
    boolean deleteLevelWiseStar(Long key, int fLayer, int level);

    // get user wise FirstLayerTaskId Star
    ArrayList<Star> getStarListFlayerWise(Long key, int fLayer);

    // get layer Wise star
    ArrayList<Star> getLayerLevelWiseStar(Long key, int fLayer);

    // get Level Wise star
    int getLevelWiseStar(Long key, int fLayer, int level);

    // get Level Wise Point
    int getLevelWisePoint(Long key, int fLayer, int level);

    // get Level Wise star
    ArrayList<Integer> getLevelWisePoint(Long key, int fLayer);

    // get Level Wise star
    int getTotalStar(Long key);

    // delete user wise star
    boolean deleteStarUserKey(Long key);


    // insert First Layer Task Images
    FirstLayerTaskImage insertFLayerTaskImage(FirstLayerTaskImage firstLayerTaskImage);

    // get FirstLayerTaskId Wise Image And Description
    ArrayList<Star> getStarListUserKey(Long key);

    // get First Layer Task Images list
    ArrayList<FirstLayerTaskImage> getFirstLayerImageList(int fLayer);

    boolean deleteImageList(int fLayer);

    /*****************************************   Time Statistics  *****************************************************/
    long getTotalTimeLayerWise(Long key, int fLayerID);

    TimeStatistics insertTime(TimeStatistics timeStatistics);

    boolean deleteTime(Long key, int fLayer, int level);

    long getLevelWiseTime(Long key, int fLayer, int level);

    boolean deleteUserTime(Long key);

    /****************************************   GIFT BOX ***********************************************************/
    /*
     * @param GiftTbl to be inserted
    */
    GiftTbl insertGiftTbl(GiftTbl giftTbl);

    /*
     * @param user to be inserted
    */
    void updateGiftTbl(GiftTbl giftTbl);

    /*
    * @param GiftBox by UserId
   */
    GiftTbl getGiftBoxByUserId(Long userKey);


    long hasUserForGiftBox(Long userKey);
}
