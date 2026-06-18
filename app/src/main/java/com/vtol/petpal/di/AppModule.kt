package com.vtol.petpal.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.vtol.petpal.data.billing.BillingManager
import com.vtol.petpal.data.local.TasksDB
import com.vtol.petpal.data.local.TasksDao
import com.vtol.petpal.data.notification.NotificationPermissionManager
import com.vtol.petpal.data.remote.CloudRepository
import com.vtol.petpal.data.remote.GalleryRemoteDataSource
import com.vtol.petpal.data.repository.AppRepositoryImpl
import com.vtol.petpal.data.repository.AuthRepositoryImpl
import com.vtol.petpal.data.repository.CloudRepositoryImpl
import com.vtol.petpal.data.repository.EmergencyRepositoryImpl
import com.vtol.petpal.data.repository.FeedbackRepositoryImpl
import com.vtol.petpal.data.repository.FirebaseAnalyticsHelper
import com.vtol.petpal.data.repository.GalleryRepositoryImpl
import com.vtol.petpal.data.repository.MapsRepositoryImpl
import com.vtol.petpal.data.repository.NotificationRepositoryImpl
import com.vtol.petpal.data.repository.PremiumRepositoryImpl
import com.vtol.petpal.data.repository.SettingsRepositoryImpl
import com.vtol.petpal.data.repository.TaskRepositoryImpl
import com.vtol.petpal.data.repository.UpdateRepositoryImpl
import com.vtol.petpal.data.repository.UserRepositoryImpl
import com.vtol.petpal.data.util.ImageCompressorImpl
import com.vtol.petpal.data.worker.SyncScheduler
import com.vtol.petpal.domain.LocationProvider
import com.vtol.petpal.domain.repository.AnalyticsHelper
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.domain.repository.AuthRepository
import com.vtol.petpal.domain.repository.EmergencyRepository
import com.vtol.petpal.domain.repository.FeedbackRepository
import com.vtol.petpal.domain.repository.GalleryRepository
import com.vtol.petpal.domain.repository.MapsRepository
import com.vtol.petpal.domain.repository.NotificationRepository
import com.vtol.petpal.domain.repository.PremiumRepository
import com.vtol.petpal.domain.repository.SettingsRepository
import com.vtol.petpal.domain.repository.TaskRepository
import com.vtol.petpal.domain.repository.UpdateRepository
import com.vtol.petpal.domain.repository.UserRepository
import com.vtol.petpal.domain.usecases.AddPet
import com.vtol.petpal.domain.usecases.AddWeight
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.GetNotificationStatus
import com.vtol.petpal.domain.usecases.GetVersion
import com.vtol.petpal.domain.usecases.GetPet
import com.vtol.petpal.domain.usecases.GetPets
import com.vtol.petpal.domain.usecases.GetUser
import com.vtol.petpal.domain.usecases.GetVets
import com.vtol.petpal.domain.usecases.GetWeights
import com.vtol.petpal.domain.usecases.MapsUseCases
import com.vtol.petpal.domain.usecases.ToggleNotification
import com.vtol.petpal.domain.usecases.UpdatePet
import com.vtol.petpal.domain.usecases.emergency.AddContact
import com.vtol.petpal.domain.usecases.emergency.DeleteContact
import com.vtol.petpal.domain.usecases.emergency.EmergencyUseCases
import com.vtol.petpal.domain.usecases.emergency.ObserveContacts
import com.vtol.petpal.domain.usecases.emergency.UpdateContact
import com.vtol.petpal.domain.usecases.feedback.SubmitFeedBackUseCase
import com.vtol.petpal.domain.usecases.pets.ValidatePetInputUseCase
import com.vtol.petpal.domain.usecases.register.AuthUseCases
import com.vtol.petpal.domain.usecases.register.GetAuthState
import com.vtol.petpal.domain.usecases.register.Logout
import com.vtol.petpal.domain.usecases.register.ReadAppEntry
import com.vtol.petpal.domain.usecases.register.Register
import com.vtol.petpal.domain.usecases.register.RestPasswordUseCase
import com.vtol.petpal.domain.usecases.register.SaveAppEntry
import com.vtol.petpal.domain.usecases.register.SignIn
import com.vtol.petpal.domain.usecases.register.SignInWithFacebook
import com.vtol.petpal.domain.usecases.register.SignInWithGoogle
import com.vtol.petpal.domain.usecases.tasks.DeleteTask
import com.vtol.petpal.domain.usecases.tasks.GetSpecificTasks
import com.vtol.petpal.domain.usecases.tasks.GetPetTasks
import com.vtol.petpal.domain.usecases.tasks.GetTaskById
import com.vtol.petpal.domain.usecases.tasks.GetTasks
import com.vtol.petpal.domain.usecases.tasks.InsertTask
import com.vtol.petpal.domain.usecases.tasks.ToggleTask
import com.vtol.petpal.domain.usecases.tasks.UpdateTask
import com.vtol.petpal.domain.util.ImageCompressor
import com.vtol.petpal.presentation.register.GoogleAuthUiClient
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
    fun provideHelper(analytics: FirebaseAnalytics): AnalyticsHelper =
        FirebaseAnalyticsHelper(analytics)

    @Provides
    @Singleton
    fun provideGoogleAuthUiClient(
        @ApplicationContext context: Context
    ): GoogleAuthUiClient = GoogleAuthUiClient(context)


    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun provideMapsRepository(@ApplicationContext context: Context): MapsRepository =
        MapsRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideAppRepository(
        firestore: FirebaseFirestore,
        tasksDao: TasksDao,
        auth: FirebaseAuth,
        storage: FirebaseStorage,
        gson: Gson
    ): AppRepository =
        AppRepositoryImpl(firestore, tasksDao, auth, storage, gson)


    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        datastore: DataStore<Preferences>,
        db: TasksDB
    ): AuthRepository =
        AuthRepositoryImpl(auth, firestore, datastore, db)


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
    fun provideUserRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        storage: FirebaseStorage,
        db: TasksDB
    ): UserRepository =
        UserRepositoryImpl(firestore, auth, storage, db)


    @Provides
    @Singleton
    fun provideUpdateRepository(remoteConfig: FirebaseRemoteConfig): UpdateRepository =
        UpdateRepositoryImpl(remoteConfig)


    @Provides
    @Singleton
    fun provideAppUseCases(
        appRepository: AppRepository,
        userRepository: UserRepository,
        updateRepository: UpdateRepository,
        notificationRepository: NotificationRepository,
        imageCompressor: ImageCompressor
    ) =
        AppUseCases(
            addPet = AddPet(appRepository, imageCompressor),
            updatePet = UpdatePet(appRepository, imageCompressor),
            getPets = GetPets(appRepository),
            getPet = GetPet(appRepository),
            insertTask = InsertTask(appRepository, notificationRepository),
            getTasks = GetTasks(appRepository),
            getPetTasks = GetPetTasks(appRepository),
            addWeight = AddWeight(appRepository),
            getWeights = GetWeights(appRepository),
            getUser = GetUser(userRepository),
            getVersion = GetVersion(updateRepository),
            toggleTask = ToggleTask(appRepository),
            toggleNotification = ToggleNotification(notificationRepository),
            getSpecificTasks = GetSpecificTasks(appRepository),
            getNotificationStatus = GetNotificationStatus(notificationRepository),
            deleteTask = DeleteTask(appRepository, notificationRepository),
            updateTask = UpdateTask(appRepository),
            getTaskById = GetTaskById(appRepository)
        )

    @Provides
    @Singleton
    fun provideFeedbackRepository(firestore: FirebaseFirestore): FeedbackRepository =
        FeedbackRepositoryImpl(firestore)


    @Provides
    @Singleton
    fun provideFeedbackUseCase(feedbackRepository: FeedbackRepository): SubmitFeedBackUseCase =
        SubmitFeedBackUseCase(feedbackRepository)

    @Provides
    @Singleton
    fun provideAuthUseCases(repository: AuthRepository) =
        AuthUseCases(
            signIn = SignIn(repository),
            signUp = Register(repository),
            logout = Logout(repository),
            getAuthState = GetAuthState(repository),
            readAppEntry = ReadAppEntry(repository),
            saveAppEntry = SaveAppEntry(repository),
            signInWithGoogle = SignInWithGoogle(repository),
            signInWithFacebook = SignInWithFacebook(repository),
            resetPassword = RestPasswordUseCase(repository)
        )


    @Provides
    @Singleton
    fun provideTasksDB(application: Application): TasksDB {
        val migration12 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE pet_tasks ADD COLUMN deletedDates TEXT NOT NULL DEFAULT ''")
            }
        }

        // NEW: Migration to handle changing Primary Key from INTEGER to TEXT
        val migration56 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 1. Create a temporary table with the exact new schema (id is now TEXT)
                // Double-check your other columns against your 6.json schema file to match types perfectly!
                db.execSQL(
                    """
                CREATE TABLE IF NOT EXISTS pet_tasks_new (
                    id TEXT NOT NULL PRIMARY KEY, 
                    petId TEXT NOT NULL,
                    title TEXT NOT NULL,
                    note TEXT,
                    type TEXT NOT NULL,
                    dateTime INTEGER NOT NULL,
                    deletedDates TEXT NOT NULL,
                    isCompleted INTEGER NOT NULL,
                    repeatInterval TEXT,
                    details TEXT,
                    syncStatus TEXT NOT NULL
                )
            """.trimIndent()
                )

                // 2. Copy the data from the old table to the new table.
                // CAST(id AS TEXT) safely converts old numerical IDs (like 1, 2, 3) into Strings ("1", "2", "3")
                db.execSQL(
                    """
                INSERT INTO pet_tasks_new (id, petId, title, note, type, dateTime, deletedDates, isCompleted, repeatInterval, details, syncStatus)
                SELECT CAST(id AS TEXT), petId, title, note, type, dateTime, deletedDates, isCompleted, repeatInterval, details, syncStatus 
                FROM pet_tasks
            """.trimIndent()
                )

                // 3. Drop the old table
                db.execSQL("DROP TABLE pet_tasks")

                // 4. Rename the temporary table to the official production table name
                db.execSQL("ALTER TABLE pet_tasks_new RENAME TO pet_tasks")
            }
        }

        return Room.databaseBuilder(
            context = application,
            klass = TasksDB::class.java,
            name = "tasks_DB"
        )
            .fallbackToDestructiveMigration(true) // Keeps safety net active
            .addMigrations(migration12, migration56) // <-- Add the new migration step here
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(tasksDB: TasksDB): TasksDao = tasksDB.tasksDao


    @Provides
    @Singleton
    fun provideRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()


    @Provides
    @Singleton
    fun provideNotificationPermissionManager(
        @ApplicationContext context: Context
    ): NotificationPermissionManager {
        return NotificationPermissionManager(context)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        @ApplicationContext ctx: Context,
        dataStore: DataStore<Preferences>
    ): NotificationRepository = NotificationRepositoryImpl(ctx, dataStore)


    @Provides
    @Singleton
    fun provideValidateUseCase(): ValidatePetInputUseCase = ValidatePetInputUseCase()


    @Provides
    @Singleton
    fun provideImageCompressor(
        @ApplicationContext context: Context
    ): ImageCompressor =
        ImageCompressorImpl(context)


    @Provides
    @Singleton
    fun provideEmergencyRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
    ): EmergencyRepository =
        EmergencyRepositoryImpl(firestore, auth)


    @Provides
    @Singleton
    fun provideEmergencyUseCases(repository: EmergencyRepository) =
        EmergencyUseCases(
            observeContacts = ObserveContacts(repository),
            deleteContact = DeleteContact(repository),
            addContact = AddContact(repository),
            updateContact = UpdateContact(repository)
        )


    @Provides
    @Singleton
    fun provideCloudRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): CloudRepository =
        CloudRepositoryImpl(firestore, auth)

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideBillingManager(
        @ApplicationContext context: Context,
        premiumRepository: PremiumRepositoryImpl
    ): BillingManager = BillingManager(context, premiumRepository)

    @Provides
    @Singleton
    fun providePremiumRepository(
        dataStore: DataStore<Preferences>
    ): PremiumRepository = PremiumRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideSyncScheduler(workManager: WorkManager): SyncScheduler =
        SyncScheduler(workManager)


    @Provides
    @Singleton
    fun provideSettingsRepository(
        dataStore: DataStore<Preferences>
    ): SettingsRepository =
        SettingsRepositoryImpl(dataStore)


    @Provides
    @Singleton
    fun provideGalleryRepository(
        galleryRemoteDataSource: GalleryRemoteDataSource
    ): GalleryRepository =
        GalleryRepositoryImpl(galleryRemoteDataSource)


    @Provides
    @Singleton
    fun provideTaskRepository(dao: TasksDao, gson: Gson): TaskRepository =
        TaskRepositoryImpl(dao, gson)
}