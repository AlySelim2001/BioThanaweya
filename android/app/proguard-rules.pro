# Keep runtime annotations and generic signatures used by Room, Kotlin and reflection-based adapters.
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Preserve Room entities and members when Room is included by native feature modules.
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keep @androidx.room.Database class * { *; }
-keep @androidx.room.TypeConverter class * { *; }
-keepclassmembers class * {
    @androidx.room.* <fields>;
    @androidx.room.* <methods>;
}

# WebView JavaScript interface methods are resolved by name from JavaScript at runtime.
-keep class com.biothanaweya.app.WebAppInterface { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep Kotlin metadata for reflection and diagnostics without disabling optimization globally.
-keep class kotlin.Metadata { *; }

# Retain useful source locations in optimized crash reports.
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
