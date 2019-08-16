# Weather App
Weather App using Kotlin to Android

##### Changelog:
- Initialized Google Maps as default Google Maps Activity
- Adding Search Button on **activity_maps.xml** layout
- Enable user location to display Map
- Enable request permission to ACCESS_FINE_LOCATION
- Adding map click to add marker
- Getting marker position to ability search 15 TOP cities
- Adding CitiesActivity
- Adjust CitiesActivity
- Navite to CitiesActivity passing User Location Information using extras
- Adding Internet Permission
- Adding Anko library
- Using doAsync to make a request API call
- Using JSON library to convert response from API to JSON
- Getting information from API
- Creating *City, Coordinate and Weather* class
- Updating to get always just one pine
- Creating cities list to get top 15 cities
- Created **CityAdapter** using ViewHolder
- Created **RecycleView** to CitiesList
- Displaying all cities as Recycle View
- Adding progress bar
- Changing MapActivity name
- Enable back button action
- Include **gson** and **Picasso** libraries
- Adding DetailsCityActivity
- Setup to navigate to DetailsCityActitivty from CitiesActivity select a RecycleView item
- Enable *City and Weather* as Serializable to passing in extras intent
- Using SharedPreference as safety storage and by-pass variable
- Put a toast to force user to always click on Map before go to CitiesActivity
- Adding deep link in **MainActivity**
- Merge with *sdk_inloco* branch to integrate with SDK InLoco 

###### Improvements:
- [x] Create User Coordination class
- [x] Get user location (coordinates) after add marker
- [x] Create safe getExtra information
- [x] Clean all unused imports
- [] Change to Android JetPack archicture