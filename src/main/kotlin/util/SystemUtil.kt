package util

fun getSystemIdentifier() = "${System.getProperty("os.name")}_${System.getProperty("os.version")}_${System.getProperty("os.arch")}"

