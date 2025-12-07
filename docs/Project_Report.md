# HRM & Payroll System - Project Report

**Course:** COMP2130 - Advanced Java Programming  
**Semester:** Fall 2025  
**Group Members:** Amran Hawaga, Jezrel Dela Cruz, Jared Duldulao  
**Submission Date:** December 7, 2025

---

## Table of Contents
1. Executive Summary
2. System Architecture
3. Implementation Details
4. Features Implemented
5. Challenges & Solutions
6. Testing & Validation
7. Future Enhancements

---

## 1. Executive Summary

This project delivers a comprehensive JavaFX Human Resource Management and Payroll System that exceeds the assignment requirements. The application features robust object-oriented design, persistent data storage via serialization, role-based access control, and advanced data visualization for payroll analytics.

**Key Achievements:**
- ✅ Full CRUD operations for employees
- ✅ Advanced payroll calculations with Canadian tax compliance
- ✅ Multi-level user authentication (Admin, HR Manager, HR Staff)
- ✅ Real-time attendance tracking with clock-in/out
- ✅ Leave management system with approval workflow
- ✅ Interactive charts (PieChart, BarChart) for analytics
- ✅ Comprehensive error handling and input validation
- ✅ Professional UI/UX with CSS styling

---

## 2. System Architecture

### Design Pattern: MVC Architecture
- **Model:** Serializable entity classes (Employee, Payroll, User, etc.)
- **View:** FXML-based UI with CSS styling
- **Controller:** Event-driven controllers with separation of concerns

### Key Design Decisions:
- **Singleton Pattern:** Used for DataStorageService and AuthenticationService to ensure single source of truth
- **Strategy Pattern:** PayrollCalculator allows for different calculation strategies
- **Observer Pattern:** TableView automatically updates when data changes

### Data Flow:
1. User interaction → Controller
2. Controller → Service Layer
3. Service Layer → Model/Data Storage
4. Model changes → UI updates via JavaFX bindings

---

## 3. Implementation Details

### Core Classes:

**Employee.java**
- Encapsulates employee data with 15+ attributes
- Automatic ID generation using timestamp
- Age calculation based on DOB
- Implements Serializable for persistence

**PayrollCalculator.java**
- Implements 2025 Canadian tax rates (Federal, Provincial, CPP, EI)
- Overtime calculation at 1.5x rate after 40 hours
- Year-to-date tracking for financial reporting
- Maximum contribution limits for CPP and EI

**DataStorageService.java**
- Singleton pattern ensures thread-safe data access
- Generic serialization using SerializationUtil
- Automatic department synchronization
- In-memory maps for performance with disk persistence

---

## 4. Features Implemented

### 4.1 Basic Requirements (100% Complete)

| Feature | Implementation Status | Notes |
|---------|----------------------|-------|
| Employee Management | ✓ Fully Implemented | CRUD + search + validation |
| Payroll Processing | ✓ Fully Implemented | Tax-compliant calculations |
| Department Management | ✓ Fully Implemented | Enum-based with budget tracking |
| Data Serialization | ✓ Fully Implemented | All models serializable |
| Reporting | ✓ Fully Implemented | Text and visual reports |

### 4.2 Advanced Features (Bonus)

| Feature | Implementation Status | Complexity |
|---------|----------------------|------------|
| **User Authentication** | ✓ Complete | Multi-role with session management |
| **Role-Based Access Control** | ✓ Complete | 3-tier permission system |
| **Attendance Tracking** | ✓ Complete | Real-time clock-in/out with hours calculation |
| **Leave Management** | ✓ Complete | Request/approval workflow |
| **Data Visualization** | ✓ Complete | PieChart & BarChart integration |
| **Audit Logging** | ✓ Complete | Console-based audit trail |

---

## 5. Challenges & Solutions

### Challenge 1: Serialization of Complex Object Graphs
**Problem:** Circular references between Employee and Department  
**Solution:** Store employee IDs in Department instead of object references

### Challenge 2: Tax Calculation Accuracy
**Problem:** Precise decimal handling for financial calculations  
**Solution:** Used double with rounding, implemented maximum contribution checks

### Challenge 3: UI Responsiveness
**Problem:** Large datasets causing UI lag  
**Solution:** Lazy loading with search filtering, background threads for reports

### Challenge 4: Role-Based UI Updates
**Problem:** Disabling controls based on user role  
**Solution:** Dynamic UI state management in DashboardController.initialize()

---

## 6. Testing & Validation

### Test Cases Executed:
1. **Login Tests:** Valid/invalid credentials, account lockout after 3 attempts
2. **CRUD Tests:** Add 50+ employees, edit, delete with cascade verification
3. **Payroll Tests:** Calculate for various hours (0, 40, 60, 100), verify deductions
4. **Serialization Tests:** Restart application, verify data persistence
5. **Role Tests:** Verify HR_STAFF cannot access reports
6. **Performance Tests:** Generate reports for 100+ employees

### Results:
- All tests passed ✓
- No data loss detected across application restarts
- UI response time &lt; 2 seconds for all operations

---

## 7. Future Enhancements

1. **Database Migration:** Replace serialization with MySQL/PostgreSQL
2. **Email Integration:** Automated pay stub delivery
3. **Biometric Authentication:** Fingerprint/face recognition for attendance
4. **Mobile App:** Companion Android/iOS application
5. **Machine Learning:** Predictive analytics for leave patterns
6. **Cloud Storage:** AWS/Azure backup integration

---

## 8. Team Contributions

| Member | Role | Major Contributions |
|--------|------|---------------------|
| **Amran Hawaga** | Lead Developer & System Architect | System design, core services, payroll engine, integration management |
| **Jezrel Dela Cruz** | UI/UX Designer & QA Engineer | FXML design, CSS styling, dashboard layout, comprehensive testing |
| **Jared Duldulao** | Database Manager & Technical Writer | Data models, serialization, enum classes, project documentation |

---

**Total Lines of Code:** ~3,500  
**Development Hours:** ~120 hours  
**Known Issues:** None  
**Deployment:** Ready for production use

---

## Appendices

### Appendix A: Class Diagram
[Diagram would be inserted here]