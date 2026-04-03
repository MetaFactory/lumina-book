import { FieldSchema } from '@common/types';
import { normalizeDataTableFormSchema, normalizeEntityPanelSchema } from '@common/utils';

const listFields: FieldSchema[] = [
   {
      name: 'login',
      title: 'Login',
      type: 'TEXT'
   },
   {
      name: 'firstName',
      title: 'First Name',
      type: 'TEXT'
   },
   {
      name: 'lastName',
      title: 'Last Name',
      type: 'TEXT'
   },
   {
      name: 'email',
      title: 'Email',
      type: 'TEXT'
   },
   {
      name: 'activated',
      title: 'Activated',
      type: 'BOOLEAN'
   }
];

const fields: FieldSchema[] = [
   {
      name: 'login',
      title: 'Login',
      type: 'TEXT'
   },
   {
      name: 'firstName',
      title: 'First Name',
      type: 'TEXT'
   },
   {
      name: 'lastName',
      title: 'Last Name',
      type: 'TEXT'
   },
   {
      name: 'email',
      title: 'Email',
      type: 'TEXT'
   },
   {
      name: 'langKey',
      title: 'Language',
      type: 'TEXT'
   },
   {
      name: 'activated',
      title: 'Activated',
      type: 'BOOLEAN'
   }
];

export const userDataTableSchema = normalizeDataTableFormSchema({
   title: 'Accounts',
   serviceName: 'user',
   fields: listFields,
   canInsert: false
});

export const userEntityViewSchema = normalizeEntityPanelSchema({
   title: 'Account',
   serviceName: 'user',
   fields: fields,
   canEdit: false,
   canDelete: false
});
