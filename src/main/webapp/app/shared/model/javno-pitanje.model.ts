import { Moment } from 'moment';
import { IDodatniInfoUser } from 'app/shared/model/dodatni-info-user.model';
import { IArtikl } from 'app/shared/model/artikl.model';
import { IOdgovorNaJavnoPitanje } from 'app/shared/model/odgovor-na-javno-pitanje.model';

export interface IJavnoPitanje {
  id?: number;
  pitanje?: string;
  datum?: string;
  prikaz?: boolean;
  dodatniinfoUser?: IDodatniInfoUser;
  artikl?: IArtikl;
  odgovorNaJavnoPitanje?: IOdgovorNaJavnoPitanje;
}

export const defaultValue: Readonly<IJavnoPitanje> = {
  prikaz: false,
};
