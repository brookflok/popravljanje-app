import { IKanton } from 'app/shared/model/kanton.model';

export interface ILokacija {
  id?: number;
  adresa?: string;
  postanskiBroj?: string;
  grad?: string;
  kanton?: IKanton;
}

export const defaultValue: Readonly<ILokacija> = {};
