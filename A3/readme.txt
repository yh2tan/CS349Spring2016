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

PortableSketch

1.0 Introduction

	PortableSketch is a mobile version of JSketch (see JSketch 
readme.txt) that runs on android OS. Although many desktop features has been 
removed, it included unique features only available for Android. MobileSketch 
also has potentially improved accuracy and efficiency in compare to JSketch 
(As it was done the night before).
	The application includes key features in JSketch such as drawing, 
repositioning, editing fundamental shapes. Although the application does not 
include resizing windows, portable toolbars,and saving/loading (all windows 
feature), it include new undo and redo features and support both portrait and 
landscape mode.
	Currently this application only compatible with Nexus7, with API23 
(Android 6.0). Please be aware when using.

2.0 System/Hardware requirement and Installation

	2.1 System/Hardware requirement

		Supported Device: Nexus 7
		Screen Solution: 1200x1920
		Size: 7.02”
		Density: xhdpi
		
		Minimum OS requirement: Android 6.0 Marshmallow or above
		Other requirements: 
			- Java SE 1.8.0.91
			- Android API 23

	2.2 Installation
		** Please follow the steps CAREFULLY!

		Import Project to Android Studio:
			1. start Android Studio
			2. select “open existing project”
			3. the project should be built by Android Studio 
			itself.
		
		Configure correct virtual device:
			** As this software was only fully tested on 	
			** emulators, it is advised to use a virtual machine.

			1. Go to ADV manager (at the toolbar above)
			2. Click on Create Virtual Device
			3. Select “Tablet” tab
			4. Select Nexus7 (not Nexus7 2012) and next.
			5. In System Image, choose Marshmallow x86 (Download
			   it if needed)
			6. Set default in Portrait (or Landscape)
			7. Disable Device Frame (VERY IMPORTANT!!!)
			8. Finish

		Now, you can simply run the project on the selected ADV.

3.0 Key Features

	This section will discuss all features in details, below is the included features:

	1. Portrait/Landscape mode
	2. Tool Bar/Color Pallet/Line Thickness
	3. Shape editing
	4. Layering
	5. Utility Buttons

	3.1 Portrait/Landscape mode
		Rotating the device will result the two different layout 
	display for this app, 	however in either of these mode, all features 
	should be included. The draw canvas 	would be rotated to the correct 
	display automatically during orientation change and 	all other UI 
	components should be repositioned accordingly. Additionally, 
	orientation will maintain the state of the application before the 
	orientation occurs.

	3.2 Tool Bar/Color Pallet/LineThickness Bar
		Tool Bar consist of the following basic features, user’s touch 
	event on the draw cavan will be reflected base on the tool bar’s 	
	current selection. Selected item will be appeared in Cyan. Below are 
	the 6 tools included:

	Selection: This tool features shape selecting, shape reposition and 
		shape editing. Selected shapes will be coated with gray and 
		white border. If user tapped anywhere that contains no shape,
 		it will unselect any selected shape.
	Line: Draw Line. 
	Rectangle: Draw an unfilled rectangle with colored border.
	Circle: Draw an unfilled circle with colored border. The size of the 
		shape is only determined by the displacement on x axis, 
		meaning the drawing animation would not occur if there is 
		vertical movements
	Paint: Fill the interior areas of a shape (only on rectangles and 
		circle).
	Erase: Remove the selected shape.

	** For ordering of the shape when selecting/painting/erasing, see 
	Layer section.

		Color Pallet selects a color, the next shape created/painted 
	will be based on the currently selected color. User can edit the 
	color(border) of an existing shape using Selection Tool (see Shape 
	editing section).

		Line Thickness Bar selects a stroke, the next shape created 
	will have border size of the selected thickness. User can edit the 
	border size of an existing shape using Selection Tool (see shape 
	editing section).

	3.3 Shape Editing
		Shape editing is featured by Selection tool, user can edit the 
	following of the selected shape:
			- Position
			- Border Color
			- Border Thickness
	
		Also, when using selects the shape, the color pallet and 
	stroke bar will reflect
	one the attribute of that shape. 
		Editing the interior color of the shape is done by using 	
	paint.

	3.4 Layering
		The painting order of the shape comes in LIFO, where the 
	shapes that’s created at last will be painted a top of all shapes.
		The selection order of the shape comes is LIFO as well, where 	
	the shape appears to be at top will get selected. 
	
	** If drawing three circles in increasing size, the last circle will 	
	always get selected despite the visibility of the inner circle.

		Any Shape editing tools will not change the layering property. 

	3.5 Utility Buttons
		
		The application comes with 3 buttons in tool bar to support 	
	more features:
		1. Redo button
		2. Undo button
		3. Clear

		3.5.1 Undo
			Undo will rewind the current canvas by one user action
		on the draw canvas. These actions includes:
			- Creating new Shapes
			- Reposition any Shapes
			- Repainting any Shapes
			- Change the border for any shapes (color and strokes)
			- Deleting any shapes
		For demo purposes, it only support up to 10 undos.

		3.5.2 Redo
			Redo will rewind if there is a undo action made by 
		user. If any new action occurred(that’s not a undo or redo), 
		any redo history will be cleaned. 
			Since the app only supports 10 undo, it can only revert 
		up to 10 redo.
		
		3.5.3 Clear
			Clear button cleans the canvas. Note that it also 
		cleans any cached undo and redo histories.

4.0 Existing Problems
	Below are any problems that I am cautioned about that’s not included in the
 requirements. These should be fixed in the future:
	1. OnResume()/OnRestart() has not been implemented. Sometimes 
	 Rebuilding the project on an old device will call OnResume(), which 
	 could cause display problems (where multiple buttons gets selected, 
	 this recovers if user select the default button that shouldn’t be 
	 selected).

	2. Selection on extremely steep line are performed better than 
	 JSketch, but the problem still exits on some extremes.



  

		

	

		



