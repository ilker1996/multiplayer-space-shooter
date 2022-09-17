## 2D Shooting Game

- This is a 2D alien shooting game for our class CENG453
- It includes both single player and multiplayer

## Database
You should create database tables using SQL queries in the
**/backend/User.sql**  and **/backend/Score.sql**.

You should fill your database credentials in **/backend/src/main/resources/application.properties**
## Unit Tests

You can find the unit tests of the servers in **/test** folder of the related folders(e.g. multiplayer , backend)

## GUI Test cases

GUI test cases are in the $ROOT/frontend/GUITest.docx

## How to extract executables

Extract frontend.jar
```bash
cd $ROOT/frontend/
mvn clean compile assembly:single
```

Extract backend.war
```bash
cd $ROOT/backend/
mvn clean package
```

Extract multiplayer.war
```bash
cd $ROOT/multiplayer/
mvn clean package
```

Copy executable files to the executable folder
```bash
cp $ROOT/frontend/target/frontend.jar $ROOT/executables/
cp $ROOT/backend/target/backend.war $ROOT/executables/
cp $ROOT/multiplayer/target/multiplayer.war $ROOT/executables/
```

## How to run

Run server applications in any server

Also note that, backend and multiplayer application is using port ```8080```
and ```8082``` respectively

```bash
java -jar executables/backend.war
java -jar executables/multiplayer.war
```
Then , execute frontend application in your local computer or anywhere else 
with internet connection
```bash
java -jar executables/frontend.jar
```

In the first screen , give backend and multiplayer applications' location as 
server address 

For example if you execute server applications in your local computer:

Backend server address = http://localhost:8080

Matcher(multiplayer server) server address = http://localhost:8082



After successful sign up and login you can play single and multiplayer 
with ```LEFT``` and ```RIGHT``` buttons on your keyboard.
Additionally, you can see your highest scores in the leaderboard.

 For more detailed information about GUI please refer to ```/frontend/GUITest.docx```
