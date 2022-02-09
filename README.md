<img align="right" src="./VoidMe_Logo.png" alt="VoidMe Logo" width="200"/>

#    **VoidMe**

**Members:**

- _[Moritz Dallendörfer](https://gitlab.mi.hdm-stuttgart.de/md139)_
- _[Benedict Lang](https://gitlab.mi.hdm-stuttgart.de/bl042)_

**Group:**

_HdM Stuttgart_

_Mobile Application Developement 1- Dozenten: [Andreas Gawelczyk](https://gitlab.mi.hdm-stuttgart.de/gawelczyk)_, [Matthias Nagel](https://gitlab.mi.hdm-stuttgart.de/nagel)

___

# _About this Project_ #
**Last Updated: November, 11st 2021**

VoidMe is a simple Android app that allows you to blacklist specific locations and to add them into a SQLight database. To accomplish this, you create a new entry at the current location that certain people or vehicles should avoid. One chooses a suitable category, adds a short description and the location is stored in the database with the entered information and coordinates.
Furthermore, the entries can be displayed vividly.

___

## Categories

- busy places (Überfüllte Orte)
- dark alleys (Dunkle Gassen)
- narrow streets (Schmale Straßen)
- poor accessibility (Schlechte Barrierefreiheit)
- danger of slipping (Rutschgefahr)

## Features

* [x] Location & Sensors:
  * GPS/Galileo
* [x] Data Storage:
  * App local data storage with SQLight
  * Shared Preferences


## Configuration & Dependencies

| Module | Version |
| ------ | ------ |
| Gradle | v7.0.2 |
| Android Gradle Plugin | v7.0.4 |
| Android CompileSdk| 31 |
| Android MinSdk | 23 |
| Android TargetSdk | 31 |

```javascript
dependencies {

    implementation 'androidx.appcompat:appcompat:1.4.1'
    implementation 'com.google.android.material:material:1.5.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.3'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.4.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0'

    implementation 'androidx.room:room-runtime:2.4.1'
    implementation 'androidx.test.uiautomator:uiautomator:2.2.0'
    annotationProcessor 'androidx.room:room-compiler:2.4.1'

    implementation "androidx.fragment:fragment:1.4.1"
    implementation "androidx.activity:activity:1.4.0"

    implementation 'androidx.navigation:navigation-fragment:2.4.0'
    implementation 'androidx.navigation:navigation-ui:2.4.0'
    implementation 'com.google.android.gms:play-services-maps:18.0.2'
    implementation 'com.google.android.gms:play-services-location:19.0.1'

    implementation 'androidx.preference:preference:1.2.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
    androidTestImplementation 'androidx.test.espresso:espresso-contrib:3.4.0'
    androidTestImplementation 'androidx.test:rules:1.4.0'
}
```


## Legals ##

***
``` 
 - Program name:    VoidMe
 - Description:     Location Tracker for categorized blacklisting of locations that should be avoided. 
 - Version:         1.0.0      
 - Date:            11/22/2021
 - Authors:         Benedict Lang, Moritz Dallendörfer
 - Contact:         bl042@hdm-stuttgart.de, md139@hdm-stuttgart.de
 - Copyright:       © 2021 HdM University of Applied Sciences, Lang, Dallendörfer                       
 - License:         MIT-License
                                                                                                   
   Permission is hereby granted, free of charge, to any person obtaining a copy of the Software     
   and the accompanying documentation is granted permission to use code in parts but not entirely,  
   including the right to use, copy, modify, merge, publish, distribute and/or sublicense it and    
   to provide such rights to persons to whom such software is furnished,                            
   subject to the following conditions:
                         
   The above copyright notice and this permission notice shall be included in all copies or partial 
   copies of the Software.
   The software is provided "as is" without warranty of any kind, either express or implied,        
   including, but not limited to, the warranty of merchantability, fitness for a particular         
   purpose, or non-infringement.
   
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY DAMAGES OR OTHER CLAIMS,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING IN CONNECTION WITH THE SOFTWARE 
OR ANY OTHER USE OF THE SOFTWARE.
```
