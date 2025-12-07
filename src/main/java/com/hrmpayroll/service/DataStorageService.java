package com.hrmpayroll.service;

import com.hrmpayroll.model.*;
import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class DataStorageService {
    private static final String DATA_DIR = "data";
    private static final String EMPLOYEES_FILE = DATA_DIR + File.separator + "employees.ser";
    private static final String PAYROLLS_FILE = DATA_DIR + File.separator + "payrolls.ser";
    private static final String ATTENDANCE_FILE = DATA_DIR + File.separator + "attendance.ser";
    private static final String USERS_FILE = DATA_DIR + File.separator + "users.ser";
    private static final String LEAVE_REQUESTS_FILE = DATA_DIR + File.separator + "leave_requests.ser";

    private static DataStorageService instance;
    private List<Employee> employees;
    private List<Payroll> payrolls;
    private List<AttendanceRecord> attendanceRecords;
    private List<User> users;
    private List<LeaveRequest> leaveRequests;
    private Map<com.hrmpayroll.model.enums.DepartmentType, Department> departments;

    private DataStorageService() {
        ensureDataDirectoryExists();
        initializeDepartments();
        loadAllData();
    }

    public static synchronized DataStorageService getInstance() {
        if (instance == null) {
            instance = new DataStorageService();
        }
        return instance;
    }

    private void initializeDepartments() {
        departments = new HashMap<>();
        for (com.hrmpayroll.model.enums.DepartmentType type : com.hrmpayroll.model.enums.DepartmentType.values()) {
            departments.put(type, new Department(type));
        }
    }

    private void ensureDataDirectoryExists() {
        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> loadData(String filename, Class<T> type) {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data from " + filename + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private <T> boolean saveData(List<T> data, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving data to " + filename + ": " + e.getMessage());
            return false;
        }
    }

    private void loadAllData() {
        employees = loadData(EMPLOYEES_FILE, Employee.class);
        payrolls = loadData(PAYROLLS_FILE, Payroll.class);
        attendanceRecords = loadData(ATTENDANCE_FILE, AttendanceRecord.class);
        users = loadData(USERS_FILE, User.class);
        leaveRequests = loadData(LEAVE_REQUESTS_FILE, LeaveRequest.class);
        syncDepartments();
    }

    private void syncDepartments() {
        // Ensure all employees are in correct department lists
        for (Employee emp : employees) {
            Department dept = departments.get(emp.getDepartment());
            if (dept != null && !dept.getEmployeeIds().contains(emp.getEmployeeId())) {
                dept.addEmployee(emp.getEmployeeId());
            }
        }
    }

    private void saveAllData() {
        saveData(employees, EMPLOYEES_FILE);
        saveData(payrolls, PAYROLLS_FILE);
        saveData(attendanceRecords, ATTENDANCE_FILE);
        saveData(users, USERS_FILE);
        saveData(leaveRequests, LEAVE_REQUESTS_FILE);
    }

    // Employee methods
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public Employee getEmployeeById(String employeeId) {
        return employees.stream()
                .filter(e -> e.getEmployeeId().equals(employeeId))
                .findFirst()
                .orElse(null);
    }

    // FIXED: Added missing method that ReportGenerator depends on
    public Employee getEmployee(String employeeId) {
        return getEmployeeById(employeeId);
    }

    public boolean saveEmployee(Employee employee) {
        boolean exists = getEmployeeById(employee.getEmployeeId()) != null;
        boolean result = exists ? updateEmployee(employee) : employees.add(employee);

        if (result) {
            Department dept = departments.get(employee.getDepartment());
            if (dept != null) {
                dept.addEmployee(employee.getEmployeeId());
            }
            saveData(employees, EMPLOYEES_FILE);
        }
        return result;
    }

    private boolean updateEmployee(Employee employee) {
        employees.removeIf(e -> e.getEmployeeId().equals(employee.getEmployeeId()));
        return employees.add(employee);
    }

    public boolean deleteEmployee(String employeeId) {
        boolean result = employees.removeIf(e -> e.getEmployeeId().equals(employeeId));
        if (result) {
            saveData(employees, EMPLOYEES_FILE);
        }
        return result;
    }

    // Payroll methods
    public List<Payroll> getPayrollsByEmployee(String employeeId) {
        return payrolls.stream()
                .filter(p -> p.getEmployeeId().equals(employeeId))
                .collect(Collectors.toList());
    }

    public boolean savePayroll(Payroll payroll) {
        boolean result = payrolls.add(payroll);
        if (result) {
            saveData(payrolls, PAYROLLS_FILE);
        }
        return result;
    }

    // Attendance methods
    public boolean saveAttendanceRecord(AttendanceRecord attendance) {
        boolean result = attendanceRecords.add(attendance);
        if (result) {
            saveData(attendanceRecords, ATTENDANCE_FILE);
        }
        return result;
    }

    public List<AttendanceRecord> getAttendanceByEmployee(String employeeId) {
        return attendanceRecords.stream()
                .filter(a -> a.getEmployeeId().equals(employeeId))
                .collect(Collectors.toList());
    }

    // User methods
    public User getUser(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public boolean saveUser(User user) {
        users.removeIf(u -> u.getUsername().equals(user.getUsername()));
        boolean result = users.add(user);
        if (result) {
            saveData(users, USERS_FILE);
        }
        return result;
    }

    // Leave request methods
    public boolean saveLeaveRequest(LeaveRequest leaveRequest) {
        boolean result = leaveRequests.add(leaveRequest);
        if (result) {
            saveData(leaveRequests, LEAVE_REQUESTS_FILE);
        }
        return result;
    }

    public List<LeaveRequest> getLeaveRequests() {
        return new ArrayList<>(leaveRequests);
    }

    // Department methods
    public Department getDepartment(com.hrmpayroll.model.enums.DepartmentType departmentType) {
        return departments.get(departmentType);
    }

    public List<Employee> getDepartmentEmployees(com.hrmpayroll.model.enums.DepartmentType departmentType) {
        return employees.stream()
                .filter(e -> e.getDepartment() == departmentType)
                .collect(Collectors.toList());
    }
}