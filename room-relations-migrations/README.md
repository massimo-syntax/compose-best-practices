# Room Migrations Guide

Follow these steps to configure and execute Room database migrations:

---

## Setup & Initial Schema

### Step 1: Install the Room Gradle Plugin

- **`gradle/libs.versions.toml`** (`[plugins]`):
  ```toml
  room = { id = "androidx.room", version.ref = "room" }
  ```

- **Root `build.gradle.kts`**:
  ```kotlin
  plugins {
      alias(libs.plugins.room) apply false
  }
  ```

- **Module `build.gradle.kts`**:
  ```kotlin
  plugins {
      alias(libs.plugins.room)
  }

  android {
      ...
      room {
          schemaDirectory("$projectDir/schemas")
      }
  }
  ```

### Step 2: Generate Initial Schema (Version 1)

- Build, run, and interact with the app to export schema Version 1.

### Database Creation

In this example, **Hilt** is used to provide the database instance rather than a direct `getInstance()` method inside the database class. DAOs can be injected directly via Hilt:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): MigrationsDatabase {
        return Room.databaseBuilder(
            context,
            MigrationsDatabase::class.java,
            "query_migrations_database"
        ).build()
    }

    @Provides
    fun provideDataDao(database: MigrationsDatabase): DataDao {
        return database.getDataDao()
    }

}
```

---

## Migration Options

- `fallbackToDestructiveMigration()` - used in development
- Auto migration
- Migrations using SQL queries (preferred)

---

## Auto Migrations

### Update Database & Entity (Version 2)

- **Modify Entity model**:
  - Setting `@ColumnInfo(defaultValue = "${0L}")` is important when adding non-null columns (starts from a default value).
  ```kotlin
  @Entity(tableName = "data_table")
  data class DataEntity(
      @PrimaryKey(autoGenerate = true)
      val id: Int = 0,
      val label: String,

      @ColumnInfo(defaultValue = "${0L}", name = "created_at")
      val createdAt: Long = System.currentTimeMillis()
  )
  ```

- **Update `@Database` annotation**:
  ```kotlin
  @Database(
      entities = [DataEntity::class],
      version = 2,
      exportSchema = true,
      autoMigrations = [
          AutoMigration(from = 1, to = 2)
      ]
  )
  abstract class MigrationsDatabase : RoomDatabase() {
      abstract fun getDataDao(): DataDao
  }
  ```

### Further Schema Changes

It is possible to apply further changes (always increasing the database version each time):
- Delete columns (`@DeleteColumn`)
- Add new columns
- Rename columns (`@RenameColumn`)

#### Renaming Column Example (Version 2 → 3)

1. **Rename column in Entity model**:
   ```kotlin
   @Entity(tableName = "data_table")
   data class DataEntity(
       ...
       // Column renamed in migration 2 to 3
       @ColumnInfo(name = "data_label")
       val label: String,
   )
   ```

2. **Add `AutoMigrationSpec` inside the Database class**:
   *(Must be defined inside the database class so schema files are output to the correct location)*
   ```kotlin
   @RenameColumn(tableName = "data_table", fromColumnName = "label", toColumnName = "data_label")
   class RenameColumnLabelToDataLabel : AutoMigrationSpec
   ```

3. **Increment version number & attach spec**:
   ```kotlin
   @Database(
       ...
       version = 3,
       exportSchema = true,
       autoMigrations = [
           AutoMigration(from = 1, to = 2),
           AutoMigration(from = 2, to = 3, spec = MigrationsDatabase.RenameColumnLabelToDataLabel::class)
       ]
   )
   ```

---

## Migrations Using SQL Queries (Preferred)

Starting from an unmigrated database class (Version 1):

```kotlin
@Database(
    entities = [DataEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class MigrationsDatabase : RoomDatabase() {
    abstract fun getDataDao(): DataDao
}
```

### Step 1: Add new column(s) in the Entity data class
- Setting `@ColumnInfo(defaultValue = "${0L}")` is important when adding non-null columns (starts from a default value).

```kotlin
@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val label: String,
    // New column
    @ColumnInfo(defaultValue = "${0L}", name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
```

### Step 2: Define Migration object
Create `Migration.kt` in a `.migration` package (in the same location as the database):

```kotlin
// Migration from version 1 to 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE data_table ADD COLUMN created_at INTEGER NOT NULL DEFAULT 0")
    }
}
```

### Step 3: Add Migration to Database Builder

```kotlin
@Provides
@Singleton
fun provideAppDatabase(
    @ApplicationContext context: Context
): MigrationsDatabase {
    return Room.databaseBuilder(
        context,
        MigrationsDatabase::class.java,
        "query_migrations_database"
    )
        .addMigrations(MIGRATION_1_2)
        .build()
}
```

### Step 4: Update Database Version
**Very Important**: Update the database version in the `@Database` annotation:

```kotlin
@Database(
    entities = [DataEntity::class],
    version = 2, // Version updated from 1 to 2
    exportSchema = true,
)
abstract class MigrationsDatabase : RoomDatabase() {
    ...
}
```

---

### Adding Subsequent Migrations

1. Chain additional migrations in `Room.databaseBuilder()`:
   ```kotlin
   Room.databaseBuilder(...)
       .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
   ```

2. Always increment the version number in `@Database`:
   ```kotlin
   @Database(
       version = 3, // Version updated from 2 to 3
       ...
   )
   abstract class MigrationsDatabase : RoomDatabase() { ... }
   ```
