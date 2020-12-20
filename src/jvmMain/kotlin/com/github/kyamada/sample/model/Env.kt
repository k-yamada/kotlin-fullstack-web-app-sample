package com.github.kyamada.sample.model

import io.github.cdimascio.dotenv.Dotenv

object Env {
    /** .env または 環境変数の値をロードする(環境変数が優先される) */
    private val dotenv = Dotenv.configure().ignoreIfMissing().load()
    private var env: String? = null
    val ENV: String get() = env ?: dotenv["ENV"]!!
    val AWS_ACCESS_KEY_ID: String get() = dotenv["AWS_ACCESS_KEY_ID"]!!
    val AWS_SECRET_ACCESS_KEY: String get() = dotenv["AWS_SECRET_ACCESS_KEY"]!!
    val S3_BUCKET_NAME: String get() = dotenv["S3_BUCKET_NAME"]!!
    val REGION: String get() = dotenv["REGION"]!!
    val DB_HOSTNAME: String get() = dotenv["DB_HOSTNAME"] ?: ""
    val DB_USERNAME: String get() = dotenv["DB_USERNAME"]!!
    val DB_PASSWORD: String get() = dotenv["DB_PASSWORD"]!!

    fun setENV(env: String) {
        this.env = env
    }

    val shortEnv: String
        get() {
            return when (ENV) {
                "development" -> "dev"
                "staging" -> "stg"
                "production" -> "prd"
                else -> ENV
            }
        }
}
