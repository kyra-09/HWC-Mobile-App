package org.piramalswasthya.cho.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import org.piramalswasthya.cho.utils.generateUuid

@Entity(
    tableName = "Chielf_Complaint_DB",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["patientID"],
            childColumns = ["patientID"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = BenFlow::class,
            parentColumns = ["benFlowID"],
            childColumns = ["benFlowID"],
            onDelete = ForeignKey.NO_ACTION
        ),
    ]
)
@JsonClass(generateAdapter = true)
data class ChiefComplaintDB(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "chiefComplaintId") val chiefComplaintId: Int?,
    @ColumnInfo(name = "chiefComplaint") val chiefComplaint: String?,
    @ColumnInfo(name = "duration") val duration: String?,
    @ColumnInfo(name = "durationUnit") val durationUnit: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "patientID") val patientID: String,
    @ColumnInfo(name = "beneficiaryID") var beneficiaryID: Long? = null,
    @ColumnInfo(name = "beneficiaryRegID") var beneficiaryRegID: Long? = null,
    @ColumnInfo(name = "benFlowID") var benFlowID: Long? = null,
){
    constructor(benChiefComplaints: BenChiefComplaints, patient: Patient, benFlow: BenFlow) : this(
        generateUuid(),
        benChiefComplaints.chiefComplaintID,
        benChiefComplaints.chiefComplaint,
        benChiefComplaints.duration?.toString(),
        benChiefComplaints.unitOfDuration,
        benChiefComplaints.description,
        patient.patientID,
        benFlow.beneficiaryID,
        benFlow.beneficiaryRegID,
        benFlow.benFlowID,
    )
}
