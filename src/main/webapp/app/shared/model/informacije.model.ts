import { Moment } from 'moment';
import { IArtikl } from 'app/shared/model/artikl.model';

export interface IInformacije {
  id?: number;
  vrstaOglasa?: string;
  datumObjave?: string;
  brojPregleda?: number;
  artikl?: IArtikl;
}

export const defaultValue: Readonly<IInformacije> = {};
