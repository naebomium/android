# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:


-optimizationpasses 5


-dontwarn com.squareup.okhttp.*
-dontwarn java.lang.invoke.*
-dontwarn android.support.**
-dontwarn com.google.ads.**
-dontwarn retrofit.**


-dontwarn android.support.**


##To remove debug logs:
#-assumenosideeffects class android.util.Log {
#    public static *** d(...);
#    public static *** v(...);
#}

#Keep the R
-keepclassmembers class **.R$* {
    public static <fields>;
}

#android
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends android.app.Fragment
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#android

#support lib
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
#support lib


#api
-keepattributes *Annotation*
-keepattributes Signature
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn rx.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keepattributes *Annotation*

-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement


-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection﻿
-keep class * extends dagger.internal.BindingsGroup
-dontwarn dagger.internal.**


-keep class com.mobium.new_api.methodParameters.** { *; }
-keep class com.mobium.new_api.models.** { *; }
-keep class com.mobium.new_api.models.* { *; }
-keep interface com.mobium.new_api.ApiInterface{ *; }
-keep class com.mobium.client.** { *; }

#api

#native
-keepclasseswithmembernames class * {
 native <methods>;
}
#native



# For RxJava:
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**


-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#scan
-keep class net.sourceforge.zbar.** { *; }

#js
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

#enums
-keep public enum com.stuff.MyConfigObject$** {
    **[] $VALUES;
    public *;
}
-keepattributes InnerClasses


-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class org.parceler.Parceler$$Parcels


-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
 -dontwarn com.fasterxml.jackson.databind.**
 -keep class org.codehaus.** { *; }
 -keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
 public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }

-dontwarn com.squareup.okhttp.**



-keep public class com.mobileapptracker.** { public *; }

-keep public class com.google.android.gms.ads.identifier.** { *; }

-keep class com.flurry.** { *; }
-dontwarn com.flurry.**

-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** {
  *;
}

-keep public class org.apache.http.**
-keepclassmembers public class org.apache.http.** {
  *;
}
-dontwarn net.hockeyapp.android.UpdateFragment
-dontwarn net.hockeyapp.android.UpdateActivity

-keepclassmembers class net.hockeyapp.android.UpdateFragment {
  *;
}

-keepclassmembers class name.cpr.VideoEnabledWebView$JavascriptInterface { public *; }