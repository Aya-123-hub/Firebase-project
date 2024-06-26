buildscript {
    repositories {
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.3")
        classpath("com.google.gms:google-services:4.4.1")
    }
}

plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}

allprojects {
    repositories {
    }
}