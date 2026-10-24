---
name: room-migrations
description: Step-by-step instructions for setting up, configuring, and executing Android Room database migrations using Auto Migrations or SQL Query Migrations.
---

# Room Database Migrations

Use this skill when configuring or executing Room database migrations in Android.

---

## Key Migration Rules

1. **Schema Export Required**: Always set `exportSchema = true` in `@Database`. Room requires exported JSON schema files to validate migrations and generate auto-migrations.
2. **Version Increment**: Always increase `@Database(version = N)` by 1 for each database schema change.
3. **Default Values**: Always specify `@ColumnInfo(defaultValue = "...")` when adding new non-null columns to existing entities to prevent runtime migration failures.
4. **Sequential Execution**: Perform migrations sequentially version-by-version (`1 -> 2`, `2 -> 3`).

---

## 1. Initial Setup & Configuration

### Gradle Plugin Setup

**`gradle/libs.versions.toml`**:
```toml
[plugins]
room = { id = "androidx.room", version.ref = "room" }
```

**Root `build.gradle.kts`**:
```kotlin
plugins {
    alias(libs.plugins.room) apply false
}
```

**Module `build.gradle.kts`**:
```kotlin
plugins {
    alias(libs.plugins.room)
}

android {
    room {
        // Directory where exported database schema JSON files are saved
        schemaDirectory("$projectDir/schemas")
    }
}
```

### Database Provisioning (Hilt Example)

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

## 2. Migration Strategies

1. `fallbackToDestructiveMigration()`: Destroys and recreates database tables. Use only during early development.
2. **Auto Migration**: Room automatically infers schema changes between exported schema JSON files.
3. **SQL Query Migration (Preferred)**: Manual, explicit SQL execution via `Migration(from, to)`.

---

## Option A: Auto Migrations

### Step 1: Modify Entity Model
Add the new column to the entity. Specify `defaultValue` for non-null columns.

```kotlin
@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val label: String,
    // New column added in Version 2 migration
    @ColumnInfo(defaultValue = "${0L}", name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
```

### Step 2: Update `@Database` Annotation
Increase `version` to 2, keep `exportSchema = true`, and declare the `AutoMigration`.

```kotlin
@Database(
    entities = [DataEntity::class],
    version = 2, // Increased version from 1 to 2
    exportSchema = true, // Required for auto migrations
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class MigrationsDatabase : RoomDatabase() {
    abstract fun getDataDao(): DataDao
}
```

### Handling Ambiguous Changes (Renaming / Deleting Columns)

When renaming or deleting columns/tables, Room cannot infer the change automatically and requires an `AutoMigrationSpec`.

1. **Update Entity property**:
   ```kotlin
   @Entity(tableName = "data_table")
   data class DataEntity(
       @PrimaryKey(autoGenerate = true)
       val id: Int = 0,
       // Column renamed from 'label' to 'data_label' in Version 3
       @ColumnInfo(name = "data_label")
       val label: String,
   )
   ```

2. **Define `AutoMigrationSpec` inside the Database class**:
   ```kotlin
   // Spec required for column renaming; must be declared inside the RoomDatabase class
   @RenameColumn(tableName = "data_table", fromColumnName = "label", toColumnName = "data_label")
   class RenameColumnLabelToDataLabel : AutoMigrationSpec
   ```

3. **Increase version & register spec**:
   ```kotlin
   @Database(
       entities = [DataEntity::class],
       version = 3, // Increased version from 2 to 3
       exportSchema = true,
       autoMigrations = [
           AutoMigration(from = 1, to = 2),
           AutoMigration(from = 2, to = 3, spec = MigrationsDatabase.RenameColumnLabelToDataLabel::class)
       ]
   )
   abstract class MigrationsDatabase : RoomDatabase() { ... }
   ```

---

## Option B: SQL Query Migrations (Preferred)

Starting from Version 1:

```kotlin
@Database(
    entities = [DataEntity::class],
    version = 1,
    exportSchema = true, // Required to record schema history
)
abstract class MigrationsDatabase : RoomDatabase() {
    abstract fun getDataDao(): DataDao
}
```

### Step 1: Update Entity Class

```kotlin
@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val label: String,
    // New column added in Version 2 migration
    @ColumnInfo(defaultValue = "${0L}", name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
```

### Step 2: Define Migration Object
Create a `Migration` instance executing raw SQLite commands:

```kotlin
// Manual migration from version 1 to 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Execute SQL statement to add the new created_at column to data_table
        db.execSQL("ALTER TABLE data_table ADD COLUMN created_at INTEGER NOT NULL DEFAULT 0")
    }
}
```

### Step 3: Register Migration in Database Builder

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
        .addMigrations(MIGRATION_1_2) // Attach migration
        .build()
}
```

### Step 4: Increase Database Version

```kotlin
@Database(
    entities = [DataEntity::class],
    version = 2, // Increased version from 1 to 2
    exportSchema = true,
)
abstract class MigrationsDatabase : RoomDatabase() { ... }
```

### Step 5: Subsequent Migrations
For future versions (`2 -> 3`), create `MIGRATION_2_3`, chain it in `.addMigrations()`, and increase `@Database(version = 3)`:

```kotlin
// Chain all sequential migrations in databaseBuilder
Room.databaseBuilder(...)
    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
    .build()

// Update database annotation version to 3
@Database(
    version = 3, // Increased version to 3
    exportSchema = true,
    ...
)
abstract class MigrationsDatabase : RoomDatabase() { ... }
```
