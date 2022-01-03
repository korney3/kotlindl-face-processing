package org.decembrist.faceprocessing.controller

import org.decembrist.faceprocessing.model.FileInfo
import org.decembrist.faceprocessing.service.FileStorageImpl
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import kotlin.streams.toList


@RequestMapping("/files")
@Controller
class DownloadFileController(private val fileStorage: FileStorageImpl) {

    /*
     * Retrieve Files' Information
     */
    @GetMapping
    fun getListFiles(model: Model): String {
        val fileInfos: List<FileInfo> = fileStorage.loadFiles().map { path ->
            FileInfo(
                path.fileName.toString(),
                //todo
                MvcUriComponentsBuilder.fromMethodName(
                    DownloadFileController::class.java,
                    "downloadFile", path.fileName.toString()
                ).build().toString()
            )
        }.toList()

        model.addAttribute("files", fileInfos)
        return "listfiles"
    }

    /*
     * Download Files
     */
    @GetMapping("/{filename}")
    fun downloadFile(@PathVariable filename: String): ResponseEntity<Any> {
        val fileResource = fileStorage.loadFile(filename)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, """attachment; filename="${fileResource.filename}"""")
            .body(fileResource)
    }
}