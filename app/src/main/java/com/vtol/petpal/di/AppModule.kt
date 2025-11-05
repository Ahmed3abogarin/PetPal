package com.vtol.petpal.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.vtol.petpal.data.repository.AppRepositoryImpl
import com.vtol.petpal.data.repository.MapsRepositoryImpl
import com.vtol.petpal.domain.LocationProvider
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.domain.repository.MapsRepository
import com.vtol.petpal.domain.usecases.AddPet
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.GetPets
import com.vtol.petpal.domain.usecases.GetVets
import com.vtol.petpal.domain.usecases.MapsUseCases
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
    fun provideAppRepository(firestore: FirebaseFirestore): AppRepository =
        AppRepositoryImpl(firestore)


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
        AppUseCases(addPet = AddPet(appRepository), getPets = GetPets(appRepository))
}