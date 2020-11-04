import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import dodatniInfoUser, {
  DodatniInfoUserState
} from 'app/entities/dodatni-info-user/dodatni-info-user.reducer';
// prettier-ignore
import artikl, {
  ArtiklState
} from 'app/entities/artikl/artikl.reducer';
// prettier-ignore
import usluga, {
  UslugaState
} from 'app/entities/usluga/usluga.reducer';
// prettier-ignore
import potreba, {
  PotrebaState
} from 'app/entities/potreba/potreba.reducer';
// prettier-ignore
import galerija, {
  GalerijaState
} from 'app/entities/galerija/galerija.reducer';
// prettier-ignore
import mainSlika, {
  MainSlikaState
} from 'app/entities/main-slika/main-slika.reducer';
// prettier-ignore
import profilnaSlika, {
  ProfilnaSlikaState
} from 'app/entities/profilna-slika/profilna-slika.reducer';
// prettier-ignore
import slika, {
  SlikaState
} from 'app/entities/slika/slika.reducer';
// prettier-ignore
import informacije, {
  InformacijeState
} from 'app/entities/informacije/informacije.reducer';
// prettier-ignore
import javnoPitanje, {
  JavnoPitanjeState
} from 'app/entities/javno-pitanje/javno-pitanje.reducer';
// prettier-ignore
import odgovorNaJavnoPitanje, {
  OdgovorNaJavnoPitanjeState
} from 'app/entities/odgovor-na-javno-pitanje/odgovor-na-javno-pitanje.reducer';
// prettier-ignore
import entiteti, {
  EntitetiState
} from 'app/entities/entiteti/entiteti.reducer';
// prettier-ignore
import kanton, {
  KantonState
} from 'app/entities/kanton/kanton.reducer';
// prettier-ignore
import lokacija, {
  LokacijaState
} from 'app/entities/lokacija/lokacija.reducer';
// prettier-ignore
import ucesnici, {
  UcesniciState
} from 'app/entities/ucesnici/ucesnici.reducer';
// prettier-ignore
import poruka, {
  PorukaState
} from 'app/entities/poruka/poruka.reducer';
// prettier-ignore
import chat, {
  ChatState
} from 'app/entities/chat/chat.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly dodatniInfoUser: DodatniInfoUserState;
  readonly artikl: ArtiklState;
  readonly usluga: UslugaState;
  readonly potreba: PotrebaState;
  readonly galerija: GalerijaState;
  readonly mainSlika: MainSlikaState;
  readonly profilnaSlika: ProfilnaSlikaState;
  readonly slika: SlikaState;
  readonly informacije: InformacijeState;
  readonly javnoPitanje: JavnoPitanjeState;
  readonly odgovorNaJavnoPitanje: OdgovorNaJavnoPitanjeState;
  readonly entiteti: EntitetiState;
  readonly kanton: KantonState;
  readonly lokacija: LokacijaState;
  readonly ucesnici: UcesniciState;
  readonly poruka: PorukaState;
  readonly chat: ChatState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  dodatniInfoUser,
  artikl,
  usluga,
  potreba,
  galerija,
  mainSlika,
  profilnaSlika,
  slika,
  informacije,
  javnoPitanje,
  odgovorNaJavnoPitanje,
  entiteti,
  kanton,
  lokacija,
  ucesnici,
  poruka,
  chat,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
