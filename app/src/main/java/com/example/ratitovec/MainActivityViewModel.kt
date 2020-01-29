package com.example.ratitovec

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList

class MainActivityViewModel : AndroidViewModel {
    var repo: PohodRepository
    var pohodi: LiveData<PagedList<Pohod>>

    constructor(app: Application) : super(app) {
        repo = PohodRepository(app)
        pohodi = repo.selectThisYear()
    }

    fun selectAll() {
        pohodi = repo.selectAll()
    }

    fun selectThisYear() {
        pohodi = repo.selectThisYear()
    }

    fun selectWithId(id: Int): Pohod {
        return repo.selectWithId(id)
    }

    fun insert(pohod: Pohod) = repo.insert(pohod)
    fun insert(pohod: List<Pohod>) = repo.insert(pohod)
    fun delete(pohod: Pohod) = repo.delete(pohod)

}