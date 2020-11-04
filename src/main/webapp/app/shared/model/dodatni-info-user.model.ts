import { ILokacija } from 'app/shared/model/lokacija.model';
import { IProfilnaSlika } from 'app/shared/model/profilna-slika.model';
import { IUser } from 'app/shared/model/user.model';
import { IArtikl } from 'app/shared/model/artikl.model';
import { IPoruka } from 'app/shared/model/poruka.model';
import { IUcesnici } from 'app/shared/model/ucesnici.model';

export interface IDodatniInfoUser {
  id?: number;
  korisnickoime?: string;
  brojTelefona?: string;
  majstor?: boolean;
  postoji?: boolean;
  detaljniOpis?: string;
  lokacija?: ILokacija;
  profilnaSlika?: IProfilnaSlika;
  user?: IUser;
  artikls?: IArtikl[];
  poruka?: IPoruka;
  ucesnici?: IUcesnici;
}

export const defaultValue: Readonly<IDodatniInfoUser> = {
  majstor: false,
  postoji: false,
};
