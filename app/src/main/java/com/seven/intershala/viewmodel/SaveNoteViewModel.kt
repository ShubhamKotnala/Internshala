package com.seven.intershala.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seven.intershala.database.NotesDatabase
import com.seven.intershala.model.NotesData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SaveNoteViewModel : ViewModel() {

    private var dataBaseInstance: NotesDatabase? = null
    private val compositeDisposable = CompositeDisposable()

    val offlineDataList = MutableLiveData<List<NotesData>>()
    val saveDataListener = MutableLiveData<Boolean>()

    fun setInstanceOfDb(dataBaseInstance: NotesDatabase) {
        this.dataBaseInstance = dataBaseInstance
    }

    fun saveDataIntoDb(data: NotesData){
        dataBaseInstance?.feedDataDao()?.insertNotes(data)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
            },{ it.message
                saveDataListener.postValue(false)
            })?.let {
                saveDataListener.postValue(true)
            }
    }

    fun updateNote(data: NotesData){
        dataBaseInstance?.feedDataDao()?.updateData(data.id, data.title, data.description, data.date)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
            },{ it.message
                saveDataListener.postValue(false)
            })?.let {
                saveDataListener.postValue(true)
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}