import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProfilnaSlika from './profilna-slika';
import ProfilnaSlikaDetail from './profilna-slika-detail';
import ProfilnaSlikaUpdate from './profilna-slika-update';
import ProfilnaSlikaDeleteDialog from './profilna-slika-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProfilnaSlikaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProfilnaSlikaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProfilnaSlikaDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProfilnaSlika} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProfilnaSlikaDeleteDialog} />
  </>
);

export default Routes;
