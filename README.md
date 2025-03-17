# Patient_Medicine_AppointmentSystem_Application_Project_2
1. Project Overview
This Patient Medicine and Appointment System is built with:
Spring Boot (for backend & REST APIs)
Spring Security (for authentication & authorization)
Thymeleaf + Bootstrap (for frontend templates)
MySQL (relational database)
The system manages Users (Admins, Doctors, Patients), Appointments, Medications, and more.

2. Prerequisites & Requirements
Java 17+ (or the version specified in your pom.xml or build.gradle)
Maven (depending on your project) to build
MySQL database running locally or accessible via network
IDE (IntelliJ etc.) or a command line to run the application

3. Setting Up & Configuring
3.1. Clone or Download
Clone this repository or download the ZIP
Unzip (if necessary) and open in your IDE or place in a folder
3.2. Database Configuration
Open src/main/resources/application.properties (or .yml) and update to match your local MySQL setup:

spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

# (Optional) For debug logging
spring.jpa.show-sql=true

# (Optional) For schema generation
spring.jpa.hibernate.ddl-auto=update

ddl-auto=update automatically creates/updates tables. For production, consider validate or manual migrations.
3.3. Build & Dependencies
If using Maven, run:

mvn clean install


Ensure it resolves dependencies (Spring Boot, Spring Security, Thymeleaf, etc.).

4. Running the Application
In your IDE: run the main class
PatientMedicineAndAppointmentSystemApplication (or whichever is annotated with @SpringBootApplication).
Or via command line:

mvn spring-boot:run

java -jar target/Patient-Medicine-and-Appointment-System.jar


Verify: The app starts on http://localhost:8080 (by default).

5. Accessing the Application
http://localhost:8080/login for the login page
http://localhost:8080/register for user registration
http://localhost:8080/forgot-password for the forgot-password form
Admin Dashboard: http://localhost:8080/admin/dashboard
Doctor Dashboard: http://localhost:8080/doctor/dashboard
Patient Dashboard: http://localhost:8080/patient/dashboard
Depending on your role (ADMIN, DOCTOR, PATIENT), you’ll be redirected to the respective dashboard upon login.

6. API Endpoints & Basic Flows
Below is a simplified list of key endpoints, showing request/response and validation rules.
6.1. Authentication & Registration
POST /login
Request: form fields username (email), password
Response: If valid, user is authenticated & redirected to /dashboard. If invalid, returns error on login page.
GET /register
Renders a form to pick a role (Admin/Doctor/Patient) and fill out name, email, password, etc.
POST /register
Request (form-encoded):
json
Copy
{
  "name": "Karthik",
  "email": "karthik@gmail.com",
  "password": "mypassword",
  "role": "PATIENT",
  "phone": "...",       // if patient/doctor
  "gender": "...",      // if patient
  "medicalHistory": "...", // if patient
  "specialization": "..."  // if doctor
}


Response: redirects to /login on success, or shows errors if email is already in use, etc.
Validation:
name and email cannot be blank
password must be non-empty
role must be one of [ADMIN, DOCTOR, PATIENT]
6.2. Forgot/Reset Password
(If you are manually resetting, skip the token approach. If you’re using a token-based approach, see below.)
GET /forgot-password
Renders a form to enter email.
POST /forgot-password
Request: email
Response: If email exists, generate a token, store it, optionally send an email. Show success message. If no user found, show error.
GET /reset-password?token=XYZ&email=someone@example.com
Validates the token, if valid shows a form for new password.
POST /reset-password
Request: token, email, newPassword, confirmPassword
Validation: newPassword must match confirmPassword.
Action: updates the user’s password in DB, clears token.

6.3. Admin Endpoints
GET /admin/dashboard
Renders an Admin overview page with links to manage doctors, patients, appointments.
GET /admin/doctors
Shows a table of all doctors + a form to add a new one.
POST /admin/doctors
Creates a new doctor (also creates user with role=DOCTOR).
Validation: fullName, email, password, specialization, etc.
POST /admin/doctors/{id}/delete
Deletes a doctor if no existing appointments conflict.
GET /admin/patients
Table of patients, form for editing or deleting.
POST /admin/patients/{id}/delete
Removes a patient if possible.
6.4. Appointments Endpoints
GET /appointments
Shows all appointments for Admin.
For Doctor role, only that doctor’s appointments.
For Patient role, only that patient’s appointments.
POST /appointments
Create a new appointment with doctorId, patientId, dateTime.
Validation: must have valid doctor/patient IDs, parseable date/time.
POST /appointments/{id}/delete
Removes an appointment.

6.5. Data Validation Rules
User
email: must be unique, non-blank, valid format
password: non-blank
role: must be one of ADMIN, DOCTOR, PATIENT
Doctor
fullName: required
specialization: recommended (like “Cardiology”)
user: must be a User with role=DOCTOR
Patient
fullName: required
phone, gender, medicalHistory: optional but recommended
Appointment
doctorId & patientId: must exist
dateTime: valid LocalDateTime
status: “SCHEDULED”, “CANCELLED”, “COMPLETED”, etc.
Medication
name, dosage, frequency, duration: typically non-blank
patientId: must exist

7. Additional Notes
Database schema: ensure your User entity has the correct fields (resetToken, resetTokenExpiry, etc.) if using the token-based password reset.
Security: Spring Security is configured in SecurityConfig.java. Make sure roles (ADMIN, DOCTOR, PATIENT) are properly set.
Thymeleaf: All HTML files (login, register, forgot_password, reset_password, dashboards) are in src/main/resources/templates/....

8. Running in Production
Build the JAR:

mvn clean package


Provide the correct spring.datasource.* properties via environment variables or external config.
Launch with:
bash

java -jar target/Patient-Medicine-and-Appointment-System.jar


Secure your environment (SSL, secure DB credentials, etc.).
