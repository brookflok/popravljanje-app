import { Moment } from 'moment';
import { IArtikl } from 'app/shared/model/artikl.model';

export interface IGalerija {
  id?: number;
  ime?: string;
  datum?: string;
  artikl?: IArtikl;
}

export const defaultValue: Readonly<IGalerija> = {};
