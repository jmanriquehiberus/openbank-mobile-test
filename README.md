# Openbank mobile test

This android application written in kotlin and based upon clean architecture, makes use of the marvel API to display a list of characters belonging to the marvel universe. In addition to that, it allows the user to see
further information of each of these characters by tapping on their element on the list, as well as a detailed description of them available on the internet and linked to the character detail in a beautifully displayed manner.

## Screens

<pre><p align="center"><img src="https://user-images.githubusercontent.com/72376438/122182175-557d0f00-ce8a-11eb-86f7-8f8d9f1ebf63.png" width="198">        <img src="https://user-images.githubusercontent.com/72376438/122182929-0683a980-ce8b-11eb-9e66-501d839e957f.png" width="198">       <img src="https://user-images.githubusercontent.com/72376438/122183446-8873d280-ce8b-11eb-800c-dc85608f8614.png" width="198">       <img src="https://user-images.githubusercontent.com/72376438/122233299-3ac38e00-cebc-11eb-89a3-28922f45a9da.png" width="198">
</p></pre>
<pre><p align="center"><img src="https://user-images.githubusercontent.com/72376438/122233799-a4dc3300-cebc-11eb-9ff4-04aa84a855e4.png" width="198">        <img src="https://user-images.githubusercontent.com/72376438/122233817-a7d72380-cebc-11eb-9f2f-25ee6874f605.png" width="198">
</p></pre>

## Functionality

The app displays a list of marvel characters on its main screen, the user can either refresh the screen performing a pull to refresh gesture, or access the detail of a character by tapping on it.
In case an error occurs while retrieving data, an error screen with a retry button and the information related to the error will be displayed.
Whenever a pull to refresh or a retry after an error is performed, a call to retrieve data is forced to the API, bypassing the memory of the application as it will described later on in further detail.

The detail screen contains the name and description of the character, the number of series, stories and comics where the character appears, as well as links to the complete biography of the character and its appearances on series, stories and comics on the "Find out more" section. Those two types of links will be opened on a webview screen right after the user taps on its related UI components.
If there is no internet connection a snackbar will be displayed on screen notifying the user while preventing the webview from opening.

## Marvel API

This API retrieves a list of characters that are displayed in the application, retrofit has been chosen as the library in charge of performing the remote calls to the endpoints and do the job. In order to retrieve data from those endpoints, it is necessary to first create an account on the marvel website, right after that, it will be necessary to obtain both, a public key and a private key.

Once with the keys, it will be necessary to form a hash with the md5 conversion of the result of joining a timestamp, the private key and the public key we previously obtained. Right after getting the hash, we will need to send the timestamp, the public key and the hash as parameters of every endpoint in order to authenticate ourselves and obtain access.

## Architecture

The application was developed following clean architecture practices in order to create a testable, scalable and easy to work on solution. It is divided in 5 diferent layers, these are:

### App / Presenter

This layer encompasses the entire UI of the application and implements the Model-View-ViewModel design pattern.

Once the application loads, the splash activity is displayed for a while to finally navigate towards the main activity, once there, the recyclerview fragment is loaded within its container.

#### Navigation

The navigation between activities takes place thanks to a custom component located in the common/navigation package that utilizes startActivity().
On the other hand, the navigation between fragments takes place differently, using the NavComponent of the fragment itself to navigate towards the destination provided by the action observed.
This is possible because of the observation of viewmodel live data from the main activity, once we tap on an element of the list, we make a call to the viewmodel to fire the action of navigation towards the detail fragment that in turn triggers the navigate method of the NavComponent located on the main activity.

#### Load of data

When it comes to the load of data into the UI, we make a call to the viewmodel when the fragment initializes, right after that, the viewmodel performs a call to the upper layers to retrieve data, since we are observing the live data of the response from the fragment itself, once we obtain a response, the UI is setup depending on the result.

#### UI states

There are 3 states which a fragment can be in, loading, success and error.
Once the application performs the call, the state of the screen is set to loading.
Right after getting the response the state of the screen will be set to either success or error.
If the state is success, we fill the screen with the data obtained.
If the state is error, we display an error screen with information of the related error and an error support code, as well as displaying a button to retry the formerly failed operation.

#### Reactive programming

In order to retrieve data from the upper layers and respond asynchronously, it is necessary to implement observers in the UI, the purpose behind this idea is to have a listener of the live data related to the use case at hand, that will be triggered once the live data has changed its state. This live data will contain the response of the upper layers and will be set as a result of the action performed by the use case located in the domain layer and called by the viewmodel.


### Domain

The domain layer contains the use cases of the application, there is a buildUseCaseObservable() on each of the use cases, this method is triggered when the execute() method of the use case's instance is called and takes place on a different thread. The instance that is going to be retrieved by both use cases in this application, is that of an observable, in both cases, a Single type object.
The use case implements an interface that acts as a repository for all use cases in the layer, this interface will be implemented in the data layer.
Regardless of the use case, the parameters sent to the data layer through the repository interface is passed from the viewmodel to the use case within a inner container class that will always have a forceRemote parameter, the importance of this will be described along with the data layer.

### Data

This layer acts as an intermediate step between the domain and the datasources layer, the purpose of its existence is to deflect calls to either the memory of the application or the marvel API.
There at the implementation of the domain repository interface, the forceRemote parameter will be checked before performing a call through the data repository interface to its datasources layer implementation.
If the forceRemote parameter is false, we will check the application's memory first and if we obtain no results, a call to the marvel API through the datasources layer will be performed, otherwise, the data from the memory will be given back all the way down to the app / presenter layer.
If the forceRemote parameter is true, we will clear the data from the memory and perform a call to the marvel API straight away.

### Datasources

The datasources layer encompasses the means of communication with both, the memory of the application and the marvel API.
The memory of the application is a sole class that keeps the instances alive of the previously retrieved data from the API and it is accessed to through the implementation of the data layer repository interface which implementations access the memory store and retrieve data to be returned to the app / presenter layer.
The remote package of the datasources layer implements the data layer repository interface and performs calls to the retrofit service that contains the marvel API endpoints.
Once a call has been performed, the service function that calls to the endpoint at hand returns a response object, there at the datasources implemented method from the data layer repository interface, we use a custom mapper to transform the response object into a list of models of type Character, this list will be wrapped into a single observable that will be finally returned all the way down to the app / presenter layer.

### Model

This layer contains all the entity models used throughout the layers.

## Dependency injection

In order to have at our disposal all the required objects instanciated and available, it is necessary to inject them in Koin.
Koin is the dependency injector that has been used in this test for this clean architecture to work.
The location of the koin modules can be found within the app / presenter layer, at the common/di/AppModules.kt file.
The setup of koin can be found at the OpenbankMobileTestApplication application class referenced from the AndroidManifest.xml

## Error handling

Every viewmodel has an error builder attached to process the response errors that are obtained, some of them come from the API, others don't.
In any case, all errors coming from the API are mapped and just like any other error, form an error bundle on the error builder.
There is an AppAction enumeration for every use case in the app, and an AppError for every possible error, those are:

Unknown (-1)
General error (1)
No internet (2)
Timeout (3)
No route to host (4)
Unauthorized (401)
Forbidden (403)
Not found (404)
Not allowed (405)
Conflict (409)

Error bundles contain the error detail, the error support code and the AppAction indicating the use case they are related to in order to ease possible future error debugging.

## Testing

Testing is implemented on each and any of the architecture's layers for one of the two endpoints, the one that retrieves the list of characters.
The code flow is tested straight from the fragment all the way up to the datasources layer.

### App / Presenter

Tests on the presenter layer are instrumented tests, as they require a device either physical or emulated to run, in those tests, the fragment and the viewmodel are both mocked and their lifecycles are emulated.
When it comes to the fragment, it is first tested whether the activity finds any problem launching, then the display of information of characters tested and lastly another test is run to check whether the list is scrollable or not.
On the other hand, tests on the viewmodel are performed in order to check the proper working of the retrieval of data and of all the 3 states, loading success and error. In any case, the expected response is mocked and the method with a call to the upper layers is stubbed so that whenever it is called and there is a response, it is bypassed and replaced with the previously mocked instance.

### Domain

Tests on domain are unit tests and the purpose of them is to make sure that the methods from the repository retrieve the proper data, the response is mocked and the method is stubbed, so that once the process has come to an end, if the result was successful the mocked response will be returned, otherwise, the test will fail.

### Data

Tests on domain are unit tests and the purpose of them is to make sure that both, the calls to the memory and the calls to the repository work properly, the way to perform those tests takes place in the same way as it does for domain, the response is mocked, and call to datasources is stubbed and the result is asserted for the test to succeed

### Datasources

Tests on this layer are unit tests as well, their purpose is to make sure that the ultimate layer of communication works well, in order to do so, the retrofit service is mocked as well as the memory, calls are performed after their methods have been stubbed, and the response is asserted with the mocked one that was created before, all parts of the process are tested to ensure the performance of the layer and the application as well.

Being able to test each and any layer of the application separately, allows the developer to save a great amount of time whenever an error comes, by simply running the tests that were previously created, it becomes easy to spot the problem whereas debugging from scratch would have been an extra amount of time and resources, easily spared by the early implementation of tests throughout the application.
