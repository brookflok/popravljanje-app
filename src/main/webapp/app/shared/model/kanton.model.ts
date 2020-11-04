import { IEntiteti } from 'app/shared/model/entiteti.model';

export interface IKanton {
  id?: number;
  imeKantona?: string;
  entitet?: IEntiteti;
}

export const defaultValue: Readonly<IKanton> = {};
