package com.jmvincenti.marvelcharacters.injection

import android.arch.persistence.room.Room
import android.content.Context
import com.jmvincenti.marvelcharacters.BuildConfig
import com.jmvincenti.marvelcharacters.data.api.AuthInterceptor
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.database.CharacterDao
import com.jmvincenti.marvelcharacters.data.database.CharactersDb
import com.jmvincenti.marvelcharacters.data.repository.CharacterRepository
import com.jmvincenti.marvelcharacters.data.repository.CharactersDataSourceFactory
import com.jmvincenti.marvelcharacters.ui.characterdetail.CharacterDetailContract
import com.jmvincenti.marvelcharacters.ui.characterdetail.CharacterDetailPresenter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


/**
 * TODO: Add a class header comment! 😘
 */
class InjectorManager {

    companion object {
        fun getOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor())
                    .build()
        }

        fun getRetrofitClient(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BuildConfig.API_BASE_URL)
                    .build()
        }

        fun getRetrofitClient(): Retrofit {
            return getRetrofitClient(getOkHttpClient())
        }

        fun getCharactersClient(): CharactersClient = CharactersClient(getRetrofitClient())

        fun getCharacterDatasourceFactory(client: CharactersClient) = CharactersDataSourceFactory(client, Executors.newFixedThreadPool(5))

        fun getCharacterDao(context: Context): CharacterDao {
            return getDb(context).characterDao()
        }


        fun getCharacterRepository(context: Context): CharacterRepository {
            val client = getCharactersClient()
            return CharacterRepository(
                    remoteSourceFactory = getCharacterDatasourceFactory(client),
                    charactersClient = getCharactersClient(),
                    characterDao = getCharacterDao(context),
                    retryExecutor = Executors.newFixedThreadPool(5)
            )
        }


        fun getCharacterDetailPresenter(): CharacterDetailContract.Presenter<CharacterDetailContract.View> = CharacterDetailPresenter()

        const val DB_NAME = "characters_db_name"
        fun getDb(context: Context): CharactersDb { //TODO not in injector
            return Room.databaseBuilder(context.applicationContext,
                    CharactersDb::class.java, DB_NAME).build()

        }
    }


}