package com.github.kyamada.sample.database

import com.github.kyamada.sample.model.Env
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.DriverManager

object DatabaseFactory {
    private val DATABASE_NAME by lazy {
        var name = "fullstack"
        if (Env.ENV == "test") {
            name += "_test"
        }
        name
    }
    private const val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"

    fun init() {
        createDatabaseIfNotExist()
        val db = Database.connect(hikari())
        db.useNestedTransactions = true

        transaction {
            SchemaUtils.create(Tasks)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = JDBC_DRIVER
        config.jdbcUrl = "jdbc:mysql://${Env.DB_HOSTNAME}/$DATABASE_NAME"
        config.username = Env.DB_USERNAME
        config.password = Env.DB_PASSWORD
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.addDataSourceProperty("zeroDateTimeBehavior", "convertToNull")
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(
            block: suspend () -> T
    ): T {
        return if (Env.ENV == "test") {
            transaction { runBlocking { block() } }
        } else {
            newSuspendedTransaction { block() }
        }
    }

    private fun createDatabaseIfNotExist() {
        // JDBCドライバをロード
        Class.forName(JDBC_DRIVER)
        val conn =
                DriverManager.getConnection("jdbc:mysql://${Env.DB_HOSTNAME}:3306/?user=${Env.DB_USERNAME}&password=${Env.DB_PASSWORD}&zeroDateTimeBehavior=convertToNull")
        val statement = conn.createStatement()
        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS $DATABASE_NAME")
    }
}
