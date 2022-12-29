# SmartLogs

## Features

1. Create new log file Everyday with current date name.
2. Compress all log files in zip, set password on it (optional).
3. Delete old files depending on the no. of days provided by developer.
4. Display an alert which take input from user about the issue / bug they are facing.
5. The theme of alert view can be customizable.
6. The send-to email can be provided by developer.
7. JSON file can be attached with following information: -> Device manufacturer -> Device model -> OS installed on device -> Currently running app version -> Free storage space available.

## Requirements

* Compile SDK 33
* AGP (Android Gradle Plugin) 7.2.1 or greater
* Theme Material Components
* File Provider Path is required in case you have file provider in your manifest. A tools:replace is required in meta data.
    
```
<provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                 --> tools:replace="android:resource" <--
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/your_path_file" />
        </provider>

```

Path required in xml file

```
<files-path
        name="files"
        path="NewZip/" />
```

## Usage
```
 LogBuilder
            .setDefaultTag("tag")
            .setLogFileName("log file")
            .setTitle("Bug report")
            .setPassword("123456")
            .setEmail("mail@mail.com")
            .setSubjectToEmail("Email subject")
            .setDaysForLog(4)
            .setPassword("123456")
            .setTitleFontSize(18f) // size in sp
            .setSendButtonFontSize(18f) // size in sp
            .setTextViewBackgroundColor(getMyColor(com.whizpool.supportsystem.R.color.gray))
            .setSendBtnImage(getMyDrawable(R.drawable.ic_launcher_background), false) // true for forcefully null, will ignore if drawable is set.
            .setDialogTypeface(getMyFont(R.font.allan))
            .setLineColor(getMyColor(R.color.purple_200))
            .setKnobColor(getMyColor(R.color.purple_200))
            .setMainBackgroundColor(getMyColor(R.color.purple_200))
            .setDialogButtonTextColor(getMyColor(com.whizpool.supportsystem.R.color.gray))
            .setSendButtonBackgroundColor(getMyColor(R.color.white))
            .build(this) // must call this first time

```

Methods can also be use separately

```
 LogBuilder.addAttachment(listOf(Uri))
```

For showing report dialog

```
LogBuilder.sendReport(this, listOf(Uri)) // Second parameter for additional files can be null.
```

## Installation
Add jitpack url in Project level gradle or setting.gradle

```
allprojects {
    repositories {
        google()
        jcenter()
        
     --> maven { url "https://jitpack.io" } <--
       
    }
}
```

Add dependency in app level gradle

```
implementation 'com.github.whizpool:SmartLogs_Android:version'
```
