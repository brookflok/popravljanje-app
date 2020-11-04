import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Lokacija from './lokacija';
import LokacijaDetail from './lokacija-detail';
import LokacijaUpdate from './lokacija-update';
import LokacijaDeleteDialog from './lokacija-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LokacijaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LokacijaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LokacijaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Lokacija} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LokacijaDeleteDialog} />
  </>
);

export default Routes;
