# ==============================================================================
# ProGuard & R8 Optimization and Shrinking Rules - BioThanaweya
# ==============================================================================

# ------------------------------------------------------------------------------
# 1. Annotation Processing Metadata & General Reflection
# ------------------------------------------------------------------------------
# Preserve critical runtime annotations, generic signatures, exceptions, and inner class relationships
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,Exceptions

# Retain source file names and line numbers for de-obfuscated production stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Support for AndroidX @Keep annotations
-keep @androidx.annotation.Keep class * { *; }
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# ------------------------------------------------------------------------------
# 2. Dependency Injection: Dagger & Hilt
# ------------------------------------------------------------------------------
-keep class * extends dagger.hilt.android.HiltAndroidApp { *; }
-keep class * extends androidx.activity.ComponentActivity { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep @dagger.Module class * { *; }
-keep @javax.inject.Singleton class * { *; }
-keep @dagger.Provides class * { *; }
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

-keepclassmembers class * {
    @javax.inject.Inject <init>(...);
    @javax.inject.Inject <fields>;
    @javax.inject.Inject <methods>;
}

# Keep ViewModels and their constructors for Hilt injection
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

-dontwarn dagger.hilt.**
-dontwarn javax.inject.**

# ------------------------------------------------------------------------------
# 3. Firebase & Google Play Services
# ------------------------------------------------------------------------------
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-keep class com.google.firebase.appcheck.** { *; }
-keep class com.google.firebase.ai.** { *; }
-keep class com.google.firebase.components.** { *; }
-keep class * implements com.google.firebase.components.ComponentRegistrar { *; }

-keepclassmembers class * {
    @com.google.firebase.database.IgnoreExtraProperties *;
    @com.google.firebase.database.PropertyName *;
}

# ------------------------------------------------------------------------------
# 4. Serialization: Moshi & Kotlin Serialization
# ------------------------------------------------------------------------------
# Preserve classes annotated with @JsonClass and their generated adapters
-keep @com.squareup.moshi.JsonClass class * { *; }
-keep class *JsonAdapter {
    <init>(...);
    public <methods>;
}

-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}

# Retain generic signatures on Moshi adapter factories
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-dontwarn com.squareup.moshi.**

# Kotlinx Serialization fallbacks
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}
-dontwarn kotlinx.serialization.**

# ------------------------------------------------------------------------------
# 5. Networking: Retrofit & OkHttp
# ------------------------------------------------------------------------------
-keep class retrofit2.** { *; }
-keepclasseswithmembers interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# ------------------------------------------------------------------------------
# 6. Database: Room Persistence
# ------------------------------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-dontwarn androidx.room.paging.**
-dontwarn androidx.room.**

# ------------------------------------------------------------------------------
# 7. Domain, FSRS Spaced Repetition & Kotlin Coroutines
# ------------------------------------------------------------------------------
-keepclassmembers class * extends kotlinx.coroutines.CoroutineScope { *; }
-dontwarn kotlinx.coroutines.**

-keep class com.example.domain.model.** { *; }
-keep class com.example.domain.fsrs.** { *; }
-keep class com.example.data.model.** { *; }
-keep class ru.lorddarthart.fsrs.** { *; }
-dontwarn ru.lorddarthart.fsrs.**

# ------------------------------------------------------------------------------
# 8. Jetpack Compose UI
# ------------------------------------------------------------------------------
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}
