package org.piramalswasthya.cho.ui.commons.lab_technician

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.cho.database.room.SyncState
import org.piramalswasthya.cho.model.ProcedureDTO
import org.piramalswasthya.cho.model.UserCache
import org.piramalswasthya.cho.repositories.BenFlowRepo
import org.piramalswasthya.cho.repositories.PatientRepo
import org.piramalswasthya.cho.repositories.PatientVisitInfoSyncRepo
import org.piramalswasthya.cho.repositories.UserRepo
import org.piramalswasthya.cho.work.WorkerUtils
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LabTechnicianFormViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    savedStateHandle: SavedStateHandle,
    private val userRepo: UserRepo,
    private val patientVisitInfoSyncRepo: PatientVisitInfoSyncRepo,
    private val benFlowRepo: BenFlowRepo,
    private val patientRepo: PatientRepo
) : ViewModel() {

    private var _loggedInUser: UserCache? = null
    val loggedInUser: UserCache?
        get() = _loggedInUser
    private var _boolCall = MutableLiveData(false)
    val boolCall: LiveData<Boolean>
        get() = _boolCall

    private var _procedures = MutableLiveData<List<ProcedureDTO>>(null)
    val procedures: LiveData<List<ProcedureDTO>>
        get() = _procedures

    @SuppressLint("StaticFieldLeak")
    val context: Context = application.applicationContext

    val state = savedStateHandle

    val isEntitySaved = MutableLiveData<Boolean>()

    suspend fun downloadProcedure(patientId: String) {
        withContext(Dispatchers.IO) {
            benFlowRepo.pullLabProcedureData(patientId)
        }
    }
    suspend fun getPrescribedProcedures(patientId: String) {
        withContext(Dispatchers.IO) {
            _procedures.postValue(patientRepo.getProcedures(patientId))
            Timber.d("fetched procedures")
        }
    }

    fun saveEntity() {
        viewModelScope.launch {
            //map entity
            isEntitySaved.value = true
        }
    }

    fun getLoggedInUserDetails() {
        viewModelScope.launch {
            try {
                _loggedInUser = userRepo.getUserCacheDetails()
                _boolCall.value = true
            } catch (e: Exception) {
                Timber.d("Error in calling getLoggedInUserDetails() $e")
                _boolCall.value = false
            }
        }
    }

    fun resetBool() {
        _boolCall.value = false
    }

    fun saveLabData(dtos: List<ProcedureDTO>?, patientId: String) {
        try {
            dtos?.forEach { procedureDTO ->

                viewModelScope.launch {
                    var procedure =
                        patientRepo.getProcedure(procedureDTO.benRegId, procedureDTO.procedureID)
                    procedureDTO.compListDetails.forEach { componentDetailDTO ->
                        Timber.d("mjf" + componentDetailDTO.testComponentName + ":" + componentDetailDTO.testResultValue)
                        var componentDetails =
                            patientRepo.getComponent(procedure.id, componentDetailDTO.testComponentID)
                        componentDetails.testResultValue = componentDetailDTO.testResultValue
                        componentDetails.remarks = componentDetailDTO.remarks
                        patientRepo.updateComponentDetails(componentDetails)
                    }

                    // update sync state for lab data
                    val patient = patientRepo.getPatient(patientId = patientId)
                    val benFlow = benFlowRepo.getBenFlowByBenRegId(patient.beneficiaryRegID!!)

                    val patientVisitInfoSync = benFlow?.benVisitNo?.let {
                        patientVisitInfoSyncRepo.getPatientVisitInfoSyncByPatientIdAndBenVisitNo(patientId,
                            it
                        )
                    }
                    patientVisitInfoSync?.labDataSynced = SyncState.UNSYNCED

                    patientVisitInfoSync?.let {
                        patientVisitInfoSyncRepo.insertPatientVisitInfoSync(it)
                        WorkerUtils.labPushWorker(context)
                    }
                }
            }

        } catch (e: Exception) {
            Timber.d("error saving lab records due to $e")
        }
    }
}