package com.chavez.avisoscampus

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.chavez.avisoscampus.data.AvisosRepository
import com.chavez.avisoscampus.data.AvisosRepositoryDataStore
import com.chavez.avisoscampus.notificacion.Notificador

private val Context.almacenAvisos: DataStore<Preferences> by preferencesDataStore(name = "historial_avisos")

class AvisosApp : Application() {

    val repositorio: AvisosRepository by lazy {
        AvisosRepositoryDataStore(almacenAvisos)
    }

    override fun onCreate() {
        super.onCreate()
        Notificador.crearCanales(this)
    }
}
