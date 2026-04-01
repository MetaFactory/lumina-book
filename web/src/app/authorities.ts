import { PathAuthority } from '@common/types';

export const appAuthorities: PathAuthority[] = [
   {
      path: 'setting/*',
      authority: 'ADMIN'
   }
].map((item) => ({
   ...item,
   path: item.path.replace(/^\/|\/$/, '')
}));
