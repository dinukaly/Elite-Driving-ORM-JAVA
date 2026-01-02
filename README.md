# Elite Driving School Management System

A comprehensive JavaFX-based desktop application for managing driving school operations, built with modern Java technologies and following industry best practices.

## 🏫 Project Overview

The Elite Driving School Management System is a full-featured application designed to streamline the operations of driving schools. It provides an intuitive interface for managing students, instructors, courses, lessons, and payments, making it easier for administrators and receptionists to handle daily operations efficiently.

## ✨ Key Features

### 🎯 User Management
- **Multi-role Authentication**: Separate interfaces for Admin and Receptionist roles
- **Secure Login System**: Password encryption using BCrypt
- **Role-based Access Control**: Different permissions based on user roles

### 👨‍🎓 Student Management
- **Student Registration**: Complete student profile management
- **Course Enrollment**: Easy enrollment of students in multiple courses
- **Payment Tracking**: Monitor payments, remaining fees, and financial status
- **Contact Management**: Store and manage student contact information

### 👨‍🏫 Instructor Management
- **Instructor Profiles**: Detailed instructor information and specializations
- **Course Assignment**: Assign instructors to specific courses
- **Status Management**: Track active/inactive instructor status
- **Contact Details**: Complete contact information management

### 📚 Course Management
- **Course Creation**: Define course details, duration, and fees
- **Course Catalog**: Browse and manage available courses
- **Fee Structure**: Flexible pricing for different courses
- **Duration Management**: Set course timelines and schedules

### 📅 Lesson Scheduling
- **Lesson Planning**: Schedule lessons for students and instructors
- **Time Management**: Efficient lesson time allocation
- **Status Tracking**: Monitor lesson completion status
- **Conflict Resolution**: Avoid scheduling conflicts

### 💰 Payment Management
- **Payment Processing**: Record and track student payments
- **Fee Calculation**: Automatic calculation of remaining balances
- **Payment History**: Complete payment transaction history
- **Financial Reports**: Generate financial summaries

## 🛠️ Technology Stack

### Backend Technologies
- **Java 21**: Latest Java version with modern features
- **Hibernate ORM**: Object-relational mapping for database operations
- **MySQL**: Robust relational database management
- **Jakarta Persistence**: Standard persistence API
- **BCrypt**: Secure password hashing

### Frontend Technologies
- **JavaFX 21**: Modern Java GUI framework
- **JFoenix**: Material Design components for JavaFX
- **FXML**: XML-based UI markup language
- **CSS Styling**: Custom stylesheets for professional appearance

### Development Tools
- **Maven**: Project management and dependency resolution
- **Lombok**: Reduce boilerplate code
- **Material Design Icons**: Professional iconography

## 🏗️ Architecture Pattern

The application follows a **layered architecture** pattern with clear separation of concerns:

### Layer Structure
```
┌─────────────────────────────────────────┐
│         Presentation Layer             │
│  (JavaFX Controllers + FXML Views)     │
├─────────────────────────────────────────┤
│         Business Logic Layer           │
│         (BO - Business Objects)        │
├─────────────────────────────────────────┤
│         Data Access Layer             │
│         (DAO - Data Access Objects)    │
├─────────────────────────────────────────┤
│         Entity Layer                  │
│      (JPA/Hibernate Entities)          │
├─────────────────────────────────────────┤
│         Database Layer                │
│           (MySQL Database)             │
└─────────────────────────────────────────┘
```

### Key Components
- **DTO (Data Transfer Objects)**: For transferring data between layers
- **Entity Classes**: JPA entities representing database tables
- **BO Layer**: Business logic and validation
- **DAO Layer**: Database operations and queries
- **Controllers**: UI logic and user interaction handling

## 🗄️ Database Schema

### Entity Relationships
- **Students** can enroll in multiple **Courses**
- **Courses** can have multiple **Students**
- **Instructors** are assigned to specific **Courses**
- **Lessons** are scheduled between **Students**, **Instructors**, and **Courses**
- **Payments** are tracked for each **Student**

## 🚀 Getting Started

### Prerequisites
- **Java 21** or higher
- **MySQL Server** 8.0+
- **Maven** 3.8+

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd Elite_Driving
   ```

2. **Database Setup**
   - Ensure MySQL is running on your system
   - The application will automatically create the database schema
   - Default database: `elite_driving`

3. **Configure Database Connection**
   - Open `src/main/resources/hibernate.properties`
   - Update MySQL connection details if needed:
     ```properties
     hibernate.connection.url=jdbc:mysql://localhost:3306/elite_driving?createDatabaseIfNotExist=true
     hibernate.connection.username=root
     hibernate.connection.password=your_password
     ```

4. **Build the Project**
   ```bash
   mvn clean install
   ```

5. **Run the Application**
   ```bash
   mvn javafx:run
   ```
   
   Or use the Maven wrapper:
   ```bash
   ./mvnw javafx:run  # Linux/Mac
   mvnw.cmd javafx:run  # Windows
   ```

## 📱 User Interface

### Welcome Screen
- Clean and modern interface with user role selection
- Intuitive navigation to different system modules

### Admin Dashboard
- Comprehensive overview of system statistics
- Quick access to all management modules
- Real-time data visualization

### Management Interfaces
- **Student Management**: CRUD operations with search and filter
- **Instructor Management**: Profile management and assignment
- **Course Management**: Course catalog and configuration
- **Lesson Scheduling**: Interactive scheduling interface
- **Payment Management**: Financial tracking and reporting

## 🔧 Configuration

### Hibernate Configuration
The application uses Hibernate for ORM with the following settings:
- **Auto-schema creation**: Database schema is automatically created
- **SQL logging**: SQL queries are logged for debugging
- **Connection pooling**: Efficient database connection management

### Application Properties
Key configuration files:
- `hibernate.properties`: Database connection and ORM settings
- `module-info.java`: Java module system configuration

## 🔒 Security Features

- **Password Encryption**: All passwords are encrypted using BCrypt
- **Role-based Access**: Different user roles with appropriate permissions
- **Input Validation**: Comprehensive validation for all user inputs
- **Error Handling**: Robust error handling and user feedback

## 🧪 Testing

The application includes comprehensive testing capabilities:
- Unit tests for business logic
- Integration tests for database operations
- UI testing for user interfaces

Run tests with:
```bash
mvn test
```

## 📈 Performance Optimization

- **Lazy Loading**: Efficient data loading strategies
- **Connection Pooling**: Database connection optimization
- **Caching**: Hibernate second-level cache support
- **Query Optimization**: Optimized database queries

## 🛡️ Error Handling

- **Global Exception Handling**: Centralized error management
- **User-friendly Messages**: Clear error messages for users
- **Logging**: Comprehensive logging for debugging
- **Recovery Mechanisms**: Graceful error recovery

## 📚 Documentation

### Code Documentation
- Comprehensive JavaDoc comments throughout the codebase
- Clear method and class documentation
- Usage examples and best practices

### User Documentation
- In-app help system
- Tooltips and guidance
- Error message explanations

## 🤝 Contributing

We welcome contributions to the Elite Driving School Management System! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **JavaFX Community**: For the excellent UI framework
- **Hibernate Team**: For the powerful ORM solution
- **JFoenix**: For the Material Design components
- **MySQL**: For the reliable database system


**Built with ❤️ for driving schools everywhere**