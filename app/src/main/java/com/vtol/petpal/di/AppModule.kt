package com.vtol.petpal.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vtol.petpal.data.local.TasksDB
import com.vtol.petpal.data.local.TasksDao
import com.vtol.petpal.data.repository.AppRepositoryImpl
import com.vtol.petpal.data.repository.AuthRepositoryImpl
import com.vtol.petpal.data.repository.MapsRepositoryImpl
import com.vtol.petpal.domain.LocationProvider
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.domain.repository.AuthRepository
import com.vtol.petpal.domain.repository.MapsRepository
import com.vtol.petpal.domain.usecases.AddPet
import com.vtol.petpal.domain.usecases.AddWeight
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.GetPet
import com.vtol.petpal.domain.usecases.GetPets
import com.vtol.petpal.domain.usecases.GetVets
import com.vtol.petpal.domain.usecases.GetWeights
import com.vtol.petpal.domain.usecases.MapsUseCases
import com.vtol.petpal.domain.usecases.register.AuthUseCases
import com.vtol.petpal.domain.usecases.register.GetAuthState
import com.vtol.petpal.domain.usecases.register.Logout
import com.vtol.petpal.domain.usecases.register.ReadAppEntry
import com.vtol.petpal.domain.usecases.register.Register
import com.vtol.petpal.domain.usecases.register.SaveAppEntry
import com.vtol.petpal.domain.usecases.register.SignIn
import com.vtol.petpal.domain.usecases.tasks.GetTasksById
import com.vtol.petpal.domain.usecases.tasks.GetTasks
import com.vtol.petpal.domain.usecases.tasks.InsertTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMapsRepository(@ApplicationContext context: Context): MapsRepository =
        MapsRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideAppRepository(firestore: FirebaseFirestore, tasksDao: TasksDao, auth: FirebaseAuth, storage: FirebaseStorage): AppRepository =
        AppRepositoryImpl(firestore, tasksDao, auth, storage)


    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth,firestore: FirebaseFirestore, datastore: DataStore<Preferences>): AuthRepository =
        AuthRepositoryImpl(auth, firestore,datastore)


    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("app_prefs")
            }
        )
    }

    @Provides
    @Singleton
    fun provideMapsUseCases(repository: MapsRepository) =
        MapsUseCases(getNearLocations = GetVets(repository))

    @Provides
    @Singleton
    fun provideLocation(@ApplicationContext context: Context) = LocationProvider(context)


    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()


    @Provides
    @Singleton
    fun provideAppUseCases(appRepository: AppRepository) =
        AppUseCases(
            addPet = AddPet(appRepository),
            getPets = GetPets(appRepository),
            getPet = GetPet(appRepository),
            insertTask = InsertTask(appRepository),
            getTasks = GetTasks(appRepository),
            getTasksById = GetTasksById(appRepository),
            addWeight = AddWeight(appRepository),
            getWeights = GetWeights(appRepository)
        )

    @Provides
    @Singleton
    fun provideAuthUseCases(repository: AuthRepository) =
        AuthUseCases(
            signIn = SignIn(repository),
            signUp = Register(repository),
            logout = Logout(repository),
            getAuthState = GetAuthState(repository),
            readAppEntry = ReadAppEntry(repository),
            saveAppEntry = SaveAppEntry(repository)
        )


    @Provides
    @Singleton
    fun provideTasksDB(application: Application): TasksDB {
        return Room.databaseBuilder(
            context = application,
            klass = TasksDB::class.java,
            name = "tasks_DB"
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(tasksDB: TasksDB): TasksDao = tasksDB.tasksDao

}