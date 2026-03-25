# Acceptance Criteria

## AI Scheduling

- Given a valid natural language prompt, system returns at least 1 slot
- Returned slots must not conflict with existing events
- Slots must respect user working hours

## Event Creation

- User can create event with valid time range
- Overlapping events are rejected

## Booking Page

- Guest can view available slots without login
- Guest can successfully reserve a slot

## Calendar Sync

- Events are synced to external calendar within 30 seconds

## Reminders

- Reminder is sent at configured time before event

## Error Handling

- Invalid prompt returns structured error
- External API failure retries at least 3 times
