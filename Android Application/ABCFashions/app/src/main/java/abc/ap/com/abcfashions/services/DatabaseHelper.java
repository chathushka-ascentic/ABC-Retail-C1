package abc.ap.com.abcfashions.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


class DatabaseHelper extends SQLiteOpenHelper {

    private static final String databaseName = "abc.sqlite";
    private static String databasePath = "";
    private static final int databaseVersion = 1 ; //increase version when there is a change in the db
    private final Context context;
    private SQLiteDatabase dataBase=null;

    public static DatabaseHelper databaseHelper;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }


    DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
        databasePath = context.getDatabasePath(databaseName).getPath();
    }


    private void createDataBase() throws IOException
    {
        // If database not exists copy it from the assets
        boolean mDataBaseExist = checkDataBase();

        if (!mDataBaseExist)
        {
            this.getWritableDatabase();
            try {
                copyDataBase();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
        else
        {
            this.getWritableDatabase();
        }

    }

    private void copyDataBase() throws IOException
    {
        InputStream mInput = context.getAssets().open(databaseName);
        String outFileName = databasePath;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }

        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    private boolean checkDataBase() {
        File dbFile = new File(databasePath);
        return dbFile.exists();
    }

    // Open the database, so we can query it
    private boolean openDataBase() throws SQLException, IOException {
        this.createDataBase();

        dataBase = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
        dataBase.setVersion(databaseVersion);


        return dataBase != null;
    }

    public Cursor ExecuteSql(String sql) throws Exception {
            if (dataBase == null || !dataBase.isOpen())
                openDataBase();

            return this.dataBase.rawQuery(sql, null);

    }

    void ExecuteSqlWrite(String sql) throws Exception{
        if (dataBase == null || !dataBase.isOpen())
            openDataBase();

        this.dataBase.execSQL(sql);
    }


    public long Insert(String tableName, ContentValues contentValues) throws Exception {
            if (dataBase == null || !dataBase.isOpen())
                openDataBase();


            return this.dataBase.insert(tableName, null, contentValues);
    }

    public int Delete(String tableName) throws Exception {

            if (dataBase == null || !dataBase.isOpen())
                openDataBase();

            return this.dataBase.delete(tableName, null, null);

    }

    public int Delete(String tableName, String whereClause) throws Exception {

            if (dataBase == null || !dataBase.isOpen())
                openDataBase();

            return this.dataBase.delete(tableName, whereClause, null);
    }

    public int Delete(String tableName, String whereClause, String[] whereArgs) throws Exception {
            if (dataBase == null || !dataBase.isOpen())
                openDataBase();

            return this.dataBase.delete(tableName, whereClause, whereArgs);
    }

    public int Update(String tableName, ContentValues values, String whereClause) throws Exception {
            if (dataBase == null || !dataBase.isOpen())
                openDataBase();

            return this.dataBase.update(tableName, values, whereClause, null);

    }

    public int Update(String tableName, ContentValues values, String whereClause, String[] whereArgs) throws Exception {
            if (dataBase == null || !dataBase.isOpen())
                openDataBase();

            return this.dataBase.update(tableName, values, whereClause, whereArgs);
    }

    public long insertOrUpdate(String tableName, ContentValues values) throws Exception {
            if (dataBase == null || !dataBase.isOpen())
                openDataBase();

            return this.dataBase.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public void BeginTransaction() throws Exception {
            if (dataBase == null || !dataBase.isOpen())
                openDataBase();

            this.dataBase.beginTransaction();
    }

    public void CommitTransaction() throws Exception {
            this.dataBase.setTransactionSuccessful();
            this.dataBase.endTransaction();
    }

    public void RollBackTransaction() {
        try {
            this.dataBase.endTransaction();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.i("Database Created....", "Database Created....");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion != newVersion)
        {
                // Remove db and create new ones
                if(context.deleteDatabase(databaseName))
                {
                    try
                    {
                        copyDataBase();
                    } catch (IOException mIOException) {
                        throw new Error("ErrorCopyingDataBase");
                    }
                }

        }

    }

}