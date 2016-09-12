#   This project is made by Ye Hua (Edward) Tan in July, 2016. The assignment
# is entirely # purposed for grading of Assignment 3 of Spring 2016, CS349,
# at University of Waterloo. Acknowledgement goes to University of Waterloo
# CS349 lecture materials and staff, CS349 anonymous students who actively
# answer questions on Piazza, Java Library documents, and android
# documentations. You’re welcome to use it as a reference # material.

#   This document will summarize all required specs of this software and the
#fundamental # use. Also discuss some design decisions made in this software
#that fix some pre-exiting # issue. It also includes some unimplemented
#feature that’s critical to a real software, but neglected by the
#Assignment’s requirement.

CrowdCurio

1.0 Introduction
  
  CrowdCurio is a crowdsourcing platform that coordinates contributions from
people to support scientific discoveries, medical data processing, humanities 
research and technological innovations, as well as an experimentation framework for 
studying research questions in human-computer interaction, machine learning, 
crowdsourcing, and citizen science.

  The goal of this project is to support mobile application for CrowdCurio.
The application consist HTTP url request from the website to gain external data using
internet, thus internet connection is fairly important in this project.

  CrowdCurio Mobile uses latest android 6.0 that support up to API 24. The user interface
of this application includes several advanced UI techniques such as nested fragments and
enhanced app nevigation with master-detail view as well as sibling views. The app also 
imports different gestures to support user experience.

2.0 System requirement and Installation

2.1 System requirements

	Device: Nexsus 6
	Screen Resolution: 1440x2560
	Size:5.96"
	Density: 560dpi

	Minimum OS requirement: Android 6.0 Marshmallow or above
	Other Requirement:
		- Java SE 1.8.0.91
		- Android API 23

2.2 Installation
	** Please following the steps CAREFULLY

	Import Project:
		1. start Android Studio
		2. select "open existing project" and find the directory
		3. the project should be imported and build by the android studio

	Virtual Device Setting:
		** The app has only been asked to test on virtual device, it
		** is best to use an emulator for best confidence.

		1. Go to ADV manager.
		2. Clock on Create Virtual Device
		3. Select "Phone" and next.
		4. Select Marshmallow x86.
		5. Disable Device Frame
		6. Complete.

2.3 Testing Requirements

  When testing the application, please avoid using physical inputs (etc. keyboard), 
many features are implemented on soft key board only and does not apply with hardware
keyboard.

3.0 Key Features

	Agenda：
	1. Master-Detail Flow, Project Lists
	2. Search Bar, Search Type, Clear Search and other Search related contents
	3. Add/Delete Favourite Project, Favourite Filtering.
	4. Refresh functionality.
	5. Detail Activity, Sibling Views.


3.1 Master-Detail Flow, Project Lists

  The main nevigation flow of the app is based on Master-Detail flow, where user
are provided with list of different contents and each contains a smaller content
that will expand on the title in a greate detail. 

  On master flow, each item consist of the following three attribute:
	1. Preview Image
	2. Project Owner
	3. Project Title

  By quick tapping on each item, user are brought to the detail view.

  Some projects are marked unavailable on the API, these projects are still being 
displayed, but user will get a toast if they try to access it, as it will not
brougt user to the detail view.

  Upon any loading or downloading request, there will be a spinner at the side
indicates that resources are being download. This indicator will not halt the 
program and continues to allow user to navigates the app. 
  However there are some inconsistency with the spinner showing due to the 
downloading pattern of the images. 

3.2 Search Bar, Search Type, and other Search function

  Search bar is provided to the user between item list and the title bar. Search bar contains
search field, clear input button and search type.

  User should always use soft keyboard to construct there search input. For consistency,
the input field only accept alphanumeric values and space. To apply search, user will have to
use the "search" button shown on their soft keyboard. The "search" key however only appears if 
user enters from legal input (ie not hardware keyboard).

  If user apply search on empty entry, the list of all projects will be shown as result. Because 
of this, a clear entry key is provided beisde the entry field, if user request all project list 
after any search request. (Originally used to get all project onclick)

  Finally, search type drop down list provide specific fields to search for (ie name or description).
The default would be the name.

3.3 Add/Delete Favourite Project (Used for bonus)

  The app provides Favourite filtering to allow user to add and delete project from their favourite
filtering list. To add/delete, user may have to longtap the list item in master view. 
  A short toast will be display on whether the user added or deleted a project from the filter list.

  To enale favourite filtering, click on the floating button with the star to filter out the favourite
project out of the current content. If user made a search, filter favourite will display only the 
favourite among the search results. To revert back to show all projects, use refresh button (next section).

3.4 Refresh

  Refresh is the action button that will send GET request from the url once again. This will result reloading
data and redownloading images. 
  If user made applied a search, refresh will only refresh the current content of the previous search. 
  However refresh will revert and favourite filtering. 

3.5 Detail Activity/Sibiling View

  The Detail Activity consist of top banner, which includes the image and the title. Below the bannner is a 
three tabbed pages that allows user to swap between the 3 sections of detail:
	- Description : use to provide greater content about the project
	- Questions: show all related questions (upper sentence)  regards to this project, as well as the 
		motivation (lower text) related to the question.
	- Team members: Information about the team members, including avatar, names and emails.

  There is a title slider above the content that will keep track of which tab is currently active and the tabs 
beside the current tab.

  Finally, we've also provided a refresh button in detail view to help user retrieve new server data.

3.6 Orientation

  The application supports both landscape and portrait mode and all content should be gracefully handled. 
However due to device issue, some orientatino changes may not be sensitive and requires a few millisecond 
of respond. 

3.7 Other issues
 
- On some projects, the anmation will become sloppy by some chance. My suspect is that the Bitmap for the banner
image differs between projects, and some project has condensed bitmap that slows the rendering on some activities.

- When entry field gain focus and the soft keyboard appears, there is no explicit way to collapse it unless:
	1. User finished an entry.
	2. Using Device Native action buttons below.
  Currently there is no clean and precis 

- The majority of the text are in white, however some image does not constrast well with white, resulting bad 
 visibility with the title and the return button on the detail view.
