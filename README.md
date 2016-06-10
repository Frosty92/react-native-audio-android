# react-native-audio-android

A simple audio recorder for your react native app! 

##Installation

`npm install react-native-audio-android` 

Add the following in `android/app/build.gradle`:

```
dependencies {
  ...
  compile project(':RNAudioRecorder')
}

```

Add the following in `android/settings.gradle`: 
```
include ':RNAudioRecorder', ':app'
project(':RNAudioRecorder').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-audio-android/android')

```

Edit `android/app/src/main/java/.../MainActivity.java` to register the native module:

```
...
import com.frosty92.RNAudioRecorder.RNAudioRecorderPackage; // <-- New
...

public class MainActivity extends ReactActivity {
  ...
  @Override
  protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new RNAudioRecorderPackage() // <-- New
    );
  }
```

##Usage: 

```
//import the module
let AudioRecorder = require('react-native-audio-android');
let audioRecorder = new AudioRecorder();

//to start recording audio:
       audioRecorder.startAudioRecording((success) => {
          console.log(success);
        }, (error) => {
        console.log(error);
        });
        
        
  //to stop recording audio: 
  
     audioRecorder.stopAudioRecording((result) => console.log(result));
        
        
        



