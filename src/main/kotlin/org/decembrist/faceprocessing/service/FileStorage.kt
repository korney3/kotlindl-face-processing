package org.decembrist.faceprocessing.service

import java.nio.file.Path;
import java.util.stream.Stream;
 
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream
import java.net.URI

interface FileStorage {
	fun storeMultipartFile(file: MultipartFile, filename: String)
	fun storeInputStream(file: InputStream, filename: String)
	fun loadFile(filename: String): Resource
	fun deleteAll()
	fun init()
	fun loadFiles(): Stream<Path>
	fun getUri(filename: String): URI
}