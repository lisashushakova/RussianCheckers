package org.example

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import java.io.File
import kotlin.system.exitProcess


fun main(args: Array<String>) = Find().finder(args)

class Find {

    @Option(name = "-d", usage = "Поиск файла в текущей директории")
    private var directory = "."

    @Option(name = "-r", usage = "Поиск файл во всех поддиректориях")
    private var subdirectory = false

    @Argument(required = true, usage = "Название нужного файла")
    private var file = ""

    private fun parser(args: Array<String>) {
        val parse = CmdLineParser(this)
        try {
            parse.parseArgument(*args)
        } catch (e: CmdLineException) {
            System.err.println(e.message)
            parse.printUsage(System.out)
            exitProcess(1)
        }
    }

    private fun thePathToTheFile(subdirectory: Boolean, directory: String, file: String): MutableSet<File> {
        val result = mutableListOf<File>()
        for (element in directory) {
            if (file in element) result.add
            if (element.isDirectory && subdirectory)
                thePathToTheFile(subdirectory, element, file).forEach { result.add(it.toString()) }
        }
        return result
    }

    fun finder(args: Array<String>) {
        parser(args)
        val result = mutableListOf<String>()
        val checkDir = directory
        val fileDir = File(checkDir)
        var check = false
        if (!fileDir.exists()) {
            check = true
            println("This directory does not exist.")
        }
        if (fileDir.isDirectory) for (path in thePathToTheFile(subdirectory, checkDir, file)) result.add(path)
        if (!check && result.isEmpty()) println("This file does not exist.")
        for (i in result) println(i)
    }
}

class FindFile {
    companion object {
        @JvmStatic
        fun findFile(r: Boolean, directory: Path, filename: Path): MutableSet<Path> {
            val result = mutableSetOf<Path>()
            val p = Paths.get(directory.toString(), filename.toString())
            if (Files.exists(p)) result.add(p)
            if (r) {
                val allFiles = Paths.get(directory.toString()).toFile() ?: return result
                val allDir = allFiles.list() ?: return result
                for (i in allDir) {
                    val check = Paths.get(directory.toString(), i.toString())
                    if (check.toFile().isDirectory) {
                        result.addAll(findFile(true, Paths.get(directory.toString() + File.separator + i), filename))
                    }
                }
            }
            return result
        }
    }