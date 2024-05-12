package ksnd.hiraganaconverter.feature.info.licence.mock

import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Developer
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

object MockLibs {
    private val mockLicense = License(
        name = "SampleLicense",
        url = "https://example.com",
        licenseContent = "SampleLicenseContent",
        hash = "1"
    )

    private val mockLibrary = Library(
        uniqueId = "1",
        artifactVersion = "1.0.0",
        name = "SampleLibrary",
        description = "",
        website = "",
        developers = persistentListOf(
            Developer("SampleDeveloper", "https://example.com"),
        ),
        licenses = persistentSetOf(mockLicense),
        organization = null,
        scm = null,
    )

    val item = Libs(
        libraries = persistentListOf(
            mockLibrary,
            mockLibrary.copy(uniqueId = "2", licenses = persistentSetOf()),
            mockLibrary.copy(uniqueId = "3", licenses = persistentSetOf(mockLicense, mockLicense.copy(hash = "2"))),
        ),
        licenses = persistentSetOf(
            mockLicense,
        ),
    )
}
