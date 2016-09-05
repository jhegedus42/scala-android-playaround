androidBuild


platformTarget in Android := "android-23"

javacOptions in Compile ++= "-source" :: "1.7" :: "-target" :: "1.7" :: Nil

val circeVersion = "0.4.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq ("org.scalatest" %% "scalatest" % "2.2.6" % "test,androidTest",
  "com.android.support.test" % "runner" % "0.5" % "test,androidTest" ,
  "com.android.support" % "recyclerview-v7" % "22.2.+",
  "org.sqldroid" % "sqldroid" % "1.0.3",
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "com.android.support" % "cardview-v7" % "22.2.+",
  "com.squareup.picasso" % "picasso" % "2.5.2",
  "com.theartofdev.edmodo" % "android-image-cropper" % "2.3.+"


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
resolvers += Resolver.jcenterRepo


minSdkVersion := "11"
dexMulti := true
useProguardInDebug := false
dexMaxHeap := "4096m"
dexMinimizeMain := false
dexMulti := true
protifySettings
