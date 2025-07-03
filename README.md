# GP Booking System

This project is a **General Practitioner (GP) Booking System** developed using **Java** and **Object-Oriented Programming (OOP)** principles. It provides functionality for patients and doctors to manage appointments, view prescriptions, and handle related healthcare tasks through a GUI-based interface.

## 🧠 Key Features

- **Patient Login and Registration**
- **Doctor Login and Management Interface**
- **Book, View, Change, and Reschedule Appointments**
- **View Prescriptions**
- **Database Integration for Persistent Storage**

## 🛠 Technologies Used

- Java (JDK 8+)
- Swing (for GUI)
- JDBC (Java Database Connectivity)
- Object-Oriented Programming principles
- Local Database (e.g., SQLite or MySQL — please configure in `DBManager.java`)

## 📁 Project Structure

| File | Description |
|------|-------------|
| `Main.java` | Entry point of the application |
| `Login_form.java` | Handles user login functionality |
| `NewBooking.java` | Allows patients to create new appointments |
| `ChangeAppointment.java` | Enables modification of existing appointments |
| `RescheduleBooking.class` | Backend logic for rescheduling appointments |
| `ViewBookings.java` | Displays current bookings for a patient |
| `ViewPrescription.java` | Displays prescriptions |
| `DoctorChangeSystem.java` | Allows doctors to change availability or appointments |
| `DoctorDetails.java`, `DoctorsDetails.java` | Stores and manages doctor information |
| `Patient.java`, `Doctor.java` | OOP classes representing the users |
| `DBManager.java` | Manages database connectivity and queries |
| `AppointmentViewer.java` | GUI component for viewing appointment summaries |
| `viewDocs.java` | Allows patients to browse doctors |
| `home_page.java` | Home screen of the application |
| `PatientDoctorSystemGUI.java` | Main GUI handling patient and doctor system integration |

## 🧪 How to Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Botii/GP-Booking-System.git
   cd GPBookingSystem
