package com.cws.printer

actual class FileLogger() {

    private var indexedDB: dynamic? = null

    private val logs = StringBuilder()

    actual fun open(name: String, filepath: String) {
        indexedDB = js("window.indexedDB.open(name, 1)")

        indexedDB.onupgradeneeded = {
            val db = indexedDB.result
            db.createObjectStore("logs", js("{ keyPath: 'id', autoIncrement: true }"))
        }

        indexedDB.onsuccess = {
            val db = indexedDB.result
            val transaction = db.transaction("logs", "readwrite")
            val store = transaction.objectStore("logs")
            val log = logs.toString()
            logs.clear()
            store.add(js("{ message: log, timestamp: Date.now() }"))
        }

        indexedDB.onerror = {
            console.error("IndexedDB error thrown", indexedDB.error)
        }
    }

    actual fun close() {
        val db = indexedDB.result
        db.close()
    }

    actual fun log(message: String) {
        logs.append(message).append("\n")
    }

}