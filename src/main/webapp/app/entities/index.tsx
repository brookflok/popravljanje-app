import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DodatniInfoUser from './dodatni-info-user';
import Artikl from './artikl';
import Usluga from './usluga';
import Potreba from './potreba';
import Galerija from './galerija';
import MainSlika from './main-slika';
import ProfilnaSlika from './profilna-slika';
import Slika from './slika';
import Informacije from './informacije';
import JavnoPitanje from './javno-pitanje';
import OdgovorNaJavnoPitanje from './odgovor-na-javno-pitanje';
import Entiteti from './entiteti';
import Kanton from './kanton';
import Lokacija from './lokacija';
import Ucesnici from './ucesnici';
import Poruka from './poruka';
import Chat from './chat';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}dodatni-info-user`} component={DodatniInfoUser} />
      <ErrorBoundaryRoute path={`${match.url}artikl`} component={Artikl} />
      <ErrorBoundaryRoute path={`${match.url}usluga`} component={Usluga} />
      <ErrorBoundaryRoute path={`${match.url}potreba`} component={Potreba} />
      <ErrorBoundaryRoute path={`${match.url}galerija`} component={Galerija} />
      <ErrorBoundaryRoute path={`${match.url}main-slika`} component={MainSlika} />
      <ErrorBoundaryRoute path={`${match.url}profilna-slika`} component={ProfilnaSlika} />
      <ErrorBoundaryRoute path={`${match.url}slika`} component={Slika} />
      <ErrorBoundaryRoute path={`${match.url}informacije`} component={Informacije} />
      <ErrorBoundaryRoute path={`${match.url}javno-pitanje`} component={JavnoPitanje} />
      <ErrorBoundaryRoute path={`${match.url}odgovor-na-javno-pitanje`} component={OdgovorNaJavnoPitanje} />
      <ErrorBoundaryRoute path={`${match.url}entiteti`} component={Entiteti} />
      <ErrorBoundaryRoute path={`${match.url}kanton`} component={Kanton} />
      <ErrorBoundaryRoute path={`${match.url}lokacija`} component={Lokacija} />
      <ErrorBoundaryRoute path={`${match.url}ucesnici`} component={Ucesnici} />
      <ErrorBoundaryRoute path={`${match.url}poruka`} component={Poruka} />
      <ErrorBoundaryRoute path={`${match.url}chat`} component={Chat} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
