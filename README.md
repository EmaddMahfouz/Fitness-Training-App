# Fitness-Training-App

###  Overview  
The **Fitness Training App** is a backend service built with **Spring Boot** to process fitness-related events generated by a mobile app. It tracks user interactions, such as app launches, training sessions, and cancellations, while ensuring notifications are sent under specific conditions.

###  Features  
- **Event Processing**: Handles various user activities like app launches, training session starts, cancellations, and completions.  
- **Notification System**: Calls the `/v1/notify` API to send user engagement notifications.  

###  Tech Stack  
- **Backend**: Java, Spring Boot.
- **Database**: MySQL with Spring Data JPA
- **Scheduler**: Spring Scheduler for automated background tasks.
- **API Communication**: REST API using RestTemplate for inter-service interaction.
- **Tools**: Maven, Postman.
- **IDE**: IntelliJ.


### API Endpoints  

#### (1) **Process Event**  
**`POST /v1/event/launch_app`**  
Receives an event object and processes.  
Example request:  
```json
{
  "userID": 1,
  "deviceId": 201
}
```  

#### (2) **Process Event**  
**`PUT /v1/event/update-status`**  
Receives an event object and processes.  
Example request:  
```json
{
  "id": 46,
  "type": "training_program_started",
  "userId": 1
}
```
```json
{
  "id": 46,
  "type": "training_program_finished",
  "userId": 1
}
```
```json
{
  "id": 46,
  "type": "training_program_cancelled",
  "userId": 1
}
```
#### (3) **Send Notification**  
**`POST /v1/notify`**  
Automatically triggered when:  
- A user completes a training program longer than **30 minutes**.  
- A user opens the app but does not start training within **10 minutes**.  
Example request:  
```json
{
  "userEmail": "emadmahfouz8811@gmail.com",
  "message": "Hey! Start your training session now!."
}
```
```json
{
  "userEmail": "emadmahfouz8811@gmail.com",
  "message": "Great job, 1! You’ve completed your daily training with 45 minutes of effort! Keep pushing towards your fitness goals!."
}
```  
### ⚙️ Setup Instructions  

1. **Clone the Repository**  
   ```bash
   git clone https://github.com/EmaddMahfouz/Fitness-Training-App.git
   cd Fitness-Training-App
   ```  

2. **Build and Run the Application**  
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```  

3. **Test the API**  
   Use **Postman** to send requests.  


