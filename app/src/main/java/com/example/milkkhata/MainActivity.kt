package com.example.milkkhata

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.milkkhata.room.MyRecord
import com.example.milkkhata.room.MyRecordDao
import com.example.milkkhata.screens.*
import com.example.milkkhata.ui.theme.MilkKhataTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

enum class ScreenType(name: String) {
    ListScreen("ListScreen"), SettingScreen("SettingScreen"), AboutUsScreen("AboutUsScreen"),
    FinalBillScreen("FinalBillScreen"), FilterScreen("FilterScreen")
}

@HiltViewModel
class MainViewModel @Inject constructor(private val dao: MyRecordDao) : ViewModel() {

    enum class ProgressState { NOTHING, PROGRESS, DONE, ERROR }

    val progressState = MutableLiveData(ProgressState.NOTHING)
    val resetProgressState: () -> Unit = { progressState.postValue(ProgressState.NOTHING) }

    val list: LiveData<List<MyRecord>> by lazy { dao.getAll().asLiveData() }

    private var insert: (MyRecord) -> Unit = { record ->
        viewModelScope.launch(Dispatchers.IO) {
            progressState.postValue(ProgressState.PROGRESS)
            val rowId = dao.insert(record = record)
            if (rowId == -1L) progressState.postValue(ProgressState.ERROR)
            else progressState.postValue(ProgressState.DONE)
        }
    }

    val addNewEntryDialogVisibility = mutableStateOf(true)

    val quantity = mutableStateOf("")
    val quality = mutableStateOf(4F)
    val price = mutableStateOf("")
    val note = mutableStateOf("")

    fun saveRecord() {
        insert(MyRecord(date = Date().time, price = price.value.toDouble(), quantity = quantity.value.toDouble(), quality = quality.value.toInt(), note = note.value, paid = false))
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm = ViewModelProvider(this)[MainViewModel::class.java]
        vm.list.observe(this) {
            it.forEach { rec -> Log.d("aaaaaaaaa", rec.toString()) }
            Log.d("aaaaaaaaa", "Total = ${it.size}")
        }

        vm.progressState.observe(this) {
            Log.d("aaaaaaaaa", "Progress State = ${it.name}")
        }
//        (1..30).forEach { vm.list.add(MyRecord(id = it, date = Date().time, price = 55.0, quantity = 1.5, quality = (it % 5) + 1, note = "note-$it", paid = it % 7 == 0)) }
//        vm.insert(MyRecord(date = Date().time, price = 55.0, quantity = 1.5, quality = 5 + 1, note = "note text here", paid = true))
        setContent {
            MilkKhataTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = ScreenType.ListScreen.name) {
                        composable(ScreenType.ListScreen.name) { ListScreen(navController = navController, vm = vm) }
                        composable(ScreenType.SettingScreen.name) { SettingScreen(navController = navController, vm = vm) }
                        composable(ScreenType.AboutUsScreen.name) { AboutUsScreen(navController = navController, vm = vm) }
                        composable(ScreenType.FinalBillScreen.name) { FinalBillScreen(navController = navController, vm = vm) }
                        composable(ScreenType.FilterScreen.name) { FilterScreen(navController = navController, vm = vm) }
                    }
                }
            }
        }
    }
}
