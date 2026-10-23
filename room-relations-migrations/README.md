# Room Auto-Migrations Guide

Very carefully follow these steps:

### 1. Install the Room Gradle Plugin

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

---

### 2. Generate Initial Schema (Version 1)

- Build, run, and use the app to export schema version 1.

### !! Just then !!

---

### 3. Update Database & Entity (Version 2)

- **Update Database annotation**:
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

- **Modify Entity model**:
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
### Being the schema already there is possible then to apply further changes
- Delete columns - @DeleteColumn
- Add more
- or also just rename columns - @RenameColumn
- **Always updating the version every time**
### renaming example
  ```kotlin
  @Entity(tableName = "data_table")
  data class DataEntity(
    ...
    // column renamed in migration 2 to 3
    @ColumnInfo(name = "data_label")
    val label: String,
  )
  ```
- **inside the database class**
- otherwise the schema version.json file is not in the right folder  
```koltin
    @RenameColumn(tableName = "data_table", fromColumnName = "label", toColumnName = "data_label")
    class RenameColumnLabelToDataLabel : AutoMigrationSpec
```
- add the specification
- **Increase the version number!**
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