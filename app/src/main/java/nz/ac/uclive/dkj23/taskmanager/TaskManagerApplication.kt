package nz.ac.uclive.dkj23.taskmanager

import android.app.Application
import nz.ac.uclive.dkj23.taskmanager.model.AppContainer
import nz.ac.uclive.dkj23.taskmanager.model.AppDataContainer

class TaskManagerApplication: Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}