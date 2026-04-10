import { PathAuthority } from '@common/types';

export const appAuthorities: PathAuthority[] = [
   {
      path: 'booking',
      authority: 'ROLE_USER'
   },
   {
      path: 'user',
      authority: 'ROLE_ADMIN'
   }
].map((item) => ({
   ...item,
   path: item.path.replace(/^\/|\/$/, '')
}));
