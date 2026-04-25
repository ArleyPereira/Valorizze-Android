package app.valorizze.core.mask

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.absoluteValue

class MaskVisualTransformation(private val mask: String) : VisualTransformation {

    private val specialSymbolsIndices = mask.indices.filter { mask[it] != MASK_DIGIT }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        text.forEach { char ->
            while (specialSymbolsIndices.contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), offsetTranslator())
    }

    private fun offsetTranslator() = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val offsetValue = offset.absoluteValue
            if (offsetValue == 0) return 0
            var numberOfHashtags = 0
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < offsetValue
            }
            return masked.length + 1
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == MASK_DIGIT }
        }
    }

    companion object {
        const val MASK_DIGIT = '#'

        const val ZIP_CODE_MASK = "#####-###"
        const val ZIP_CODE_MASK_SIZE = 8

        const val PHONE_MASK = "(##) #####-####"
        const val PHONE_MASK_SIZE = 11

        const val DATE_MASK = "##/##/####"
        const val DATE_MASK_SIZE = 8

        const val CPF_MASK = "###.###.###-##"
        const val CPF_MASK_SIZE = 11

        const val CARD_MASK = "#### #### #### ####"
        const val CARD_MASK_SIZE = 16

        const val CARD_EXPIRATION_MASK = "##/####"
        const val CARD_EXPIRATION_MASK_SIZE = 6

        fun maskText(value: String, mask: String): String {
            if (value.isEmpty()) return ""
            var stringWithMask = ""
            var i = 0
            for (m in mask.toCharArray()) {
                if (m != '#') {
                    stringWithMask += m
                    continue
                }
                try {
                    stringWithMask += value[i]
                } catch (e: Exception) {
                    break
                }
                i++
            }
            return stringWithMask
        }
    }
}

