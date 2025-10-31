package com.vtol.petpal.di

import android.content.Context
import com.vtol.petpal.data.repository.AppRepositoryImpl
import com.vtol.petpal.domain.LocationProvider
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.GetVets
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
    fun provideAppRepository(@ApplicationContext context: Context): AppRepository =
        AppRepositoryImpl(context)


    @Provides
    @Singleton
    fun provideAppUseCases(repository: AppRepository) = AppUseCases(getVets = GetVets(repository))

    @Provides
    @Singleton
    fun provideLocation(@ApplicationContext context: Context) = LocationProvider(context)
}