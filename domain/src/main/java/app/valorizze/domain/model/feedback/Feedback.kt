package app.valorizze.domain.model.feedback

import app.valorizze.core.enums.feedback.FeedbackType

data class Feedback(
    val title: String,
    val message: String? = null,
    val type: FeedbackType,
)

