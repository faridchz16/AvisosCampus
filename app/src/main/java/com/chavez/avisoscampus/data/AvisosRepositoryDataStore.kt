package com.chavez.avisoscampus.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.chavez.avisoscampus.model.Aviso
import com.chavez.avisoscampus.model.Origen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AvisosRepositoryDataStore(
    private val almacen: DataStore<Preferences>
) : AvisosRepository {

    override val avisos: Flow<List<Aviso>> = almacen.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferencias -> desdeJson(preferencias[CLAVE_AVISOS]) }

    override suspend fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
        lateinit var aviso: Aviso
        almacen.edit { preferencias ->
            val actuales = desdeJson(preferencias[CLAVE_AVISOS])
            val siguiente = (actuales.maxOfOrNull { it.id } ?: 0) + 1
            val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            aviso = Aviso(siguiente, titulo, texto, origen, hora)
            preferencias[CLAVE_AVISOS] = aJson((listOf(aviso) + actuales).take(MAXIMO))
        }
        return aviso
    }

    private fun aJson(avisos: List<Aviso>): String {
        val arreglo = JSONArray()
        avisos.forEach { aviso ->
            arreglo.put(
                JSONObject()
                    .put("id", aviso.id)
                    .put("titulo", aviso.titulo)
                    .put("texto", aviso.texto)
                    .put("origen", aviso.origen.name)
                    .put("hora", aviso.hora)
            )
        }
        return arreglo.toString()
    }

    private fun desdeJson(json: String?): List<Aviso> {
        if (json.isNullOrBlank()) {
            return emptyList()
        }
        return runCatching {
            val arreglo = JSONArray(json)
            List(arreglo.length()) { indice ->
                val objeto = arreglo.getJSONObject(indice)
                Aviso(
                    id = objeto.getInt("id"),
                    titulo = objeto.getString("titulo"),
                    texto = objeto.getString("texto"),
                    origen = Origen.valueOf(objeto.getString("origen")),
                    hora = objeto.getString("hora")
                )
            }
        }.getOrDefault(emptyList())
    }

    private companion object {
        val CLAVE_AVISOS = stringPreferencesKey("avisos")
        const val MAXIMO = 50
    }
}
