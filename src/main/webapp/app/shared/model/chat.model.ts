import { Moment } from 'moment';
import { IArtikl } from 'app/shared/model/artikl.model';
import { IUcesnici } from 'app/shared/model/ucesnici.model';

export interface IChat {
  id?: number;
  datum?: string;
  postoji?: boolean;
  artikl?: IArtikl;
  ucesnici?: IUcesnici;
}

export const defaultValue: Readonly<IChat> = {
  postoji: false,
};
