package org.piramalswasthya.cho.network

import okhttp3.ResponseBody
import org.piramalswasthya.cho.model.LocationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AmritApiService {

    @Headers("No-Auth: true")
    @POST("commonapi-v1.0/user/userAuthenticate/")
    suspend fun getJwtToken(@Body json: TmcAuthUserRequest): Response<ResponseBody>

    @POST("commonapi-v1.0/doortodoorapp/getUserDetails")
    suspend fun getUserDetailsById(@Body userDetail: TmcUserDetailsRequest) : Response<ResponseBody>

    @POST("hwc-facility-service/location/getLocDetailsBasedOnSpIDAndPsmID")
    suspend fun getStates(@Body request: LocationRequest): StateResponseData

    @GET("hwc-facility-service/location/get/districtMaster/{stateId}")
    suspend fun getDistricts(@Path("stateId") stateId: Int): DistrictResponse

    @GET("hwc-facility-service/location/get/districtBlockMaster/{districtId}")
    suspend fun getDistrictBlocks(
        @Path("districtId") districtId: Int
    ): DistrictBlockResponse

    @GET("hwc-facility-service/location/get/villageMasterFromBlockID/{blockId}")
    suspend fun getVillages(
        @Path("blockId") blockId: Int,
    ): VillageMasterResponse

//
//    @POST("tmapi-v1.0/user/getUserVanSpDetails/")
//    suspend fun getTMVanSpDetails(
//        @Body vanServiceType: TmcUserVanSpDetailsRequest
//    ): Response<ResponseBody>
//
//    @POST("mmuapi-v1.0/location/getLocDetailsBasedOnSpIDAndPsmID/")
//    suspend fun getLocationDetails(
//        @Body locationDetails: TmcLocationDetailsRequest
//    ): Response<ResponseBody>
//
//    @POST("bengenapi-v1.0/generateBeneficiaryController/generateBeneficiaryIDs/")
//    suspend fun generateBeneficiaryIDs(
//        @Body obj: TmcGenerateBenIdsRequest
//    ): Response<ResponseBody>
//
//
//    @POST("tmapi-v1.0/registrar/registrarBeneficaryRegistrationNew")
//    suspend fun getBenIdFromBeneficiarySending(@Body beneficiaryDataSending: BeneficiaryDataSending): Response<ResponseBody>
//
//    @POST("identity-0.0.1/rmnch/syncDataToAmrit")
//    suspend fun submitRmnchDataAmrit(@Body sendingRMNCHData: SendingRMNCHData): Response<ResponseBody>
//
//    @POST("identity-0.0.1/rmnch/getBeneficiaryDataForAsha")
//    suspend fun getBeneficiaries(@Body userDetail: GetBenRequest): Response<ResponseBody>
//
//    @POST("identity-0.0.1/id/getByBenId")
//    suspend fun getBeneficiaryWithId(@Query("benId") benId: Long) : Response<ResponseBody>
//
//    @POST("fhirapi-v1.0/healthIDWithUID/createHealthIDWithUID")
//    suspend fun createHid(@Body createHealthIdRequest: CreateHealthIdRequest): Response<ResponseBody>
//
//    @POST("fhirapi-v1.0/healthID/mapHealthIDToBeneficiary")
//    suspend fun mapHealthIDToBeneficiary(@Body mapHIDtoBeneficiary: MapHIDtoBeneficiary): Response<ResponseBody>
//
//    @POST("fhirapi-v1.0/healthIDCard/generateOTP")
//    suspend fun generateOtpHealthId(@Body generateOtpHid: GenerateOtpHid): Response<ResponseBody>
//
//    @POST("fhirapi-v1.0/healthIDCard/verifyOTPAndGenerateHealthCard")
//    suspend fun verifyOtpAndGenerateHealthCard(@Body validateOtpHid: ValidateOtpHid): Response<ResponseBody>

}