package org.example

import org.junit.Test
import kotlin.test.assertEquals

internal class FindTest {

    @Test
    fun test_1() {
        val directory = "files"
        val file = "first.txt"
        val subdirectory = false
        val assertPath = mutableListOf("files\\first.txt")
        assertEquals(assertPath, Find().thePathToTheFile(subdirectory, directory, file))
    }

    @Test
    fun test_2() {
        val directory = "files"
        val file = "second.txt"
        val subdirectory = true
        val assertPath = mutableListOf("files\\heh\\second.txt")
        assertEquals(assertPath, Find().thePathToTheFile(subdirectory, directory, file))
    }

    @Test
    fun test_3() {
        val directory = "files"
        val file = "third.txt"
        val subdirectory = false
        val assertPath = mutableListOf("files\\third.txt")
        assertEquals(assertPath, Find().thePathToTheFile(subdirectory, directory, file))
    }

    @Test
    fun test_4() {
        val directory = "files"
        val file = "fourth.txt"
        val subdirectory = true
        val assertPath = mutableListOf("files\\aLotOfPapok\\a\\lot\\of\\papok\\fourth.txt")
        assertEquals(assertPath, Find().thePathToTheFile(subdirectory, directory, file))
    }

    @Test
    fun test_6() {
        val directory = "files"
        val file = "bul'bul'.txt"
        val subdirectory = false
        val assertPath = emptyList<String>()
        assertEquals(assertPath, Find().thePathToTheFile(subdirectory, directory, file))
    }
}
