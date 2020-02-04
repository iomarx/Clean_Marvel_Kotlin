package com.puzzlebench.cmk.data.repository.source

import io.realm.Realm
import io.realm.RealmObject

abstract class DataSource<T : RealmObject>(private val clazz: Class<T>) {

    fun save(item: T, realm: Realm? = null) {
        if (realm == null) {
            Realm.getDefaultInstance().use { realmInstance ->
                realmInstance.executeTransaction {
                    realmInstance.insertOrUpdate(item)
                }
            }
        } else {
            realm.insertOrUpdate(item)
        }
    }

    fun save(items: List<T>, realm: Realm = Realm.getDefaultInstance()) {
        realm.use { realmInstance ->
            realmInstance.executeTransaction {
                items.map { save(it, realm) }
            }
        }
    }

    fun getAll(realm: Realm = Realm.getDefaultInstance()): MutableList<T>? {
        return realm.copyFromRealm(realm.where(clazz).findAll())
    }

    fun findById(id: Int, realm: Realm = Realm.getDefaultInstance()): T? {
        realm.use {
            val result = it.where(clazz).equalTo("_id", id).findFirst()
            return if (result != null) {
                it.copyFromRealm(result)
            } else {
                null
            }
        }
    }

    fun delete(id: Int): Int {
        var deleteResult = 0
        Realm.getDefaultInstance().use { realm ->
            val result = realm.where(clazz).equalTo("_id", id).findFirst()
            realm.executeTransaction {
                result?.let {
                    it.deleteFromRealm()
                    deleteResult = id
                }
            }
        }

        return deleteResult
    }
}