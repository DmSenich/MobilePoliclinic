package ru.pin120.policlinic

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import ru.pin120.policlinic.models.Specialty
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.sql.SQLException


class DatabaseHelper(private val mcontext:Context) : SQLiteOpenHelper(mcontext, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "policlinicMobileDataBase.db"
        private const val DB_VERSION = 1
        private var DB_PATH = ""
    }
    init {
        if (Build.VERSION.SDK_INT >= 17) {
            DB_PATH = mcontext.applicationInfo.dataDir + "/databases/"
        } else {
            DB_PATH = "/data/data/" + mcontext.packageName + "/databases/"
        }
        copyDataBase()
        this.readableDatabase
    }
    private var mDataBase: SQLiteDatabase? = null
    private var mNeedUpdate = false

    public fun updataDataBase(){
        if(mNeedUpdate){
            val dbFile = File(DB_PATH + DB_NAME)
            if(dbFile.exists())
                dbFile.delete()
            copyDataBase()
            mNeedUpdate = false;
        }
    }
    private fun checkDataBase(): Boolean {
        val dbFile = File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    @Throws(IOException::class)
    private fun copyDataBase(){
        if(!checkDataBase()){
            this.readableDatabase
            this.close()
            try{
                copyDBFile()
            }catch (mIOEx: IOException){
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    @Throws(IOException::class)
    private fun copyDBFile() {
        val mInput: InputStream = mcontext.getAssets().open(DB_NAME)
        val mOutput: OutputStream = FileOutputStream(DB_PATH + DB_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int
//        while ((mInput.read(mBuffer).also { mLength = it }) > 0)
//            mOutput.write(mBuffer, 0, mLength)
        mLength = mInput.read(mBuffer)
        while (mLength > 0) {
            mOutput.write(mBuffer, 0, mLength)

            mLength = mInput.read(mBuffer)
        }
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    @Throws(SQLException::class)
    public fun openDataBase():Boolean{
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY)
        return mDataBase != null
    }

    @Synchronized
    override fun close() {
        if (mDataBase != null)
            mDataBase?.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(newVersion > oldVersion)
            mNeedUpdate = true
    }

//    public fun getSpecialties():ArrayList<Specialty>{
//        val specialties = ArrayList<Specialty>()
//        var specialty : Specialty
//
//        //specialties = mDBHelper.getSpecialties();
//        mDataBase?.rawQuery("select * from specialties", null).use{
//                cursor ->
//            cursor?.moveToFirst()
//
//            while (!cursor?.isAfterLast!!){
//                specialty = Specialty(null,null)
//                specialty.id = cursor.getLong(0)
//                specialty.name = cursor.getString(1)
//                specialties.add(specialty)
//                cursor.moveToNext()
//            }
//        }
//        return specialties
//    }

}

