# Scrum Management System - Complete API Specification

## Base Configuration

**Base URL:** `http://localhost:8080/api`

**Authentication:** All endpoints except `/`, `/register`, `/verify`, and `/login` require JWT token in header:

```json
Authorization: Bearer <jwt-token>
```

**Standard Response Codes:**

- `200` OK - Request successful
- `201` Created - Resource created
- `204` No Content - Successful deletion
- `400` Bad Request - Invalid input
- `401` Unauthorized - Missing/invalid token
- `403` Forbidden - Insufficient permissions
- `404` Not Found - Resource doesn't exist
- `409` Conflict - Violation of data integrity
- `500` Internal Server Error

---

## 1. AUTHENTICATION

### POST `/register`

Register new user account

- **Auth:** None
- **Request Body:**
  ```json
  {
    "firstName": "string (required, min 2 chars)",
    "lastName": "string (required, min 2 chars)",
    "username": "string (required, min 2 chars)",
    "email": "string (required, valid email)",
    "password": "string (required, min 8 chars)"
  }
  ```
- **Response (201)**

### POST `/verify`

Verify the user

- **Query Params:**
  - `code` (string): The verification code sent to the user via email
- **Response (200)**

### GET `/verify/resend`

Resend the verification code to the user email

- **Query Params:**
  - `username` (string): The username to which the code will be sent
- **Response (200)**

### GET `/refresh-token`

Get a new JWT token

- **Query Params:**
  - `refreshToken` (string): The refresh token of the user
- **Response (200):**
  ```json
  {
    "jwt": "string",
    "refreshToken": "string"
  }
  ```

### POST `/login`

Authenticate user

- **Auth:** None
- **Request Body:**
  ```json
  {
    "username": "string (required)",
    "password": "string (required)"
  }
  ```
- **Response (200):**
  ```json
  {
    "jwt": "string",
    "refreshToken": "string"
  }
  ```

### POST `/logout`

Invalidate current token

- **Auth:** Required
- **Response (200)**

---

## 2. USER MANAGEMENT

### GET `/profile`

Get current user's profile with projects and tasks

- **Auth:** Required
- **Response (200):**
  ```json
  {
    "id": "number",
    "firstName": "string",
    "lastName": "string",
    "username": "string",
    "email": "string",
    "enrollDate": "date",
    "projects": [
      {
        "id": "number",
        "name": "string",
        "description": "string",
        "role": "SCRUM_MASTER|PRODUCT_OWNER|DEVELOPER|TESTER"
      }
    ],
    "tasks": [
      {
        "id": "number",
        "title": "string",
        "description": "string",
        "status": "Status enum",
        "projectId": "number",
        "userStoryId": "number"
      }
    ]
  }
  ```

### PUT `/profile`

Update current user's profile

- **Auth:** Required
- **Request Body:**
  ```json
  {
    "firstName": "string (optional)",
    "lastName": "string (optional)",
    "username": "string",
    "email": "string (optional)",
    "password": "string (optional, min 8 chars)"
  }
  ```
- **Response (200):** Updated user object

### GET `/users`

Search users

- **Auth:** Required
- **Query Params:**
  - `name` (string): Search by first/last name
  - `email` (string): Search by email
  - `page` (number, default 1)
  - `limit` (number, default 20)
- **Response (200):**
  ```json
  {
    "users": [
      {
        "id": "number",
        "firstName": "string",
        "lastName": "string",
        "username": "string",
        "email": "string"
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 100,
      "totalPages": 5
    }
  }
  ```

### GET `/users/{userId}`

Get specific user details

- **Auth:** Required
- **Response (200):**
  ```json
  {
    "id": "number",
    "firstName": "string",
    "lastName": "string",
    "username": "string",
    "email": "string",
    "enrollDate": "date",
    "projects": [
      {
        "id": "number",
        "name": "string",
        "description": "string",
        "creationDate": "date",
        "scrumMaster": {...},
        "productOwner": {...},
        "userRole": "enum Role"
      }
    ]
  }
  ```

---

## 3. PROJECT MANAGEMENT

### GET `/projects`

Get all projects where user is a member

- **Auth:** Required
- **Query Params:**
  - `page` (number, default 1)
  - `limit` (number, default 20)
- **Response (200):**
  ```json
  {
    "projects": [
      {
        "id": "number",
        "name": "string",
        "description": "string",
        "creationDate": "datetime",
        "scrumMaster": {
          "id": "number",
          "firstName": "string",
          "lastName": "string",
          "username": "string",
          "email": "string"
        },
        "productOwner": {
          "id": "number",
          "firstName": "string",
          "lastName": "string",
          "username": "string",
          "email": "string"
        },
        "userRole": "Role enum"
      }
    ],
    "pagination": { ... }
  }
  ```

### POST `/projects`

Create new project (user becomes Product Owner)

- **Auth:** Required
- **Request Body:**
  ```json
  {
    "name": "string",
    "description": "string",
    "scrumMasterUsername": "string"
  }
  ```
- **Response (201):**
  ```json
  {
    "id": "number",
    "name": "string",
    "description": "string",
    "creationDate": "date",
    "scrumMaster": {
      "id": "string",
      "firstName": "string",
      "lastName": "string",
      "username": "string",
      "email": "string"
    },
    "productOwner": {
      "id": "string",
      "firstName": "string",
      "lastName": "string",
      "username": "string",
      "email": "string"
    },
    "userRole": "PRODUCT_OWNER"
  }
  ```

### GET `/projects/{projectId}`

Get project details with all members

- **Auth:** Required (must be project member)
- **Response (200):**
  ```json
  {
    "id": "number",
    "name": "string",
    "description": "string",
    "creationDate": "datetime",
    "scrumMaster": { ... },
    "productOwner": { ... },
    "members": [
      {
        "user": {
          "id": "number",
          "firstName": "string",
          "lastName": "string",
          "email": "string"
        },
        "role": "Role enum"
      }
    ],
    "statistics": {
      "totalEpics": "number",
      "totalUserStories": "number",
      "totalSprints": "number",
      "activeSprint": "object | null"
    }
  }
  ```

### PUT `/projects/{projectId}`

Update project details

- **Auth:** Required (Product Owner only)
- **Request Body:**
  ```json
  {
    "name": "string (optional)",
    "description": "string (optional)",
    "scrumMasterUsername": "string (optional)"
  }
  ```
- **Response (200):** Updated project

### DELETE `/projects/{projectId}`

Delete project permanently

- **Auth:** Required (Product Owner only)
- **Response (204):** No content

---

## 4. PROJECT MEMBERS

### GET `/projects/{projectId}/members`

Get all project members

- **Auth:** Required (must be project member)
- **Query Params:**
  - `role` (Role enum): Filter by role
- **Response (200):**
  ```json
  [
    {
      "username": "string",
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "role": "Role enum"
    }
  ]
  ```

### POST `/projects/{projectId}/members`

Add member to project

- **Auth:** Required (Scrum Master)
- **Request Body:**
  ```json
  {
    "username": "string (required)",
    "role": "DEVELOPER|TESTER (required)"
  }
  ```
- **Response (201):** Member object
- **Note:** Cannot add duplicate members. Only one Scrum Master per project.

### PUT `/projects/{projectId}/members/{username}/roles`

Add a role to a member

- **Auth:** Required (Scrum Master or Product Owner)
- **Request Body:**
  ```json
  {
    "role": "Role enum (required)"
  }
  ```
- **Response (200):** Updated member

### DELETE `/projects/{projectId}/members/{username}/roles`

Remove a role from a member

- **Auth:** Required (Scrum Master or Product Owner)
- **Request Body:**
  ```json
  {
    "role": "Role enum (required)"
  }
  ```
- **Response (204):** No content

### DELETE `/projects/{projectId}/members/{username}`

Remove member from project

- **Auth:** Required (Scrum Master or Product Owner)
- **Response (204):** No content
- **Note:** Cannot remove Product Owner. Reassign first.

---

## 5. EPICS

### GET `/projects/{projectId}/epics`

Get all epics

- **Auth:** Required (must be project member)
- **Response (200):**
  ```json
  [
    {
      "id": "number",
      "title": "string",
      "description": "string",
      "userStoriesCount": "number"
    }
  ]
  ```

### POST `/projects/{projectId}/epics`

Create new epic

- **Auth:** Required (Product Owner only)
- **Request Body:**
  ```json
  {
    "title": "string (required)",
    "description": "string (required)"
  }
  ```
- **Response (201):**
  ```json
  {
    "id": "number",
    "title": "string",
    "description": "string",
    "userStoriesCount": "number"
  }
  ```

### GET `/projects/{projectId}/epics/{epicId}`

Get epic details

- **Auth:** Required (must be project member)
- **Response (200):**
  ```json
  {
    "id": "number",
    "title": "string",
    "description": "string",
    "userStoriesCount": "number"
  }
  ```

### PUT `/projects/{projectId}/epics/{epicId}`

Update epic

- **Auth:** Required (Product Owner only)
- **Request Body:**
  ```json
  {
    "title": "string (optional)",
    "description": "string (optional)"
  }
  ```
- **Response (200):** Updated epic

### DELETE `/projects/{projectId}/epics/{epicId}`

Delete epic (removes epic association from user stories)

- **Auth:** Required (Product Owner only)
- **Response (204):** No content

### POST `/projects/{projectId}/epics/{epicId}/user-stories`

Add user stories to epic

- **Auth:** Required (Product Owner only)
- **Request Body:**
  ```json
  {
    "userStoryIds": ["number array (required)"]
  }
  ```
- **Response (200):** Updated epic
- **Validation:** User stories must exist in the project

### DELETE `/projects/{projectId}/epics/{epicId}/user-stories/{userStoryId}`

Remove user story from epic

- **Auth:** Required (Product Owner only)
- **Response (204):** No content

---

## 6. USER STORIES

### GET `/projects/{projectId}/user-stories`

Get project user stories

- **Auth:** Required (must be project member)
- **Query Params:**
  - `epicId` (number): Filter by epic
  - `sprintId` (number): Filter by sprint
  - `unassigned` (boolean): Show only stories not in sprint
- **Response (200):**
  ```json
  [
    {
      "id": "number",
      "title": "string",
      "priority": "number",
      "epic": {
        "id": "number",
        "title": "string"
      },
      "sprint": {
        "id": "number",
        "title": "string"
      }
    }
  ]
  ```

### POST `/projects/{projectId}/user-stories`

Create user story

- **Auth:** Required (Product Owner only)
- **Request Body:**
  ```json
  {
    "title": "string (required)",
    "priority": "number (optional)",
    "description": {
      "as": "string (required, 'As a [user type]')",
      "what": "string (required, 'I want [goal]')",
      "for": "string (required, 'So that [benefit]')"
    }
  }
  ```
- **Response (201):** Created user story

### GET `/projects/{projectId}/user-stories/{userStoryId}`

Get user story details with tasks and criteria

- **Auth:** Required (must be project member)
- **Response (200):**
  ```json
  {
    "id": "number",
    "title": "string",
    "priority": "number",
    "epic": {
      "id": "number",
      "title": "string"
    },
    "sprint": {
      "id": "number",
      "title": "string"
    },
    "description": {
      "as": "string",
      "what": "string",
      "for": "string"
    },
    "acceptanceCriteria": [
      {
        "id": "number",
        "given": "string",
        "when": "string",
        "ands": [
          {
            "and": "string"
          }
        ],
        "then": "string"
      }
    ]
  }
  ```

### PUT `/projects/{projectId}/user-stories/{userStoryId}`

Update user story

- **Auth:** Required (Product Owner only)
- **Request Body:** Same as POST (all fields optional)
- **Response (200):** Updated user story

### DELETE `/projects/{projectId}/user-stories/{userStoryId}`

Delete user story (cascades to tasks)

- **Auth:** Required (Product Owner only)
- **Response (204):** No content

### POST `/projects/{projectId}/user-stories/{userStoryId}/criterias`

Add acceptance criteria

- **Auth:** Required (Scrum Master only)
- **Request Body:**
  ```json
  {
    "given": "string (required, 'Given [context]')",
    "when": "string (required, 'When [action]')",
    "ands": [
      {
        "and": "string"
      }
    ],
    "then": "string (required, 'Then [outcome]')"
  }
  ```
- **Response (201):** Created criteria

### PUT `/projects/{projectId}/user-stories/{userStoryId}/criterias/{criteriaId}`

Update acceptance criteria

- **Auth:** Required (Scrum Master only)
- **Request Body:** Same as POST (all optional)
- **Response (200):** Updated criteria

### DELETE `/projects/{projectId}/user-stories/{userStoryId}/criterias/{criteriaId}`

Delete acceptance criteria

- **Auth:** Required (Scrum Master only)
- **Response (204):** No content

---

## 7. SPRINTS

### GET `/projects/{projectId}/sprints`

Get all sprints

- **Auth:** Required (must be project member)
- **Query Params:**
  - `active` (boolean): Filter active sprints
  - `startDate` (date): Filter by the start date
  - `endDate` (date): Filter by the end date
- **Response (200):**
  ```json
  [
    {
      "id": "number",
      "title": "string",
      "startDate": "date",
      "endDate": "date",
      "isActive": "boolean",
      "userStoriesCount": "number"
    }
  ]
  ```

### POST `/projects/{projectId}/sprints`

Create sprint

- **Auth:** Required (Scrum Master only)
- **Request Body:**
  ```json
  {
    "title": "string (required)",
    "startDate": "date (required)",
    "endDate": "date (required)"
  }
  ```
- **Response (201):**
  ```json
  {
    "id": "number",
    "title": "string",
    "startDate": "date",
    "endDate": "date",
    "isActive": "boolean",
    "userStoriesCount": "number"
  }
  ```
- **Validation:** endDate > startDate, no overlapping sprints

### GET `/projects/{projectId}/sprints/{sprintId}`

Get sprint details with backlog

- **Auth:** Required (must be project member)
- **Response (200):**
  ```json
  {
    "id": "number",
    "title": "string",
    "startDate": "date",
    "endDate": "date",
    "isActive": "boolean",
    "userStoriesCount": "number"
  }
  ```

### PUT `/projects/{projectId}/sprints/{sprintId}`

Update sprint

- **Auth:** Required (Scrum Master only)
- **Request Body:** Same as POST (all optional)
- **Response (200):** Updated sprint

### DELETE `/projects/{projectId}/sprints/{sprintId}`

Delete sprint (unassigns user stories)

- **Auth:** Required (Scrum Master only)
- **Response (204):** No content

### POST `/projects/{projectId}/sprints/{sprintId}/user-stories`

Assign user stories to sprint

- **Auth:** Required (Scrum Master only)
- **Request Body:**
  ```json
  {
    "userStoryIds": ["number array (required)"]
  }
  ```
- **Response (200):** Updated sprint
- **Validation:** User stories must exist and not already in another sprint

### DELETE `/projects/{projectId}/sprints/{sprintId}/user-stories/{userStoryId}`

Remove user story from sprint

- **Auth:** Required (Scrum Master only)
- **Response (204):** No content

---

## 8. TASKS

### GET `/projects/{projectId}/tasks`

Get all tasks

- **Auth:** Required (must be a Scrum Master)
- **Query Params:**
  - `sprintId` (number): Filter by sprint
  - `userStoryId` (number): Filter by user story
  - `status` (Status enum): Filter by status
  - `developer` (string): Filter by developer
  - `tester` (string): Filter by tester
- **Response (200):**
  ```json
  [
    {
      "id": "number",
      "title": "string",
      "description": "string",
      "status": "Status enum",
      "userStory": {
        "id": "number",
        "title": "string"
      }
    }
  ]
  ```

### GET `/projects/{projectId}/tasks/me`

Get all tasks assigned to current user

- **Auth:** Required (must be Developer or Tester)
- **Query Params:**
  - `sprintId` (number): Filter by sprint
  - `userStoryId` (number): Filter by user story
  - `status` (Status enum): Filter by status
- **Response (200):**
  ```json
  [
    {
      "id": "number",
      "title": "string",
      "description": "string",
      "status": "Status enum",
      "userStory": {
        "id": "number",
        "title": "string"
      }
    }
  ]
  ```

### POST `/projects/{projectId}/user-stories/{userStoryId}/tasks`

Create task

- **Auth:** Required (Scrum Master only)
- **Request Body:**
  ```json
  {
    "title": "string (required)",
    "description": "string (required)",
    "developer": "string (optional)",
    "tester": "string (optional)"
  }
  ```
- **Response (201):** Created task
- **Validation:** User story must be in a sprint

### GET `/projects/{projectId}/user-stories/{userStoryId}/tasks/{taskId}`

Get task details

- **Auth:** Required (Must be a Scrum Master, Developer or Tester assigned to this task)
- **Response (200):**
  ```json
  {
    "id": "number",
    "title": "string",
    "description": "string",
    "status": "Status enum",
    "userStory": {
      "id": "number",
      "title": "string"
    },
    "developer": {
      "username": "string",
      "firstName": "string",
      "lastName": "string"
    },
    "tester": {
      "username": "string",
      "firstName": "string",
      "lastName": "string"
    },
    "reports": [
      {
        "id": "number",
        "description": "string",
        "createdBy": {
          "username": "string",
          "firstName": "string",
          "lastName": "string"
        },
        "creationDate": "datetime"
      }
    ]
  }
  ```

### PUT `/projects/{projectId}/user-stories/{userStoryId}/tasks/{taskId}`

Update task

- **Auth:** Required (role-based)
  - **Scrum Master:** Can update all fields
- **Request Body:**
  ```json
  {
    "title": "string (optional)",
    "description": "string (optional)",
    "status": "Status enum (optional)",
    "developer": "string (optional)",
    "tester": "string (optional)"
  }
  ```
- **Response (200):** Updated task
- **Validation:** Status transitions follow workflow rules

### PUT `/projects/{projectId}/user-stories/{userStoryId}/tasks/{taskId}/status`

Update task status

- **Auth:** Required
  - **Tester:** Can update status (TO_BE_TESTED → TESTED or TEST_FAILED with report)
  - **Developer:** Can update status (TODO → IN_PROGRESS or TEST_FAILED → IN_PROGRESS)
  - **Scrum Master:** Can update status (TODO → IN_PROGRESS → TO_BE_TESTED → TESTED or TEST_FAILED → DONE)
- **Request Body:**
  ```json
  {
    "status": "Status enum",
    "report": {
      "description": "string"
    }
  }
  ```
- **Response (200):** Updated task
- **Validation:** Status transitions follow workflow rules

### DELETE `/projects/{projectId}/user-stories/{userStoryId}/tasks/{taskId}`

Delete task

- **Auth:** Required (Scrum Master only)
- **Response (204):** No content

---

## 9. REPORTS

### DELETE `/projects/{projectId}/tasks/{taskId}/reports/{reportId}`

Delete report

- **Auth:** Required (Scrum Master or Tester who made it)
- **Response (204):** No content

---

## BUSINESS RULES & VALIDATIONS

### Status Transitions

**Task Status Workflow:**

```
TODO → IN_PROGRESS → TO_BE_TESTED → TESTED → DONE
         ↑              ↓
         └──────────────┘ (Tester rejects)
```

- **Developer:** TODO → IN_PROGRESS, IN_PROGRESS → TO_BE_TESTED
- **Tester:** TO_BE_TESTED → TESTED or TO_BE_TESTED → IN_PROGRESS (with report)
- **Scrum Master:** Can mark TESTED → DONE

### Role Restrictions

- **Product Owner:**
  - Create/update/delete epics and user stories
  - Cannot modify sprints or tasks
  - Cannot remove themselves from project

- **Scrum Master:**
  - Manage sprints, tasks, and acceptance criteria
  - Assign user stories to sprints
  - Cannot modify epics or user story descriptions
  - One per project

- **Developer:**
  - Update own task status
  - View tasks and user stories

- **Tester:**
  - Test tasks and create reports
  - Update task status after testing

### Sprint Rules

- Tasks can only exist in user stories that are assigned to sprints
- User stories can only be in one sprint at a time
- Cannot delete sprint with active tasks

### Data Integrity

- Deleting user story cascades to tasks
- Deleting epic unlinks user stories (doesn't delete them)
- Removing project member unassigns their tasks
- Cannot have duplicate project members

---

## ERROR RESPONSES

All errors follow this format:

```json
{
  "error": "ERROR_CODE",
  "message": "Human readable message",
  "status": 400,
  "details": {
    /* optional field-specific errors */
  }
}
```

**Common Error Codes:**

- `INTERNAL_SERVER_ERROR` - There is a problem in the server
- `RESOURCE_EXPIRED` - Target resource was expired
- `INVALID_INPUT` - Validation failed
- `UNAUTHORIZED` - Missing/invalid token
- `FORBIDDEN` - Insufficient permissions
- `NOT_FOUND` - Resource doesn't exist
- `CONFLICT` - Duplicate or conflicting data
- `INVALID_STATUS_TRANSITION` - Status change not allowed
- `SPRINT_OVERLAP` - Date ranges overlap
- `NOT_PROJECT_MEMBER` - User not in project
