export enum CarStatus {
   Received = 'Received',
   Netting = 'Netting',
   Demo = 'Demo',
   Sent = 'Sent'
}

export type FileType = 'Importer' | 'Netting' | 'Demo' | 'Elgersma';

export enum EventAction {
   ReadImporterFile = 'ReadImporterFile',
   ReadDemoFile = 'ReadDemoFile',
   ReadPaidFile = 'ReadPaidFile',
   SendElgersmaEmail = 'SendElgersmaEmail'
}

export enum EventType {
   Info = 'Info',
   Warning = 'Warning',
   Error = 'Error'
}
