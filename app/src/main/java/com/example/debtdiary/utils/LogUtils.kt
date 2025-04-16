package com.example.debt.app.utils

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException

object LogUtils {
    const val TEST_LOG = "TEST_LOG"

    fun <T> T.wtf(message: String? = null): T {
        Log.wtf("WTF", message ?: this.toString())
        return this
    }

    fun Any.debugLog(msg: Any?, tag: String? = null) {
        Log.d(
            tag ?: setTag(),
            "$START_LOG DEBUG ${getClassWithMethod()} \n\t $RESULT_LOG ${
                createMsg(msg)
            } \n$END_LOG DEBUG ${getClassWithMethod()}\n$BORDER"
        )
    }

    fun Any.testLog(msg: Any?, tag: String? = null) {
        Log.i(
            tag ?: TEST_LOG,
            "$START_LOG TEST ${getClassWithMethod()} \n\t TEST $RESULT_LOG: ${
                createMsg(msg)
            } \n$END_LOG TEST ${getClassWithMethod()}\n$BORDER"
        )
    }

    fun Any.errorLog(msg: Any?, tag: String? = null) {
        Log.e(
            tag ?: setTag(),
            "$START_LOG EXCEPTION ${getClassWithMethod()} \n\t $RESULT_LOG: ${
                createMsg(msg)
            } \n$END_LOG EXCEPTION ${getClassWithMethod()}\n$BORDER"
        )
    }

    private fun createMsg(msg: Any?): String {
        return try {
            val prettyPrintJson = GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
            prettyPrintJson.toJson(msg)
        } catch (m: JsonSyntaxException) {
            msg.toString()
        }
    }


    private fun setTag(): String {
        val re = Regex("[^A-Za-z ]")
        val stack = Throwable().fillInStackTrace()
        val trace = stack.stackTrace
        val fileName = trace[3].fileName.replace(".kt", "")
        var methodName = trace[3].className
            .replace("$", " ")
            .replaceBefore(" ", "")
            .replace(" ", "")
        methodName = re.replace(methodName, "")
        return "$fileName.$methodName"
    }

    private fun getClassWithMethod(): String {
        val re = Regex("[^A-Za-z ]")
        val stack = Throwable().fillInStackTrace()
        val trace = stack.stackTrace
        val fileName = trace[3].fileName.replace(".kt", "")
        var methodName = trace[3].className
            .replace("$", " ")
            .replaceBefore(" ", "")
            .replace(" ", "")
        methodName = re.replace(methodName, "")
        return "$fileName.$methodName() (line: ${trace[3].lineNumber})"
    }

    private const val START_LOG = "<-- START"
    private const val RESULT_LOG = "RESULT:"
    private const val END_LOG = "--> END"
    private const val b1 =
        "\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n"
    private const val b2 =
        "\n=============================================================================================================================================================\n"
    private const val b3 =
        "\n*************************************************************************************************************************************************************\n"
    private const val b4 =
        "\n#############################################################################################################################################################\n"
    private const val BORDER = b3
}