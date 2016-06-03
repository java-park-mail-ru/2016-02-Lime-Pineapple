package db.exceptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * created: 6/2/2016
 * package: db.exceptions
 */
public class DatabaseException extends Exception {


    private final String reason;
    @Nullable
    private final Exception innerException;
    private final String dbName;

    public DatabaseException() {
        super();
        this.dbName = "";
        this.reason = "Unknown reason";
        this.innerException = null;
    }

    public DatabaseException(@NotNull String reason) {
        this.dbName = "";
        this.reason = reason;
        this.innerException = null;
    }

    public DatabaseException(@NotNull String dbName, @NotNull String reason) {
        this.dbName = dbName;
        this.reason = reason;
        this.innerException = null;
    }

    public DatabaseException(@NotNull String dbName, @NotNull Exception innerException) {
        super();
        this.dbName = dbName;
        this.reason = "InnerException";
        this.innerException = innerException;
    }

    public DatabaseException(@NotNull String dbName, @NotNull String reason, @NotNull Exception innerException) {
        super();
        this.dbName = dbName;
        this.reason = reason;
        this.innerException = innerException;
    }

    @Override
    public String toString() {
        return String.format(
                "Exception in accessing database%s due to reason:%n%s%nInnerException:%n%s",
                !dbName.isEmpty() ? String.format("[name=%s]", this.dbName) : "",
                this.reason,
                this.innerException != null ? this.innerException.toString() : "<null>"
                );
    }
}
