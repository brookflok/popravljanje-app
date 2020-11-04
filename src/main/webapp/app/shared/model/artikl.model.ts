import { ILokacija } from 'app/shared/model/lokacija.model';
import { IPotreba } from 'app/shared/model/potreba.model';
import { IUsluga } from 'app/shared/model/usluga.model';
import { IInformacije } from 'app/shared/model/informacije.model';
import { IDodatniInfoUser } from 'app/shared/model/dodatni-info-user.model';

export interface IArtikl {
  id?: number;
  ime?: string;
  kratkiOpis?: string;
  detaljniOpis?: string;
  majstor?: boolean;
  postoji?: boolean;
  lokacija?: ILokacija;
  potreba?: IPotreba;
  usluga?: IUsluga;
  informacije?: IInformacije;
  dodatniinfouser?: IDodatniInfoUser;
}

export const defaultValue: Readonly<IArtikl> = {
  majstor: false,
  postoji: false,
};
