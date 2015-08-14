Emtec Remote is an application that turns your Android phone into a remote control for your Emtec MovieCube.

The remote control application features an intuitive user interface, optimized for touch screen devices. The user interface contains navigation buttons, volume control, playback controls, color buttons and an easy to use numeric scroll pad. Specific development effort is made to ensure the user interface adapts to all screen resolutions, either in landscape or in portrait orientation.

Communication with the Movie Cube is done over Wi-Fi using the MovieCube’s host name and communication. The communication parameters are configurable. To enhance ease of use, the application features a configuration checklist for both the Movie Cube and the application. The connection with the Movie Cube is ensured by automatic verification of the application’s connectivity and guidelines to resolve possible issues. The configuration checklist is also shown to the user when starting the application for the first time to make sure the user is able to use the application.

Under the hood, the user interface and communication run in separate threads. A lightweight multi-threaded communication service is used to ensure fast communication with the MovieCube, while maintaining a responsive GUI. It is tried to optimize the code for the implementation of future application extensions (e.g., batch commands, firmware upgrade checks, remote recorder scheduling ...).

The app was tested on a HTC Hero (Android 2.1), a Samsung Galaxy Tab 10.1 (Android 3.1), a Samsung Nexus (Android 4.0.1) and several virtual devices featuring other versions of the Android platform. During development an Emtec S800H Movie Cube is used with firmware versions from [r5389](https://code.google.com/p/emtec-remote/source/detail?r=5389) onwards.

Feel free to use and contribute.