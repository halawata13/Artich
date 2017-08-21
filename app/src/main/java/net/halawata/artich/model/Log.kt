package net.halawata.artich.model

import android.util.Log
import net.halawata.artich.BuildConfig
import java.util.regex.Pattern

object Log {

    /**
     * デバッグログを出力する
     */
    fun d(msg: String?) {
        if (!BuildConfig.DEBUG) return

        Log.d(tag, msg)
    }

    /**
     * エラーログを出力する
     */
    fun e(msg: String?) {
        Log.e(tag, msg)
    }

    /**
     * クラス名、メソッド名、行番号からなるログ用タグ
     */
    private val tag: String
        get() {
            val trace = Thread.currentThread().stackTrace[4]
            val traceClass = trace.className
            val splitStr = Pattern.compile("[.]+").split(traceClass)

            val className = splitStr[splitStr.size - 1]
            val methodName = trace.methodName
            val line = trace.lineNumber

            return "$className#$methodName:$line"
        }
}
