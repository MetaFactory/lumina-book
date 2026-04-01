import { importerDataTableSchema, importerEntityViewSchema } from 'src/schema';

export const dynamicForms = [importerDataTableSchema, importerEntityViewSchema];

dynamicForms.forEach((form) => Object.freeze(form));
