# CSYE 6225 - Spring 2019

## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
| Shubhankar Dandekar| 001439467| dandekar.s@husky.neu.edu |
| Jayesh Iyer|001472726 | iyer.j@husky.neu.edu|
| Mitali Salvi|001630137  | salvi.mi@husky.neu.edu|
| Neha Gaikwad|001886361 |gaikwad.n@husky.neu.edu |

## Technology Stack
- Java SpringBoot for REST API
- Eclipse IDE
- MariaDB for Database
- Postman to test REST endpoints

## Build Instructions
1. Clone repository
2. Import maven project **restapi** in the **webapp** directory into eclipse
3. Right Click restapi > Maven > Update Maven Project > OK

## Deploy Instructions
1. Create a new database named 'CSYE6225' in MariaDB.
   ```
   create database CSYE6225
   ```
2. Add below mysql user to allow application to connect to the database 
   ```
   grant all privileges on CSYE6225.* to 'csye6225' identified by 'csye6225'
   ```
   ```
   Note : If using different MySQL credentials, you need to input the correct credentials in file webapp/src/main/resources/application.properties
   ```
3. Run Eclipse project imported in above steps as **SpringBoot Application**

## Running Tests
1. Run the restapi project imported in Eclipse as **JUnit Test**

## API Guidelines
1. **Register New user**
   This api registers a new user in the system <br>
   ```
   POST /users {user object}
   ```

2. **Add new note** <br>
   This api creates a new note in the system. <br>
   ```
   POST /note {note Object}
   ```

3. **Retreive notes from the system**
   This api retreives list of all notes available for the logged in user.
   ```
   GET /note
   ```
   This api retreived a particular note authorized for the logged in user.
   ```
   GET /note/{NoteId}
   ```

4. **Update Existing Note** <br>
   This api updates an existing note in the system authorized to the logged in user. <br>
   ```
   PUT /note/{NoteId}
   ```  

5. **Delete Existing Note** <br>
   This api deletes an existing note from the system authorized to the logged in user. <br>
   ```
   DELETE /note/{NoteId}
   ```

## CI/CD


