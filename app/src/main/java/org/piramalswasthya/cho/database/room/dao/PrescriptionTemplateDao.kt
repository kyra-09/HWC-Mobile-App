package org.piramalswasthya.cho.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.piramalswasthya.cho.model.PatientVitalsModel
import org.piramalswasthya.cho.model.PrescriptionTemplateDB
@Dao
interface PrescriptionTemplateDao {
    @Insert
    suspend fun insertPrescriptionTemplates(vararg prescriptionTemplateDB: PrescriptionTemplateDB)

    @Query("SELECT * FROM Prescription_Template_DB WHERE user_id = :userID")
    suspend fun getTemplateForUser(userID: Int): List<PrescriptionTemplateDB?>
    @Query("SELECT * FROM Prescription_Template_DB WHERE template_name = :selectedString")
    suspend fun getTemplateForUserUsingTemplateName(selectedString: String): List<PrescriptionTemplateDB?>

    @Query("DELETE FROM Prescription_Template_DB where template_name = :selectedString")
    suspend fun delete(selectedString: String)

}