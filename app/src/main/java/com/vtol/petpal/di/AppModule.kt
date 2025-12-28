package com.vtol.petpal.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.vtol.petpal.data.local.TasksDB
import com.vtol.petpal.data.local.TasksDao
import com.vtol.petpal.data.repository.AppRepositoryImpl
import com.vtol.petpal.data.repository.MapsRepositoryImpl
import com.vtol.petpal.domain.LocationProvider
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.domain.repository.MapsRepository
import com.vtol.petpal.domain.usecases.AddPet
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.GetPet
import com.vtol.petpal.domain.usecases.GetPets
import com.vtol.petpal.domain.usecases.GetVets
import com.vtol.petpal.domain.usecases.MapsUseCases
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
    fun provideAppRepository(firestore: FirebaseFirestore, tasksDao: TasksDao): AppRepository =
        AppRepositoryImpl(firestore, tasksDao)


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
    fun provideAppUseCases(appRepository: AppRepository) =
        AppUseCases(
            addPet = AddPet(appRepository),
            getPets = GetPets(appRepository),
            getPet = GetPet(appRepository),
            insertTask = InsertTask(appRepository),
            getTasks = GetTasks(appRepository)
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