import { Moment } from 'moment';
import { IMainSlika } from 'app/shared/model/main-slika.model';
import { IProfilnaSlika } from 'app/shared/model/profilna-slika.model';
import { IGalerija } from 'app/shared/model/galerija.model';

export interface ISlika {
  id?: number;
  ime?: string;
  slikaContentType?: string;
  slika?: any;
  uploaded?: string;
  mainslika?: IMainSlika;
  mainslika?: IProfilnaSlika;
  galerija?: IGalerija;
}

export const defaultValue: Readonly<ISlika> = {};
