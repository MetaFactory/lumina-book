import { FieldSchema } from '@common/types';
import { normalizeDataTableFormSchema, normalizeEntityPanelSchema } from '@common/utils';

const listFields: FieldSchema[] = [
   {
      name: 'name',
      type: 'TEXT'
   },
   {
      name: 'abbr',
      type: 'TEXT'
   }
];

const fields: FieldSchema[] = [
   {
      name: 'name',
      type: 'TEXT'
   },
   {
      name: 'abbr',
      type: 'TEXT'
   },
   {
      description: `
### Sample layout table (for Peugeot and Citroen):
      Description          start  length 
      a. license plate       1       8   (Kenteken) 
      b. filling             9       7   (opvulling) 
      c. dealer number       16      6   (Dealer nummer)
      d. chassis number      22      17  (Chassis nummer)
      e. currency code       39      3   (Valuta code)
      f. invoice amount      42      8   (factuur bedrag)
      g. date (yyyymmdd)     50      8   (Datum (jjjjmmdd))
`,
      name: 'recordLayout',
      type: 'TEXT'
   }
];

export const importerDataTableSchema = normalizeDataTableFormSchema({
   title: 'Importers',
   serviceName: 'importer',
   fields: listFields,
   canInsert: false
});

export const importerEntityViewSchema = normalizeEntityPanelSchema({
   title: 'Importer',
   serviceName: 'importer',
   fields: fields,
   canEdit: false,
   canDelete: false
});
