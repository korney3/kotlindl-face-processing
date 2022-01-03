package org.decembrist.faceprocessing

import org.decembrist.faceprocessing.service.FileStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import javax.annotation.PostConstruct


@SpringBootApplication
class KotlinSpringBootUploadDownloadFilesApplication {

    @Autowired
    lateinit var fileStorage: FileStorage;

    //todo
    @PostConstruct
    fun run() {
        fileStorage.deleteAll()
        fileStorage.init()
    }
}

fun main(args: Array<String>) {
    val builder = SpringApplicationBuilder(KotlinSpringBootUploadDownloadFilesApplication::class.java)
    builder.headless(false);
    builder.run(*args)
}
