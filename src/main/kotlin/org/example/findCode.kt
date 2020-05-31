package org.example

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import java.io.File
import kotlin.system.exitProcess


fun main(args: Array<String>) = Find().finder(args)

class Find {

    @Option(name = "-d", usage = "Search for a file in the current directory")
    private var directory = "."

    @Option(name = "-r", usage = "Search file in all subdirectories")
    private var subdirectory = false

    @Argument(required = true, usage = "The name of the file")
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

    fun thePathToTheFile(subdirectory: Boolean, directory: String, file: String): List<String> {
        val result = mutableListOf<String>()
        for (element in File(directory).listFiles()!!) {
            val newDirectory = element.toString()
            if (file in newDirectory) result.add(newDirectory)
            if (element.isDirectory && subdirectory)
                thePathToTheFile(subdirectory, newDirectory, file).forEach { result.add(it) }
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