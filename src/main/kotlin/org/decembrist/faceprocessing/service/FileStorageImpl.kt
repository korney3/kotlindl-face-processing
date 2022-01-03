package org.decembrist.faceprocessing.service

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

@Service
class FileStorageImpl : FileStorage {

    val rootLocation: Path = Paths.get("filestorage")

    override fun getUri(filename: String): URI {
        val file = rootLocation.resolve(filename)
        return file.toUri()

    }

    fun getFilenameForLandmarkFile(filename: String): String {
        val file = File(filename)
        val filenameWithoutExtension = file.nameWithoutExtension
        val extension = file.extension
        return "${filenameWithoutExtension}_landmark.$extension"
    }

    override fun storeMultipartFile(file: MultipartFile, filename: String) {
        storeInputStream(file.inputStream, filename)
    }

    override fun storeInputStream(file: InputStream, filename: String) {
        if (!Files.exists(rootLocation.resolve(filename))) {
            Files.copy(file, rootLocation.resolve(filename))
        }
    }

    override fun loadFile(filename: String): Resource {
        val resource = UrlResource(getUri(filename))

        if (resource.exists() || resource.isReadable) {
            return resource
        } else {
            throw RuntimeException("FAIL!")
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun init() {
        Files.createDirectory(rootLocation)
    }

    override fun loadFiles(): Stream<Path> {
        return Files.walk(rootLocation, 1)
            .filter { path -> !path.equals(rootLocation) }
            .map(rootLocation::relativize)
    }
}