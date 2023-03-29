# SmartLogs

## Features

1. Create new log file Everyday with current date name.
2. Compress all log files in zip, set password on it (optional).
3. Delete old files depending on the no. of days provided by developer.
4. Display an alert which take input from user about the issue / bug they are facing.
5. The theme of alert view can be customizable.
6. The send-to email can be provided by developer.
7. JSON file can be attached with following information: -> Device manufacturer -> Device model -> OS installed on device -> Currently running app version -> Free storage space available.
8. Localization included

    * Chinese (Traditional)
    * Chinese (Simplified)
    * Dutch
    * English
    * French
    * German
    * Italian
    * Japanese
    * Korean
    * Portuguese
    * Spanish
    

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

### Initialization and setup

```
 SLog
            .setDefaultTag("tag")
            .setLogFileName("log file")
            .setDaysForLog(4)             // number of last working days for collecting logs
            .setPassword("123456")        // log file password
            .hideReportDialogue(false)
            .build(this)          // must call this first time

```


Old log files can also be deleted

```
SLog.deleteOldLogs()
```

### Report dialog setup

Report dialog can be show or hide. By default it is not hide.

```
 SLog.hideReportDialogue(true) // true for hiding, false for showing
```


Report dialog can be customized

```
 SLog
            .setTitle("Bug report")
            .setEmail("mail@mail.com")
            .setSubjectToEmail("Email subject")
            .setTitleFontSize(18f) // size in sp
            .setSendButtonFontSize(18f) // size in sp
            .setTextViewBackgroundColor(getMyColor(com.whizpool.supportsystem.R.color.gray))
            .setSendBtnImage(getMyDrawable(R.drawable.ic_launcher_background)!!, false)
            .setDialogTypeface(getMyFont(R.font.allan))
            .setLineColor(getMyColor(R.color.purple_200))
            .setKnobColor(getMyColor(R.color.purple_200))
            .setMainBackgroundColor(getMyColor(R.color.purple_200))
            .setDialogButtonTextColor(getMyColor(R.color.gray))
            .setSendButtonBackgroundColor(getMyColor(R.color.white))
            .dialogEditTextBackground(getMyDrawable(R.drawable.myDrawable))
            .build(this) // must call this first time
```


Methods can also be use separately, but make sure build method is already called.
For example adding list of addition files uri.

```
 SLog.addAttachment(listOf(Uri))
```


For sending report to mail

```
SLog.sendReport(this, listOf(Uri)) // Second parameter for additional files can be null.
```


Log files can be print using following methods

* tag -> Tag of log.
* text -> Additional message with log.
* shouldSave -> Indicates should file save in auto generated file in storage.
* exception -> Exception thrown in part of code.

For summary logs

```

 SLog.summaryLog( 
                tag = "Crash",
                text = "App crashed....",
                shouldSave = true,
                exception = someException)
```

For Detail logs

```
// debug log
 SLog.detailLogs(
                tag = "Debug with tag",
                text = "msg",
                shouldSave = false,   // by default it will not write in log file
                exception = someException)
```

For Info logs

```
SLog.logInfo(
                tag = "Info tag",
                text = "msg",
                shouldSave = true,
                exception = someException)
```

For Warning logs

```
SLog.logWarn(
                tag = "Warning tag",
                text = "msg",
                shouldSave = true,
                exception = someException)
```

For Error logs

```
 SLog.logError(
                tag = "Error tag",
                text = "msg",
                shouldSave = true,
                exception = someException)
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
implementation 'com.github.whizpool:smartlogs-android:1.6'
```
