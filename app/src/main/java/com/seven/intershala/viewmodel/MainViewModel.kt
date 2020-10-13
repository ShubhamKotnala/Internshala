package com.seven.intershala.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seven.intershala.database.NotesDatabase
import com.seven.intershala.model.NotesData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    private var dataBaseInstance: NotesDatabase? = null
    private val compositeDisposable = CompositeDisposable()

    val offlineDataList = MutableLiveData<ArrayList<NotesData>>()
    val saveDataListener = MutableLiveData<Boolean>()
    val deleteDataListener = MutableLiveData<Boolean>()

    fun setInstanceOfDb(dataBaseInstance: NotesDatabase) {
        this.dataBaseInstance = dataBaseInstance
    }

    fun deleteNote(data: NotesData){
        dataBaseInstance?.feedDataDao()?.deleteNoteById(data.id)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
            },{ it.message
                deleteDataListener.postValue(false)
            })?.let {
                deleteDataListener.postValue(true)
                getSavedData()
                compositeDisposable.add(it)
            }
    }

    fun getSavedData() {
        dataBaseInstance?.feedDataDao()?.getAllRecords()!!.map { snapshot: List<NotesData>? ->
            val array = arrayListOf<NotesData>()
            snapshot?.forEach {
                array.add(it)
            }
            array
            }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                offlineDataList.postValue(it)
            }, {
                it.message
            })?.let {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}