-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable,!class/unboxing/enum
-optimizationpasses 5
-verbose

#prevent severe obfuscation
-keep,allowshrinking,allowoptimization class * { <methods>; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers,allowshrinking,allowoptimization class * {
	public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers,allowoptimization class * {
    public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepnames class com.fasterxml.jackson.** { *; }
-keep class com.arcao.menza.api.data.** { *; }

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable #needed
-keepattributes Signature # Needed by google-api-client #to make XStream work with obfuscation?
-keepattributes EnclosingMethod #required?
-keepattributes InnerClasses #required?

-keepattributes Exceptions # can be removed?
-keepattributes Deprecated # can be removed?
-keepattributes Synthetic # can be removed?

-keepattributes *Annotation*

-dontwarn com.fasterxml.jackson.databind.**


