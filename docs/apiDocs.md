# Scrum Management System - Complete API Specification

## Base Configuration

**Base URL:** `http://localhost:8080/api`

**Authentication:** All endpoints except `/`, `/register`,`/verify` and `/login` require JWT token in header:

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
- `409` conflict - violation of the data integrity
- `500` Internal Server Error

---

## 1. AUTHENTICATION

### POST `/register`

Register new user account

- **Auth:** None
- **Request Body:**
  ```json
  {
    "first_name": "string (required, min 2 chars)",
    "last_name": "string (required, min 2 chars)",
    "username": "string (required, min 2 chars)",
    "email": "string (required, valid email)",
    "password": "string (required, min 8 chars)"
  }
  ```
- **Response (201)**

### POST `/verify`

verify the user

- **Query Params:**
  - `code` (string): the verification code sent to the user via email
- **Response(200)**

### GET `/verify/resend`

resend the verification code to the user email

- **Query Params:**
  - `username` (string): the username to which the code will be sent
- **Response(200)**

### GET `/refresh-token`

get a new jwt token

- **Query Params:**
  - `refreshToken` (string): the refresh token of the user
- **Response(200)**:
  ```json
  {
    "jwt": "string"
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
    "jwt": "string"
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
    "first_name": "string",
    "last_name": "string",
    "username": "string",
    "email": "string",
    "enroll_date": "date",
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
        "project_id": "number",
        "user_story_id": "number"
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
    "first_name": "string (optional)",
    "last_name": "string (optional)",
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
        "first_name": "string",
        "last_name": "string",
        "username": "string",
        "email": "string"
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 100,
      "total_pages": 5
    }
  }
  ```

### GET `/users/{user_id}`

Get specific user details

- **Auth:** Required
- **Response (200):**
  ```json
  {
    "id": "number",
    "first_name": "string",
    "last_name": "string",
    "username": "string",
    "email": "string",
    "enroll_date": "date",
    "projects": [
      {
         "id": "number";
         "name": "string";
         "description": "string";
         "creationDate": "date";
         "scrumMaster": {...};
         "productOwner": {...};
         "userRole": "enum Role";
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
        "creation_date": "datetime",
        "scrum_master": {
          "id": "number",
          "first_name": "string",
          "last_name": "string"
          "username": "string",
          "email": "string"
        },
        "product_owner": {
          "id": "number",
          "first_name": "string",
          "last_name": "string"
          "username": "string",
          "email": "string"
        },
        "user_role": "Role enum"
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
    "scrum_master_username": "string"
  }
  ```
- **Response (201):**

```json
    {
      "id": "number"
      "name": "string",
      "description": "string",
      "creation_date": "date"
      "scrum_master": {
        "id": "string"
        "first_name": "string",
        "last_name": "string",
        "username": "string",
        "email": "string"
        },
      "productOwner": {
        "id": "string"
        "first_name": "string",
        "last_name": "string",
        "username": "string",
        "email": "string"
        },
      "user_role": PRODUCT_OWNER
    }
```

### GET `/projects/{project_id}`

Get project details with all members

- **Auth:** Required (must be project member)
- **Response (200):**
  ```json
  {
    "id": "number",
    "name": "string",
    "description": "string",
    "creation_date": "datetime",
    "scrum_master": { ... },
    "product_owner": { ... },
    "members": [
      {
        "user": {
          "id": "number",
          "first_name": "string",
          "last_name": "string",
          "email": "string"
        },
        "role": "Role enum"
      }
    ],
    "statistics": {
      "total_epics": "number",
      "total_user_stories": "number",
      "total_sprints": "number",
      "active_sprint": "object | null"
    }
  }
  ```

### PUT `/projects/{project_id}`

Update project details

- **Auth:** Required (Product Owner only)
- **Request Body:**
  ```json
  {
    "name": "string (optional)",
    "description": "string (optional)"
    "scrum_master_username": "string (optional)"
  }
  ```
- **Response (200):** Updated project

### DELETE `/projects/{project_id}`

Delete project permanently

- **Auth:** Required (Product Owner only)
- **Response (204):** No content

---

## 4. PROJECT MEMBERS

### GET `/projects/{project_id}/members`

Get all project members

- **Auth:** Required (must be project member)
- **Query Params:**
  - `role` (Role enum): Filter by role
- **Response (200):** Array of members with user info and role

### POST `/projects/{project_id}/members`

Add member to project

- **Auth:** Required (Scrum Master or Product Owner)
- **Request Body:**
  ```json
  {
    "user_id": "number (required)",
    "role": "SCRUM_MASTER|DEVELOPER|TESTER (required)"
  }
  ```
- **Response (201):** Member object
- **Note:** Cannot add duplicate members. Only one Scrum Master per project.

### PUT `/projects/{project_id}/members/{user_id}`

Update member's role

- **Auth:** Required (Scrum Master or Product Owner)
- **Request Body:**
  ```json
  {
    "role": "Role enum (required)"
  }
  ```
- **Response (200):** Updated member

### DELETE `/projects/{project_id}/members/{user_id}`

Remove member from project

- **Auth:** Required (Scrum Master or Product Owner)
- **Response (204):** No content
- **Note:** Cannot remove Product Owner. Reassign first.

---

## 5. EPICS

### GET `/projects/{project_id}/epics`

Get all epics

- **Auth:** Required (must be project member)
- **Response (200):**
  ```json
  [
    {
      "id": "number",
      "title": "string",
      "description": "string",
      "user_stories_count": "number"
    }
  ]
  ```

### POST `/projects/{project_id}/epics`

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
  [
    {
      "id": "number",
      "title": "string",
      "description": "string",
      "user_stories_count": "number"
    }
  ]
  ```

### GET `/projects/{project_id}/epics/{epic_id}`

Get epic details

- **Auth:** Required (must be project member)
- **Response (200):**
  ```json
  {
    "id": "number",
    "title": "string",
    "description": "string",
    "user_stories_count": "number"
  }
  ```

### PUT `/projects/{project_id}/epics/{epic_id}`

Update epic

- **Auth:** Required (Product Owner only)
- **Request Body:**
  ```json
  {
    "title": "string (optional)",
    "description": "string (optional)"
  }
  ```
- **Response (200)**

### DELETE `/projects/{project_id}/epics/{epic_id}`

Delete epic (removes epic association from user stories)

- **Auth:** Required (Product Owner only)
- **Response (204):** No content

---

## 6. USER STORIES

### GET `/projects/{project_id}/user_stories`

Get project user stories

- **Auth:** Required (must be project member)
- **Query Params:**
  - `epicId` (number): Filter by epic
  - `sprintId` (number): Filter by epic
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

### POST `/projects/{project_id}/user_stories`

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

### GET `/projects/{project_id}/user_stories/{user_story_id}`

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
      }
    "description": {
      "as": "string",
      "what": "string",
      "for": "string"
    },
    "acceptance_criteria": [
      {
        "id": "number",
        "given": "string",
        "when": "string",
        "ands": [
            {
                "and": "string"
            }
        ](optional)
        "then": "string"
      }
    ],
  }
  ```

### PUT `/projects/{project_id}/user_stories/{user_story_id}`

Update user story

- **Auth:** Required (Product Owner only)
- **Request Body:** Same as POST (all fields optional)
- **Response (200):** Updated user story

### DELETE `/projects/{project_id}/user_stories/{user_story_id}`

Delete user story (cascades to tasks)

- **Auth:** Required (Product Owner only)
- **Response (204):** No content

### POST `/projects/{project_id}/user_stories/{user_story_id}/criterias`

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
      ](optional)
    "then": "string (required, 'Then [outcome]')"
  }
  ```
- **Response (201):** Created criteria

### PUT `/projects/{project_id}/user_stories/{user_story_id}/criterias/{criteria_id}`

Update acceptance criteria

- **Auth:** Required (Scrum Master)
- **Request Body:** Same as POST (all optional)
- **Response (200):** Updated criteria

### DELETE `/projects/{project_id}/user_stories/{user_story_id}/criterias/{criteria_id}`

Delete acceptance criteria

- **Auth:** Required (Scrum Master only)
- **Response (204):** No content

---

## 7. SPRINTS

### GET `/projects/{project_id}/sprints`

Get all sprints

- **Auth:** Required (must be project member)
- **Query Params:**
  - `active` (boolean): Filter active sprints
- **Response (200):**
  ```json
  [
    {
      "id": "number",
      "title": "string",
      "start_date": "date",
      "end_date": "date",
      "is_active": "boolean",
      "user_stories_count": "number"
    }
  ]
  ```

### POST `/projects/{project_id}/sprints`

Create sprint

- **Auth:** Required (Scrum Master only)
- **Request Body:**
  ```json
  {
    "title": "string (required)",
    "start_date": "date (required)",
    "end_date": "date (required)"
  }
  ```
- **Response (201):** Created sprint
- **Validation:** end_date > start_date, no overlapping sprints

### GET `/projects/{project_id}/sprints/{sprint_id}`

Get sprint details with backlog

- **Auth:** Required (must be project member)
- **Response (200):**
  ```json
  {
    "id": "number",
    "title": "string",
    "start_date": "date",
    "end_date": "date",
    "is_active": "boolean",
    "product_backlog": [
      {
        "id": "number",
        "title": "string",
        "priority": "number",
        "user_story": { ... }
      }
    ],
    "sprint_backlog": [
      {
        "id": "number",
        "title": "string",
        "user_story_id": "number"
      }
    ],
    "statistics": {
      "total_tasks": "number",
      "completed_tasks": "number",
      "in_progress_tasks": "number"
    }
  }
  ```

### PUT `/projects/{project_id}/sprints/{sprint_id}`

Update sprint

- **Auth:** Required (Scrum Master only)
- **Request Body:** Same as POST (all optional)
- **Response (200):** Updated sprint

### DELETE `/projects/{project_id}/sprints/{sprint_id}`

Delete sprint (unassigns user stories)

- **Auth:** Required (Scrum Master only)
- **Response (204):** No content

### POST `/projects/{project_id}/sprints/{sprint_id}/user_stories`

Assign user stories to sprint

- **Auth:** Required (Scrum Master only)
- **Request Body:**
  ```json
  {
    "user_story_ids": ["number array (required)"]
  }
  ```
- **Response (200):** Updated sprint
- **Validation:** User stories must exist and not already in another sprint

### DELETE `/projects/{project_id}/sprints/{sprint_id}/user_stories/{user_story_id}`

Remove user story from sprint

- **Auth:** Required (Scrum Master only)
- **Response (204):** No content

---

## 8. TASKS

### GET `/projects/{project_id}/tasks`

Get all tasks (filtered by role)

- **Auth:** Required (must be project member)
- **Query Params:**
  - `sprint_id` (number): Filter by sprint
  - `user_story_id` (number): Filter by user story
  - `status` (Status enum): Filter by status
  - `assigned_to` (number): Filter by assignee
  - `my_tasks` (boolean): Show only user's tasks
- **Response (200):**
  ```json
  [
    {
      "id": "number",
      "title": "string",
      "description": "string",
      "status": "Status enum",
      "user_story": {
        "id": "number",
        "title": "string"
      },
      "assigned_to": {
        "id": "number",
        "first_name": "string",
        "last_name": "string"
      }
    }
  ]
  ```

### POST `/projects/{project_id}/tasks`

Create task

- **Auth:** Required (Scrum Master only)
- **Request Body:**
  ```json
  {
    "title": "string (required)",
    "description": "string (required)",
    "user_story_id": "number (required)",
    "assigned_to_id": "number (optional)"
  }
  ```
- **Response (201):** Created task
- **Validation:** User story must be in an active sprint

### GET `/projects/{project_id}/tasks/{task_id}`

Get task details

- **Auth:** Required (must be project member)
- **Response (200):**
  ```json
  {
    "id": "number",
    "title": "string",
    "description": "string",
    "status": "Status enum",
    "user_story": { ... },
    "assigned_to": { ... },
    "reports": [
      {
        "id": "number",
        "description": "string",
        "status": "Status enum (TESTED | DONE)",
        "created_by": {
          "id": "number",
          "first_name": "string",
          "last_name": "string",
          "role": "TESTER | DEVELOPER"
        },
        "created_at": "datetime"
      }
    ]
  }
  ```

### PUT `/projects/{project_id}/tasks/{task_id}`

Update task

- **Auth:** Required (role-based)
  - **Scrum Master:** Can update all fields
  - **Developer:** Can update status (TODO → IN_PROGRESS → TO_BE_TESTED)
  - **Tester:** Can update status (TO_BE_TESTED → TESTED or IN_PROGRESS with report)
- **Request Body:**
  ```json
  {
    "title": "string (SM only)",
    "description": "string (SM only)",
    "status": "Status enum (required)",
    "assigned_to_id": "number (SM only)"
  }
  ```
- **Response (200):** Updated task
- **Validation:** Status transitions follow workflow rules

### DELETE `/projects/{project_id}/tasks/{task_id}`

Delete task

- **Auth:** Required (Scrum Master only)
- **Response (204):** No content

---

## 9. TASK REPORTS

### GET `/projects/{project_id}/tasks/{task_id}/reports`

Get all reports for a task

- **Auth:** Required (Developers can view reports on their tasks)
- **Response (200):** Array of reports (see task details)

### POST `/projects/{project_id}/tasks/{task_id}/reports`

Create test report

- **Auth:** Required (Tester only)
- **Request Body:**
  ```json
  {
    "description": "string (required)",
    "status": "TESTED | IN_PROGRESS (required)"
  }
  ```
- **Response (201):** Created report
- **Side Effect:** Updates task status based on report status
- **Validation:** Task must be in TO_BE_TESTED status

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
- **Tester:** TO_BE_TESTED → TESTED or IN_PROGRESS (with report)
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
