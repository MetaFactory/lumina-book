import {
   userDataTableSchema,
   userEntityViewSchema,
   bookingDataTableSchema,
   bookingEntityViewSchema
} from 'src/schema';

export const dynamicForms = [
   userDataTableSchema,
   userEntityViewSchema,
   bookingDataTableSchema,
   bookingEntityViewSchema
];

dynamicForms.forEach((form) => Object.freeze(form));
