# ToDo Application

This is a full-stack ToDo application built with **Vue.js** for the frontend and **Spring Boot** for the backend. The project implements **TDD (Test Driven Development)** methodology for both frontend and backend testing. It supports the creation, updating, completion marking, deletion, and listing of ToDo tasks with proper validations and database integration.

---

##  Features

- Create a ToDo with:
  - `id`
  - `description`
  - `deadline`
- Update the ToDo (`description`, `deadline`)
- Mark a ToDo as completed
- Delete a ToDo
- Get a list of upcoming and completed ToDos
- Search functionality on the frontend
- Input validation before saving data
- Data persistence using a database

---

##  Backend (Spring Boot)

###  Components

| Component   | Responsibilities |
|-------------|------------------|
| **Model**   | Contains fields: `id`, `description`, `deadline`, `completed` |
| **Controller** | REST APIs for Create (POST), Update (PUT), Mark Complete (PATCH), Delete (DELETE), and List (GET) operations |
| **Service** | Handles business logic |
| **Repository** | Extends `JpaRepository`, includes `findByCompleted(boolean completed)` |

###  Validation

- `@NotBlank` for non-empty description
- `@FutureOrPresent` for deadline validation

---

##  Testing

###  Tools Used

- **JUnit** – Unit testing framework
- **Mockito** – Mocking repository and services
- **MockMvc** – Simulate HTTP requests/responses for controller testing

###  TDD Approach

Each functionality is first written as a test case. If the test fails, the necessary code is implemented to make it pass.

###  Test Scenarios

#### **Create ToDo**
- [ ] Description is empty
- [ ] Deadline is in the past

#### **Update ToDo**
- [ ] Invalid ID
- [ ] Description empty or deadline in past

#### **Mark As Completed**
- [ ] Invalid ID

#### **Delete ToDo**
- [ ] Invalid ID

#### **List Upcoming ToDos**
- [ ] `completed == false`

#### **List Completed ToDos**
- [ ] `completed == true`

---

##  Frontend (Vue.js)

- Fully responsive UI
- Search functionality to filter ToDos
- Form validation for inputs before submission
- Interacts with backend REST APIs

###  Frontend Testing

- **Tool Used**: [Vitest](https://vitest.dev/)
- Test components and functions with mocked API responses

---

##  Technologies Used

- **Frontend**: Vue.js, HTML, CSS, JavaScript, Vitest
- **Backend**: Spring Boot, Java, JPA, JUnit, Mockito, MockMvc
- **Database**: H2 / MySQL (based on configuration)

---

##  How It Works

1. **Client sends a request** (e.g., create ToDo) → 
2. **Controller** handles it and passes to the **Service** → 
3. **Service** applies business logic and calls **Repository** → 
4. **Repository** stores/retrieves data from the database → 
5. Response is returned to the client

---

##  Folder Structure (Simplified)

