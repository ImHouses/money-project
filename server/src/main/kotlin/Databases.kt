import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import core.budget.db.BudgetItemDbModel
import core.budget.db.TransactionDbModel
import core.user.db.UserConfigDbModel
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.config.tryGetString
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val dbConfig = environment.config.config("database")
    fun createHikariDataSource(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = dbConfig.tryGetString("connectionUrl")
                ?: System.getenv("DB_URL")
                ?: throw IllegalStateException("Database URL not configured")
            log.debug("URL: ${dbConfig.tryGetString("connectionUrl")
                ?: System.getenv("DB_URL")}")
            username = dbConfig.tryGetString("user")
                ?: System.getenv("DB_USER")
                ?: throw IllegalStateException("Database user not configured")
            password = dbConfig.tryGetString("password")
                ?: System.getenv("DB_PASSWORD")
                ?: throw IllegalStateException("Database user not configured")
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }
    val database = Database.connect(createHikariDataSource())

    transaction(database) {
        SchemaUtils.create(UserConfigDbModel, BudgetItemDbModel, TransactionDbModel)
    }
}
