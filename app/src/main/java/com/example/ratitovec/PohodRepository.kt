package com.example.ratitovec

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

class PohodRepository {
    private var pohodDAO: PohodDAO? = null

    constructor(application: Application) {
        val db = PohodiRoomDatabase.getDatabase(application)
        pohodDAO = db!!.PohodDAO()
    }

    fun selectAll(): LiveData<PagedList<Pohod>> {
        return LivePagedListBuilder(pohodDAO!!.selectAll(), 50).build()
    }

    fun selectThisYear(): LiveData<PagedList<Pohod>> {
        return LivePagedListBuilder(pohodDAO!!.selectThisYear(), 50).build()
    }

    fun selectWithId(id: Int): Pohod {
        return selectIdAsyncTask(pohodDAO!!).execute(id).get()!!
    }

    fun insert(pohod: Pohod) {
        insertAsyncTask(pohodDAO!!).execute(pohod)
    }

    fun insert(pohod: List<Pohod>) {
        insertListAsyncTask(pohodDAO!!).execute(pohod)
    }

    fun delete(pohod: Pohod) {
        deleteAsyncTask(pohodDAO!!).execute(pohod)
    }

    companion object {
        class insertListAsyncTask(val pohodDAO: PohodDAO) : AsyncTask<List<Pohod>, Void, Void?>() {
            override fun doInBackground(vararg params: List<Pohod>?): Void? {
                pohodDAO.insert(params[0]!!)
                return null
            }
        }

        class selectIdAsyncTask(val pohodDAO: PohodDAO) : AsyncTask<Int?, Void, Pohod?>() {
            override fun doInBackground(vararg params: Int?): Pohod? {
                return pohodDAO.selectWithId(params[0]!!)
            }
        }

        class insertAsyncTask(val pohodDAO: PohodDAO) : AsyncTask<Pohod, Void, Void?>() {
            override fun doInBackground(vararg params: Pohod?): Void? {
                pohodDAO.insert(params[0]!!)
                return null
            }
        }

        class deleteAsyncTask(val pohodDAO: PohodDAO) : AsyncTask<Pohod, Void, Void?>() {
            override fun doInBackground(vararg params: Pohod?): Void? {
                pohodDAO.delete(params[0]!!)
                return null
            }
        }
    }
}