# Openbank mobile test

This android application written in kotlin and based upon clean architecture, makes use of the marvel API to display a list of characters belonging to the marvel universe. In addition to that, it allows the user to see
further information of each of these characters by tapping on their element on the list, as well as a detailed description of them available on the internet and linked to the character detail in a beautifully displayed manner.

## Screens

<pre><p align="center"><img src="https://user-images.githubusercontent.com/72376438/122182175-557d0f00-ce8a-11eb-86f7-8f8d9f1ebf63.png" width="198">        <img src="https://user-images.githubusercontent.com/72376438/122182929-0683a980-ce8b-11eb-9e66-501d839e957f.png" width="198">       <img src="https://user-images.githubusercontent.com/72376438/122183446-8873d280-ce8b-11eb-800c-dc85608f8614.png" width="198">       <img src="https://user-images.githubusercontent.com/72376438/122233299-3ac38e00-cebc-11eb-89a3-28922f45a9da.png" width="198">
</p></pre>
<pre><p align="center"><img src="https://user-images.githubusercontent.com/72376438/122233799-a4dc3300-cebc-11eb-9ff4-04aa84a855e4.png" width="198">        <img src="https://user-images.githubusercontent.com/72376438/122233817-a7d72380-cebc-11eb-9f2f-25ee6874f605.png" width="198">
</p></pre>

## Architecture

The application was developed following clean architecture practices in order to create a testable, scalable and easy to work on solution. It is divided in 4 diferent modules, these are:

### App / Presenter

This layer encompasses the entire UI of the application and implements the Model-View-ViewModel design pattern. Once the application loads, the splash activity is displayed and navigates after a while to the main activity, once there, the recyclerview fragment is loaded within its container.

The navigation between activities takes place thanks to a custom component located in the common/navigation package that utilizes startActivity().
On the other hand, the navigation between fragments takes place differently, using the NavComponent of the fragment itself to navigate towards the destination provided by the action observed.
This is possible because of the observation of viewmodel live data from the main activity, once we tap on an element of the list, we make a call to the viewmodel to fire the action of navigation towards the detail fragment that in turn triggers the navigate method of the NavComponent located on the main activity.

When it comes to the load of data into the UI, we make a call to the viewmodel when the fragment initializes, right after that, the viewmodel performs a call to the upper layers to retrieve data, since we are observing from the fragment the live data of the response, once it takes place, the UI is setup depending on the result.
There are 3 states which a fragment can be in, loading, success and error.

Once the application performs the call the state of the screen is set to loading.
Right after getting the response the state of the screen will be set to either success or error.
If the state is success, we fill the screen with the data obtained.
If the state is error, we display an error screen with information of the related error and an error support code, as well as displaying a button to retry the formerly failed operation.


