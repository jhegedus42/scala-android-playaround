androidBuild

platformTarget in Android := "android-23"

javacOptions in Compile ++= "-source" :: "1.7" :: "-target" :: "1.7" :: Nil

minSdkVersion := "15"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq ("org.scalatest" %% "scalatest" % "2.2.6" % "test",
                              aar("com.android.support.test" % "runner" % "0.5"))
instrumentTestRunner in Android :=
    "android.support.test.runner.AndroidJUnitRunner"

debugIncludesTests := true

proguardOptions ++=
    "-dontwarn android.test.**" ::
    "-dontwarn org.hamcrest.**" ::
    "-dontwarn org.junit.**" ::
    "-keep class android.support.test.** { *; }" ::
    "-keep class org.junit.** { *; }" :: Nil