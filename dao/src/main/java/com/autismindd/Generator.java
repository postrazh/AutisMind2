package com.autismindd;


//1.To built the "Green Dao" library it need a custom builder.
//To create a library builder try
// -> Edit configuration from "Setup Run/Debug Configuration"
// -> "+"
// -> Application
// -> Put a name ex. "Dao"
// -> Mainclass - "Generator"
// -> Use classpath of Module
// -> "dao"
// -> Save changes.

//2.To add a new table, use "addTables" method.

//3.To add a new column just "addDateProperty" in the following table and build the dao library. It will automatically
// generate the model class for the table.


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Generator {
    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");
    private static final String OUT_DIR = PROJECT_DIR + "/app/src/main/java";

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.autismindd.dao");
        addTables(schema);
        new DaoGenerator().generateAll(schema, OUT_DIR);
    }

    /**
     * Create tables and the relationships between them
     */
    private static void addTables(Schema schema) {
        /* entities */
        Entity user = addUser(schema);
        Entity taskPack = addTaskPack(schema);
        Entity task = addTask(schema, user);
        Entity item = addItem(schema, task);
        Entity result = addResult(schema, task, item);
        Entity firstLayer=addFirstLayerTask(schema);
        Entity level=addLevel(schema,user,taskPack,task);
        Entity errorUserLog=addUserLog(schema);
        Entity levelStar=addStar(schema);
        Entity addFirstLayerTaskImage=addFirstLayerTaskImage(schema);
        Entity timeStatics=addTimeStatistics(schema);
        Entity giftTbl=addGiftTbl(schema);
    }

    /**
     * Create user's Properties
     *
     * @return DBUser entity
     */
    private static Entity addUser(Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIdProperty().primaryKey().autoincrement();
        user.addStringProperty("name").notNull();
        user.addStringProperty("password").notNull();
        user.addIntProperty("avatar").notNull();
        user.addStringProperty("pic").notNull();
        user.addLongProperty("key").notNull();
        user.addBooleanProperty("userState").notNull();
        user.addIntProperty("stars").notNull();
        user.addIntProperty("firstLayerTaskID").notNull();
        user.addIntProperty("lastLevelID").notNull();
        user.addIntProperty("point").notNull();
        user.addBooleanProperty("active").notNull();
        user.addIntProperty("helper").notNull();
        user.addIntProperty("music").notNull();
        user.addIntProperty("taskPlayProgress").notNull();
        user.addIntProperty("soundEffect").notNull();
        user.addDateProperty("createdAt").notNull();
        user.addDateProperty("updatedAt").notNull();
        return user;
    }

    /**
     * Create task Properties
     *
     * @return Task entity
     */
    private static Entity addTask(Schema schema, Entity user) {

        Entity task = schema.addEntity("Task");
        task.addLongProperty("id").primaryKey();
        task.addLongProperty("uniqId").notNull();
        task.addStringProperty("name").notNull();
        task.addStringProperty("taskImage").notNull();
        task.addStringProperty("type").notNull();
        task.addIntProperty("backgroundColor").notNull();
        task.addIntProperty("slideSequence").notNull();
        task.addBooleanProperty("active");
        task.addDateProperty("createdAt");
        task.addDateProperty("updatedAt");
        task.addLongProperty("taskPackId");
        task.addStringProperty("feedbackImage").notNull();
        task.addIntProperty("feedbackAnimation").notNull();
        task.addIntProperty("positiveAnimation").notNull();
        task.addIntProperty("negativeAnimation").notNull();
        task.addStringProperty("feedbackSound").notNull();
        task.addStringProperty("positiveSound").notNull();
        task.addStringProperty("negativeSound").notNull();
        task.addIntProperty("feedbackType").notNull();

        task.addIntProperty("errorBgColor").notNull();
        task.addStringProperty("errorImage").notNull();
        task.addStringProperty("errortext").notNull();
        task.addIntProperty("errorMandatoryScreen").notNull();

        task.addIntProperty("tutorial").notNull();
        task.addIntProperty("transition").notNull();

        Property userIdProperty = task.addLongProperty("userId").notNull().getProperty();
        ToMany userToTask = user.addToMany(task, userIdProperty);
        userToTask.setName("tasks");

        return task;
    }

    /**
     * Create item Properties
     *
     * @return Item entity
     */
    private static Entity addItem(Schema schema, Entity task) {
        Entity item = schema.addEntity("Item");
        item.addIdProperty().primaryKey().autoincrement();
        item.addFloatProperty("x");
        item.addFloatProperty("y");
        item.addIntProperty("rotation");
        item.addLongProperty("key");
        item.addIntProperty("isCircleView");
        item.addIntProperty("circleColor");
        item.addStringProperty("userText").notNull();;
        item.addIntProperty("textColor");
        item.addIntProperty("textSize");
        item.addIntProperty("borderColor");
        item.addIntProperty("backgroundColor");
        item.addIntProperty("drawable");
        item.addFloatProperty("width");
        item.addFloatProperty("height");
        item.addFloatProperty("left");
        item.addFloatProperty("right");
        item.addFloatProperty("top");
        item.addFloatProperty("bottom");
//        item.addIntProperty("helper").notNull();
        item.addStringProperty("imagePath").notNull();
        item.addStringProperty("type").notNull();
        item.addDateProperty("createdAt");
        item.addDateProperty("updatedAt");
        item.addLongProperty("task");
        item.addStringProperty("itemSound").notNull(); //reaz added
        item.addStringProperty("result").notNull();
        item.addStringProperty("openApp").notNull();
        item.addStringProperty("openUrl").notNull();
        item.addIntProperty("allowDragDrop").notNull();
        item.addLongProperty("dragDropTarget").notNull();
        item.addIntProperty("cornerRound").notNull();
        item.addLongProperty("navigateTo").notNull();
        item.addLongProperty("showedBy").notNull();
        item.addLongProperty("hideBy").notNull();
        item.addIntProperty("closeApp").notNull();
        //item.addIntProperty("allowAlign").notNull();
        //item.addLongProperty("alignTarget").notNull();
        //item.addIntProperty("alignType").notNull();
        item.addIntProperty("fontTypeFace").notNull(); //add by reaz
        item.addIntProperty("fontAlign").notNull(); //add by reaz
        item.addIntProperty("autoPlay").notNull(); //add by reaz
        item.addIntProperty("soundDelay").notNull(); //add by reaz
        item.addIntProperty("borderPixel").notNull(); //add by reaz
        item.addStringProperty("showedByTarget").notNull();
        item.addStringProperty("hiddenByTarget").notNull();
        item.addIntProperty("helper").notNull();
        item.addIntProperty("tutorialAnimation").notNull();
        item.addIntProperty("tutorialX").notNull();
        item.addIntProperty("tutorialY").notNull();
        item.addLongProperty("tutorialTag").notNull();

        Property taskIdProperty = item.addLongProperty("taskId").notNull().getProperty();
        ToMany taskToItem = task.addToMany(item, taskIdProperty);
        taskToItem.setName("items");
        return item;
    }

    /**
     * Create result Properties
     *
     * @return Result entity
     */
    private static Entity addResult(Schema schema, Entity task, Entity item) {
        Entity result = schema.addEntity("Result");
        result.addIdProperty().primaryKey().autoincrement();
        result.addLongProperty("key");
        result.addBooleanProperty("active");
        result.addDateProperty("createdAt");
        result.addDateProperty("updatedAt");

        Property taskIdProperty = result.addLongProperty("taskId").notNull().getProperty();
        ToMany taskToResult = task.addToMany(result, taskIdProperty);
        taskToResult.setName("taskResults");

        Property itemIdProperty = result.addLongProperty("itemId").notNull().getProperty();
        ToMany itemToResult = item.addToMany(result, itemIdProperty);
        itemToResult.setName("results");

        return result;
    }

    /**
     * Create Task Pack Properties
     *
     * @return Result entity
     */
    private static Entity addTaskPack(Schema schema) {
        Entity taskPack = schema.addEntity("TaskPack");
        taskPack.addIdProperty().primaryKey().autoincrement();
        taskPack.addStringProperty("name");
        taskPack.addIntProperty("level").notNull();// only for use literacyall App
        taskPack.addIntProperty("firstLayerTaskID").notNull();// only for use literacyall App
        taskPack.addIntProperty("touchAnimation").notNull(); //its a touch mode animation
        taskPack.addIntProperty("itemOfAnimation").notNull();
        taskPack.addDateProperty("createdAt");

        return taskPack;
    }

    /**
     * Create Task Pack Properties
     *
     * @return FirstLayer entity
     */
    private static Entity addFirstLayerTask(Schema schema) {
        Entity taskPack = schema.addEntity("FirstLayer");
        taskPack.addIdProperty().primaryKey().autoincrement();
        taskPack.addStringProperty("name").notNull();
        taskPack.addStringProperty("fileName").notNull();;
        taskPack.addStringProperty("imgUrl").notNull(); // only for use literacyall App
        taskPack.addIntProperty("firstLayerTaskID").notNull();// only for use literacyall App
        taskPack.addBooleanProperty("locked").notNull();
        taskPack.addBooleanProperty("state").notNull();
        taskPack.addDateProperty("createdAt");

        return taskPack;
    }

    private static Entity addLevel(Schema schema, Entity user, Entity taskPack, Entity task) {
        Entity level = schema.addEntity("Level");
        level.addIdProperty().primaryKey().autoincrement();
        level.addLongProperty("key");
        level.addIntProperty("levelNumber");
        level.addIntProperty("errorCount");
        level.addIntProperty("efficient");
        level.addDateProperty("createdAt");
        level.addDateProperty("updatedAt");


        Property userIdProperty = level.addLongProperty("userId").notNull().getProperty();
        ToMany userToTask = user.addToMany(level, userIdProperty);


        Property taskPackIdProperty = level.addLongProperty("taskPackId").notNull().getProperty();
        ToMany taskPackToResult = taskPack.addToMany(level, taskPackIdProperty);

        Property taskIdProperty = level.addLongProperty("lastTaskId").notNull().getProperty();
        ToMany taskToResult = task.addToMany(level, taskIdProperty);


     /*   Property itemIdProperty = level.addLongProperty("itemId").notNull().getProperty();
        ToMany itemToResult = item.addToMany(level, itemIdProperty);
        itemToResult.setName("results");*/

        return level;
    }
    private static Entity addUserLog(Schema schema) {
        Entity errorUserLog = schema.addEntity("ErrorUserLog");
        errorUserLog.addIdProperty().primaryKey().autoincrement();
        errorUserLog.addLongProperty("userId").notNull();//userId=UserKey
        errorUserLog.addIntProperty("firstLayerTaskId").notNull();// only for use literacyall App
        errorUserLog.addIntProperty("levelTaskPackId").notNull();// only for use literacyall App
        errorUserLog.addLongProperty("errorTaskCount");


     /*   Property itemIdProperty = level.addLongProperty("itemId").notNull().getProperty();
        ToMany itemToResult = item.addToMany(level, itemIdProperty);
        itemToResult.setName("results");*/

        return errorUserLog;
    }

    private static Entity addStar(Schema schema) {
        Entity levelStar = schema.addEntity("Star");
        levelStar.addIdProperty().primaryKey().autoincrement();
        levelStar.addLongProperty("userId").notNull();//userId=UserKey
        levelStar.addIntProperty("firstLayerTaskId").notNull();// only for use literacyall App
        levelStar.addIntProperty("levelTaskPackId").notNull();// only for use literacyall App
        levelStar.addIntProperty("levelStar").notNull();
        levelStar.addIntProperty("levelPoint").notNull();
        levelStar.addLongProperty("levelRT").notNull();
        levelStar.addLongProperty("levelDT").notNull();
        levelStar.addLongProperty("levelErrorPoint").notNull();
     /*   Property itemIdProperty = level.addLongProperty("itemId").notNull().getProperty();
        ToMany itemToResult = item.addToMany(level, itemIdProperty);
        itemToResult.setName("results");*/

        return levelStar;
    }

    //firstLayerTask Images And Description
    private static Entity addFirstLayerTaskImage(Schema schema) {
        Entity firstLayerTaskImage = schema.addEntity("FirstLayerTaskImage");
        firstLayerTaskImage.addIdProperty().primaryKey().autoincrement();
        firstLayerTaskImage.addIntProperty("firstLayerTaskId").notNull();// only for use literacyall App
        firstLayerTaskImage.addStringProperty("firstLayerTaskImages").notNull();
        firstLayerTaskImage.addStringProperty("firstLayerTaskDes").notNull();

     /*   Property itemIdProperty = level.addLongProperty("itemId").notNull().getProperty();
        ToMany itemToResult = item.addToMany(level, itemIdProperty);
        itemToResult.setName("results");*/

        return firstLayerTaskImage;
    }
    //firstLayerTask Images And Description
    private static Entity addTimeStatistics(Schema schema) {
        Entity timeStatistics = schema.addEntity("TimeStatistics");
        timeStatistics.addIdProperty().primaryKey().autoincrement();
        timeStatistics.addLongProperty("userKey").notNull();// only for use literacyall App
        timeStatistics.addIntProperty("firstLayerTaskId").notNull();
        timeStatistics.addIntProperty("level").notNull();
        timeStatistics.addLongProperty("time").notNull();

        return timeStatistics;
    }

    //firstLayerTask Images And Description
    private static Entity addGiftTbl(Schema schema) {
        Entity giftTable = schema.addEntity("GiftTbl");
        giftTable.addIdProperty().primaryKey().autoincrement();
        giftTable.addLongProperty("userId").notNull();// only for use literacyall App
        giftTable.addLongProperty("userKey").notNull();// only for use literacyall App
        giftTable.addIntProperty("AppearPoint").notNull();
        giftTable.addIntProperty("AppearCount").notNull();
        return giftTable;
    }
}
