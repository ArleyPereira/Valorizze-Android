package app.valorizze.domain.model.base

import app.valorizze.core.enums.result.ResultStatus
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val resultStatus: ResultStatus = ResultStatus.UNKNOWN,
    val status: Int? = null,
    val action: String? = null,
    val message: String? = null,
    val data: T? = null,
)

