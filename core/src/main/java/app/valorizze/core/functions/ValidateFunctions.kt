package app.valorizze.core.functions

private val EMAIL_REGEX = Regex("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", RegexOption.IGNORE_CASE)

fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email.trim())

fun isValidName(name: String): Boolean = name.trim().length >= 2

fun capitalizeEachWord(text: String): String =
    text.split(" ").filter { it.isNotBlank() }.joinToString(" ") { w ->
        w.lowercase().replaceFirstChar { c -> c.uppercase() }
    }

