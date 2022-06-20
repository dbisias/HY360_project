# CCC Bank *(Files and Databases Project)*

For the project of **Files and Databases**, we created a website for the CCC Bank. In this website wnyone can create an account, and login to manage theri founds. Users and Companies can send or recieve founds to each other.

The assignment details can be found in the [assignment PDF](https://github.com/papastam/HY360_project/blob/master/hy360_project.pdf)

In the `./report` folder, you can find the report of the project. This includes the [entity-relationship diagram](https://github.com/papastam/HY360_project/blob/master/report/entity-relation_diagram.jpg), [all sql queries](https://github.com/papastam/HY360_project/blob/master/report/sql.txt), and screenshots. 

## Build Requirements
* It is recommended that you run the project as a IntelliJ Project!
* The web app requires a database, xampp is recommended.
* For running the project, [JDK](https://www.oracle.com/ie/java/technologies/javase/javase8-archive-downloads.html) is required *(recommended version: jdk1.8.0_241)*
* [Tomcat](https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.58/bin/apache-tomcat-9.0.58.zip) is also required to run the project *(included in xampp)*

## Set up
<h3>Initial Setup</h3>

* The database can be initialized by running: <br>`src/main/java/Database/Init/InitDatabase.java`
<br>
<h3>InteliJ Setup</h3>

+ Add a compile configuration from the rop right menu by clicking the `Add Configuration...`
 button.
+ Select `Tomcat Server -> Remote` from the configurations dropdown menu.
+ Click on `Configure` next to `Application server` selection, and select the tomcat installation directory in the `Tomcat Home` selection.
+ Apply the configuration and run the project!

## Webapp 
The webapp is consisted of theese three pages:

+ Main Page

![Main page screenshot](https://github.com/papastam/HY360_project/blob/master/report/screenshots/frontpage.png?raw=true)

+ Individuals Page *(log in as an individual)*
+ Merchants Page *(log in as a merchant)*
+ Companies Page *(log in as a company)*

![Individuals page screenshot](https://github.com/papastam/HY360_project/blob/master/report/screenshots/individual_frontpage.png?raw=true)

## Co Owners
[Orestis Chiotakis](https://github.com/chiotak0)<br>
[Dimitris Bisias](https://github.com/dbisias)