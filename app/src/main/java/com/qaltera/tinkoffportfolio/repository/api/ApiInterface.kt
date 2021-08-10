package com.qaltera.tinkoffportfolio.repository.api

import com.qaltera.tinkoffportfolio.data.OperationsDto
import com.qaltera.tinkoffportfolio.data.PortfolioDto
import com.qaltera.tinkoffportfolio.data.ResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

public interface ApiInterface {

    @GET("openapi/portfolio")
    suspend fun getPortfolio(
        @Query("brokerAccountId") accountId: String?
    ): ResponseDto<PortfolioDto>

    @GET("openapi/operations")
    suspend fun getOperations(@Query("figi") figi: String,
        @Query("from") from: String,
        @Query("to") to: String)
        : ResponseDto<OperationsDto>
}