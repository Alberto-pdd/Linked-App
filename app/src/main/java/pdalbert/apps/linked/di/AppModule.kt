package pdalbert.apps.linked.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pdalbert.apps.linked.data.local.SessionManager
import pdalbert.apps.linked.data.repository.FakeFolderRepository
import pdalbert.apps.linked.data.repository.FakeLinkRepository
import pdalbert.apps.linked.data.repository.FolderRepository
import pdalbert.apps.linked.data.repository.LinkRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {

    @Binds
    @Singleton
    abstract fun bindLinkRepository(impl: FakeLinkRepository): LinkRepository

    @Binds
    @Singleton
    abstract fun bindFolderRepository(impl: FakeFolderRepository): FolderRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ProvideModule {

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }
}
