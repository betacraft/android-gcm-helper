#GCMHelper

A very simple helper library for implementing GCM inside your Android code. This does nothing but carries the 
boilerplate code. And gives a callback from the GCMIntentService implemented inside the GCMHelper

## Why

GCM implementation is nothing but putting efforts for the boilerplate code (http://en.wikipedia
.org/wiki/Boilerplate_code). So I thought of combining everything and reduce it to just one dependency. 

This library provides one method RegisterIfNot() and which returns RegId (its stored locally, 
so if your device is already registered with the server then it wont be done again. It tries to handle the exceptions
 in a way suggested by the google developer doc (https://developer.android.com/google/gcm/client.html).

## If you just want to test server side

This repo has a sample app which looks like

![Screenshot](https://raw.github.com/RainingClouds/android-gcm-helper/master/snapshot.png)

Download APK from [here](https://github.com/RainingClouds/android-gcm-helper/releases/tag/0.1)

NOTE:
Message structure for server
```
{"data":{"message":"This is a test message"}}
```

## How to use ?

### Getting aar of GCMHelper

#### From Source Code

1. Clone this repo
2. Perform gradle build
3. Get corresponding .aar file in build folder of the Library project

#### From Release

1. Download gcm-helper.aar from [here](https://github.com/RainingClouds/android-gcm-helper/releases).


4. Create a new folder in your project (on the level of assets) aars
5. Copy the built aar into this folder
6. Add following as repo in your build gradle
```gradle
 repositories {
        ...
        flatDir {
            dirs 'aars'
        }
    }
```
And add following dependency
```
compile(name: 'library', ext: 'aar')
```

### Using inside code

Once you have aar in place. Add a WakefulBroadcastReceiver 
 
```java
public final class GCMReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "###GCMReciever###";

    @Override
    public void onReceive(final Context context, final Intent intent) {
    Log.d(TAG,"Got :" +intent.getExtras().getString("from"));

    }
}
```
And add following to your AndroidManifest.xml
 
```xml
<receiver
    android:name="<path_of_above_class>"
    android:permission="com.google.android.c2dm.permission.SEND" >
    <intent-filter>
        <action android:name="com.google.android.c2dm.intent.RECEIVE" />
        <category android:name="com.rc.gcmhelper.gcm" />
    </intent-filter>
</receiver>
```

Now you will have to use GCMRegistrar class to register if the device is not already registered

```java
GCMRegistrar.RegisterIfNotRegistered(GCMHelperAppDemoActivity.this, senderId,
new GCMRegistrar.GCMRegistrarListener() {
    @Override
    public void registrationDone(final String regId) {                               
    }

    @Override
    public void registering() {
    }

    @Override
    public void errorWhileRegistering(Throwable exception) {                                
    }
});
}
```

And you are done !!

##Licence

The MIT License (MIT)

Copyright (c) 2014 Akshay Deo

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
