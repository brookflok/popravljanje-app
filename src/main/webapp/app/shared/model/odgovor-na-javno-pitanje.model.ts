import { Moment } from 'moment';
import { IJavnoPitanje } from 'app/shared/model/javno-pitanje.model';
import { IDodatniInfoUser } from 'app/shared/model/dodatni-info-user.model';

export interface IOdgovorNaJavnoPitanje {
  id?: number;
  odgovor?: string;
  datum?: string;
  prikaz?: boolean;
  javnoPitanje?: IJavnoPitanje;
  dodatniinfoUser?: IDodatniInfoUser;
}

export const defaultValue: Readonly<IOdgovorNaJavnoPitanje> = {
  prikaz: false,
};
