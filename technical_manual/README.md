<div align="center">

# TastyTravel Technical Manual

![TastyTravel logo](images/96.png)


## Design Document Highlighting The Initial Design And Current Design.

### <em>Version 1 • March 2020</em>
<br/><br/>
<br/><br/>
<br/><br/>
<br/><br/>
**Michael Savage** - michaelsavage7@mail.dcu.ie  
•  
**Gerard Slowey** - gerard.slowey2@mail.dcu.ie  
</div>

<div align="center">
<br/><br/>
<br/><br/>

---
# Table Of Contents 
<div align="left">
1. [**Introduction** ](#introduction)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.1. [Overview](#overview)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.2. [Glossary](#glossary)<br/><br/>
2. [**System Architecture**](#architecture)<br/><br/>
3. [**High Level Design**](#high-level)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1. [Object Models](#objects)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.2. [Package Diagram](#package)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.3. [Sequence Diagram](#sequence-diagram)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.4. [Data Flow Diagram](#data-flow-diagram)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.5. [Context Data Flow Diagrams](#context-diagram)<br/><br/>
4. [**Problems and Resolutions**](#problems-resolutions)<br/><br/>
5. [**Installation Guide**](#install)  
</div>

<br/>
</div>
<div align="justify"> 

<a name="introduction"></a>
# 1. Introduction  

<a name="overview"></a>
## 1.1. Overview 

TastyTravel is an Android app built with Android Studio 3.6.1.
The app allows one person to search for places for two parties to meet and the given results depend on their chosen mode of transport. 
The aim of the app is to return locations on an interactive Google Map that each party can arrive at roughly at the same time. 
They can choose to search for either Bars, Cafes, or Restaurants.
 
When users of the app successfully search using all the parameters, they can look through at a list of 20 places or less. 
If the user has created an account before searching, their searches will be stored in our Firebase real-time database. 
They can find and delete their search history in the app. We also let users save places that they like and they can find 
and delete their saved places in the app too.

The app finds recommended locations by using Mapbox isochrones and the SphericalUtil class from Google. When the user presses search, 
the app builds a URL that takes in the mode of transport (Walk, Car, or Bike) and requests the JSONObject from MapBox. 
The JSONObject contains information on the properties and geometry of the isochrone, but most importantly we parse the 
coordinates from the URL into a list and use our algorithm to find suitable meeting places. We do this by iterating through the coordinates
list of your location and finding the shortest distance to their location. Then we do the vice versa until we end up with two locations. We then
use interpolation to get a midpoint between those locations where the midpoint is a given score that is influenced by the modes of transport.
The score is a fraction between 0 and 1. The score favours people who walk, then cycle, and finally driving. If the modes of transport are the
same then the fraction will be neutral at 0.5.

By default, the app does not draw the isochrone onto the app but a toggle can be switched on. This switch will add a Google 
GeoJsonLayer to the map using the coordinates list. 
<br></br>
<br></br>
<a name="glossary"></a>
## 1.2. Glossary

|  **Term**     |   Definition  |
| ------------- |:-------------:|
| **Android Studio**    | <div align="left">Android Studio is the official integrated development environment for Google's Android operating system, built on JetBrains' IntelliJ IDEA software and designed specifically for Android development. We are using version 3.6.1.</div>|
| **API**               | <div align="left">_Application Programming Interface_ is an interface between different parts of the programme intended to simplify the implementation of software. </div>|
| **Firebase**          | <div align="left">Firebase includes services like authentication, databases and file storage. We are using The Firebase Realtime Database for data storage and user authentication for account creation and login. The realtime database is a cloud-hosted NoSQL database that lets you store and sync data between your users in real-time.</div>|
| **Interpolate**       | <div align="left">To insert (something of a different nature) into something else. In our case, we use interpolation to return coordinates which lies the given fraction of the way between two other coordinates.</div> |
| **Isochrones**        | <div align="left">A number of lines on a map connecting coordinates relating to the same time or equal times.</div>|
| **Mapbox**            | <div align="left">An open source mapping platform for custom designed maps that contains many APIs and SDKs. Mapbox Studio is like Photoshop, for maps.</div>|
| **REST**              | <div align="left">_REpresentational State Transfer_ is an architectural style for providing standards between computer systems on the web. A client makes a request to the server in order to retrieve or modify data on the server. </div>|
| **Volley**            | <div align="left">Volley is an HTTP library. We use it for fast concurrent network requests to Mapbox.</div>
| **URL**               | <div align="left">Stands for 'Uniform Resource Locator'. Used to specific a web address. We use URL's with the mapbox Isochrone API and Google Places API.</div>


<a name="architecture"></a>
# 2. System Architecture

<div align="center">
<img alt="System Architecture Diagram" src="images/System_Architecture_Diagram.jpg" width="700">
</div>

<br></br>
*System User*
The user interacts with the application through a touch screen android device. 
Location searches are entered by the user along with meet preferences. 
This input influences the end result generated by the serach and sort algorithm and helps determine suitable meeting locations.

Our app uses _Mapbox_ Isochrone APIs to get coordinates of locations which can be reached in a certain time depending on mode of transport. 
The Mapbox API returns its data in JSON format which is parsed to extract the coordinates.
An asynchronous GET request is made to the _Mapbox_ REST API, using the Android Volley HTTP handler library.

We use _Google’s Maps_ API to display a map to the user in various activities.
The API is called customise the map such as adding markers and line overlays to the map, and to change the user's view of a particular map area.

The user also uses _Google Places API_ to search for locations. 
The *AutoCompleteFragment* simplified the user of input of locations, suggesting locations to them based on input. 

We use _Firebase_ for account authentication and for the storage of saved places and search history locations. 
Data in _Firebase_’s real-time database is updated when the user saves search results or makes a search. 
This data is completely under the control of the user and can be deleted in their profile options.
Firebase uses a NoSQL database approach so all reading and writing of data is done through API function calls.

<br></br>
<br></br>
<a name="high-level"></a>
# 3. High Level Design

<a name="objects"></a>
## 3.1. Object Models

<div align="center">
<img alt="History and List Item Object" src="images/HistoryandListItem.PNG">
</div>
<br></br>

The History and List Items are Card Views that the RecyclerView uses to show results. The List Item initialises the name of the place 
and the coordinates of each location. It only displays the text to the user when searching while the coordinates are used when the List
Item is selected.  

The History Item initialises the access date as well as the name and coordinates so that the user can quickly find
when they searched for a location.

<a name="package"></a>
## 3.2. Package Diagram

<div align="center">
<img alt="Package Diagram" src="images/packageDiagram.png" width="700">
</div>
<br></br>

Initially, we thought about creating a class diagram but felt this would be a waste of time since we have 18 Classes. We used a package diagram
to show the relationships between the types of classes. There are 11 activities in our app that represent the different screens. The activities 
work by using the Utils package which includes the JsonParser, UrlBuilder, and MapsWorker which adds the markers to the map.

The activities make use of the RecyclerView which is inside the adapter package. This class needs to be used to get access to the List Item and
History Item.

<a name="sequence-diagram"></a>  
## 3.3. Sequence Diagram

<div align="center">
<img alt="Sequence Diagram" src="images/sequence-diagram.png" width="700">
</div>
<br></br>

In this Sequence Diagram, we show the interaction between objects whenever the user wants to save one of the results they received 
from searching. First the user will open the search page and straight away the activity will prepare the parameters for input.
When the user presses `SEARCH` it starts the openMap() function adding the markers and running the algorithm. When the results are 
returned into a list, they are put into Cards that the user can scroll through. The map calls the recyclerView and checks if the user
is signed in. If they are then, the card view is different as it includes a heart favourite button that gives the user the option to 
save locations.

Whenever all the cards are intialised they are displayed for the user and they can then favourite a place if they choose to.

<br></br>
<br></br>
<a name="data-flow-diagram"></a>
## 3.4. Data Flow Diagram

<div align="center">
<img alt="data flow diagram" src="images/dataFlow.png" width="700">
</div>
<br></br>

The data flow diagram shows how the signed in user and the app can interact at a high level. The user can perform tasks like searching for a meeting place
which then in turn can generate a map, create recommended search results and present the search history and saved places.  

The search history is stored automatically while the saved places relies on the users manual control. The data is stored in Firebase under different headings.
The _History_ child or the _Saved Places_ child.

<a name="context-diagram"></a>
## 3.5. Context Data Flow Diagrams

<div align="center">
<img alt="Context Data Flow Diagram" src="images/Context_Data_Flow.jpg" width="700">
</div>
<br></br>

The following context flow diagram describes the relationship between the system and entities external to the system. They are vitally important.
Each entity is contained within a rectangle and has the entity name in the middle.
The TastyTravel application is contained within a round cornered triangle.
The users interaction with the application causes information to sent to each of these entities which in turn causes a response.


<a name="problems-resolutions"></a>
# 4. Problems and Resolutions
**Selected Place Not Updated When User Selected a New Location**  
We realised that if a place was searched for and selected using the maps AutoCompleteFragment and 
the user changed their mind and searched for a new location the old location remained selected and saved to the ArrayList. 
To solve this problem, instead of immediately saving the place to the ArrayList of places we assigned the Place 
object to a temporary Place object yourSelectedLocation and theirSelectedLocation.
Only when the user was happy with their chosen search parameters when they hit the `SEARCH` button was the 
Place objects saved in the list and passed to the next activity.
This allowed the user to change the locations multiple times and only the final chosen place would be used.

**Search Results Save Icon in Recycler View Falsely Showing Checked Toggle Button**  
Due to the nature of the Android RecyclerView it doesn't save the scroll position of the items being displayed. 
If a toggle button was clicked on one of the cardviews being displayed in the recycler view this resulted in every 8th 
toggle button after that cardview showing as selected also, when in fact this was not the case.
To overcome this we used a SparseBooleanArray to keep track of the states of the toggle buttons attached to each card in the recyler view.
The SparseBooleanArray simply mapped the integer representing the position of the card in the recyler view to a boolean (true or false) 
indicating whether the toggle was checked or not.

**Search Result Would Return Empty List Because The First Index Wasn't Considered**  
There was a bug that was caught late in development where a string was initialised to the first coordinates in a list. The string was
supposed to be used as a comparison against all the other coordinates. Instead, we had reassigned the second coordinates onto the
first coordinates meaning the first coordinates were never used. We spent an hour going through logs to find the error. A simple change
to the variable names.

<br></br>
<br></br>

<a name="install"></a>
# 5. Installation Guide

### Foreword  
An active internet connection is required to use this application.

### APK Version:  
1.0

### Software and Hardware Requirements
This application is designed to run on the *Android* mobile operating system and is only compatible with 
devices running Android *version 5.0 (SDK version 21) or newer*.  
Since the application will be installed on the internal storage of the Android device, some free space will be required. Installation size is roughly around 6 MB.

### Installation Preconditions:
* An Android device will be required to install the TastyTravel application on. This device must have USB debugging enabled. 
* USB debugging is a setting available under the 'Developer Options' menu. If you are unfamililar with enabling developer options 
please read [this](https://www.howtogeek.com/129728/how-to-access-the-developer-options-menu-and-enable-usb-debugging-on-android-4.2/) article.

<div align="center">
<img alt="debug photo" src="images/debug.png" width="300">
</div>

### Installation Instructions
1. To open the Android Studio code files and install the application on an Android device, a laptop or computer with Android Studio installed will be required.
The application source code can be accessed and downloaded from [here](https://gitlab.computing.dcu.ie/savagem7/2020-ca326-msavage-tastytravel).
2. Once you have a local copy of the source code import it into Android Studio.
3. Next, connect your Android device with USB debugging enable using a suitable USB cable.
4. When Android Studio has loaded the project files and detected the connected device, click the green triangle button making sure to 
select the correct device for application deployment. The application will now be installed on the connected device. 
More details can be found at this [link](https://developer.android.com/studio/run/device) if you are encountering difficulties.
5. A confirmation message should be displayed when Android Studio has installed the application onto the device successfully.
The application can now be used and it's functionality explored.

<div align="center">
<img alt="install photo" src="images/install.png" width="700">
</div>

<br></br>
<br></br>
