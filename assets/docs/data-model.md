# Data Model

## User

- id: UUID
- email: string
- name: string
- timezone: string
- createdAt: datetime

## CalendarConnection

- id: UUID
- userId: UUID
- provider: enum (GOOGLE, OUTLOOK)
- accessToken: string
- refreshToken: string
- expiresAt: datetime

## Event

- id: UUID
- hostId: UUID
- title: string
- description: string
- startTime: datetime
- endTime: datetime
- status: enum (CONFIRMED, CANCELLED)
- createdAt: datetime

## Participant

- id: UUID
- eventId: UUID
- email: string
- name: string
- role: enum (HOST, GUEST)

## BookingPage

- id: UUID
- userId: UUID
- slug: string
- duration: integer
- createdAt: datetime

## Reminder

- id: UUID
- eventId: UUID
- type: enum (EMAIL, NOTIFICATION)
- scheduledAt: datetime
- sent: boolean
