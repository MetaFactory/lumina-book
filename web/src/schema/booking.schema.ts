import { FieldSchema } from '@common/types';
import { normalizeDataTableFormSchema, normalizeEntityPanelSchema } from '@common/utils';

const listFields: FieldSchema[] = [
   {
      name: 'guestName',
      title: 'Guest Name',
      type: 'TEXT'
   },
   {
      name: 'guestEmail',
      title: 'Guest Email',
      type: 'TEXT'
   },
   {
      name: 'startTime',
      title: 'Start Time',
      type: 'DATE_TIME'
   },
   {
      name: 'endTime',
      title: 'End Time',
      type: 'DATE_TIME'
   },
   {
      name: 'status',
      title: 'Status',
      type: 'SELECT',
      isEnum: true
   },
   {
      name: 'bookingPage',
      title: 'Booking Page',
      type: 'SELECT'
   }
];

const filterFields: FieldSchema[] = [
   {
      name: 'status',
      title: 'Status',
      type: 'SELECT',
      isEnum: true
   },
   {
      name: 'bookingPage',
      title: 'Booking Page',
      type: 'SELECT'
   }
];

const detailFields: FieldSchema[] = [
   {
      name: 'guestName',
      title: 'Guest Name',
      type: 'TEXT'
   },
   {
      name: 'guestEmail',
      title: 'Guest Email',
      type: 'EMAIL',
      required: true
   },
   {
      name: 'startTime',
      title: 'Start Time',
      type: 'DATE_TIME',
      required: true
   },
   {
      name: 'endTime',
      title: 'End Time',
      type: 'DATE_TIME',
      required: true
   },
   {
      name: 'status',
      title: 'Status',
      type: 'SELECT',
      isEnum: true
   },
   {
      name: 'bookingPage',
      title: 'Booking Page',
      type: 'SELECT'
   },
   {
      name: 'notes',
      title: 'Notes',
      type: 'TEXT',
      multiline: true,
      viewState: 'visible',
      editState: 'visible',
      insertState: 'visible'
   }
];

export const bookingDataTableSchema = normalizeDataTableFormSchema({
   title: 'Bookings',
   serviceName: 'booking',
   fields: listFields,
   filterFields: filterFields,
   canInsert: true
});

export const bookingEntityViewSchema = normalizeEntityPanelSchema({
   title: 'Booking',
   serviceName: 'booking',
   fields: detailFields,
   canEdit: true,
   canDelete: true
});
