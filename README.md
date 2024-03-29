# Simple Checklist Manager
Simple Checklist Manager is a minimal checklist manager. SCM is designed to be a tool that you can use for simple to do lists, habits, long term goals, 
or anything you might imagine using a checklist for.

Example of a wide variety of lists.  
![Alt text](https://user-images.githubusercontent.com/8732563/123871678-97ca3580-d8f9-11eb-8ec6-a46d577697e5.png "New Checklist Manager Template")  
Bare bones implementation using the default categories.  
![Alt text](https://i.gyazo.com/4caecb0ba7eab392b6230353393ab2f4.png "Exmample checklists")  

## Features
- Create/Edit/Delete/Move Categories
  - Categories have 2 types
    - Once
      - The tasks added to this category will be deleted the next day if they are checked. 
    - Persistent
      - The tasks added to this category won't be deleted regardless if checked or not. 
  - You can create multiple categories by separating categories with commas.
- Create/Edit/Delete/Move Tasks
  - As mentioned above tasks will behave relative to the category they are placed in. Once tasks will be deleted the next day automatically when checked. 
  - You can create multiple tasks within one category by separating tasks with commas.
- Right Click Menu
  - Create Tasks and Categories (Same as Edit Menu)
  - Change category type
  - Edit task or category
  - Move task or category
  - Delete task or category
- File Menu
  - New Checklist Manager
    - Deletes everything and replaces it with a default template.
  - Refresh
    - Refreshes the date and updates tasks. There won't be a change in date or tasks unless it is a new day since the last time you refreshed or ran the program.
- Edit Menu
  - Create tasks and categories.
- Tools Menu
  - Dark Mode
    - Toggles dark mode on or off.

## Download
The current version can be found here:
https://github.com/TylerKimbell/SimpleChecklistManager/releases
Also it is a jar file so you need the Java JDK to run it. https://www.oracle.com/java/technologies/javase-jdk16-downloads.html
