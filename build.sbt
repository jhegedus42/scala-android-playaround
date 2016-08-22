androidBuild

platformTarget in Android := "android-21"

javacOptions in Compile ++= "-source" :: "1.7" :: "-target" :: "1.7" :: Nil

minSdkVersion := "15"

val circeVersion = "0.4.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq ("org.scalatest" %% "scalatest" % "2.2.6" % "test,androidTest",
  "com.android.support.test" % "runner" % "0.5" % "test,androidTest" ,
  "org.sqldroid" % "sqldroid" % "1.0.3",
  "com.typesafe.slick" %% "slick" % "3.0.0"
  )

instrumentTestRunner in Android := "android.support.test.runner.AndroidJUnitRunner"

debugIncludesTests := true

proguardOptions ++=
    "-dontwarn android.test.**" ::
    "-dontwarn org.scalatest.**" ::
    "-dontwarn scala.xml.**" ::
    "-dontwarn slick.jdbc.**" ::
    "-dontwarn org.slf4j.**" ::
    "-dontwarn org.junit.**" ::
    "-dontobfuscate" ::
    "-dontnote ** "  ::
    "-dontpreverify" ::
    "-optimizations !code/simplification/arithmetic" ::
    "-keep class org.sqldroid.** {*;}" ::
    "-keep class android.support.test.** { *; }" ::
    "-keep class scala.reflect.ScalaSignature {*;}" ::
    "-keep class com.mypackage.** { *; }" ::
    "-keep class * extends junit.framework.TestCase { *; }" ::
    "-keepclasseswithmembers class * { @org.junit.** *; }" ::
    "-keepclassmembers class * { ** bytes();}"::
    "-keep class android.support.test.** { *; }" ::
    "-keep class org.junit.** { *; }" ::
    Nil

//proguardCache ++= "org.scalatest" :: "org.scalactic" :: "org.sqldroid" :: "slick" :: "com.typesafe.slick"::Nil
//proguardCache :=  Nil

minSdkVersion := "21"
dexMulti := true
useProguardInDebug := false