package ksnd.hiraganaconverter

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.print
import com.lemonappdev.konsist.api.ext.list.withNameMatching
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class KonsistTest {
    // ref: https://docs.konsist.lemonappdev.com/inspiration/snippets/general-snippets#id-10.-no-empty-files-allowed
    @Test
    fun `no empty files allowed`() {
        Konsist
            .scopeFromProject()
            .files
            .assertFalse { it.text.isEmpty() }
    }

    @Test
    fun `functions containing 'Preview' must start with 'Preview'`() {
        Konsist
            .scopeFromProject()
            .functions()
            .withNameMatching(Regex(".*Preview.*"))
            .print()
            .assertTrue { it.hasNameStartingWith("Preview", ) }
    }
}