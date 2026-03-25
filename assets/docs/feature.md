# Feature: LuminaBook — AI Scheduling Platform

## Goal

Provide an AI-driven scheduling system that allows users to create, manage, and optimize meetings using natural language.

## Actors

- User (host)
- Invitee (guest)
- Admin

## Core Capabilities

- Natural language scheduling
- Calendar integration (Google, Outlook)
- Conflict detection and resolution
- Automated reminders and follow-ups
- Booking pages
- Admin dashboard

---

## User Flows

### Flow 1: Create meeting via natural language

1. User enters prompt:
   "Book a 45-min strategy call with John next week"
2. System parses intent
3. Extract:
   - duration = 45 minutes
   - participant = John
   - time range = next week
4. System checks availability
5. Suggests top 3 time slots
6. User confirms one
7. Event is created and synced to calendars

---

### Flow 2: Booking via public page

1. Guest opens booking link
2. System shows available slots
3. Guest selects slot
4. Guest fills name/email
5. Event is created
6. Confirmation + reminder scheduled

---

### Flow 3: Conflict resolution

1. Event requested overlaps with existing
2. System detects conflict
3. Suggest alternative slots ranked by:
   - minimal conflicts
   - preferred working hours
   - user preferences

---

## Business Rules

1. Only authenticated users can create booking links
2. Guests do not need authentication
3. Time slots must not overlap confirmed events
4. Minimum meeting duration: 15 minutes
5. Maximum meeting duration: 4 hours
6. Time zone must be handled per user
7. Calendar sync must be eventually consistent
8. AI suggestions must always return at least 1 valid slot

---

## Permissions

| Role  | Action                    |
| ----- | ------------------------- |
| User  | Create/edit events        |
| User  | Connect calendars         |
| Guest | Book via link             |
| Admin | View all users and events |

---

## Edge Cases

- No availability → return "no slots found"
- Calendar provider unavailable → retry async
- AI parsing fails → fallback to manual form
- Timezone mismatch → normalize to UTC internally
