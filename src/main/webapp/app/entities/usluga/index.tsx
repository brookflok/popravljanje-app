import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Usluga from './usluga';
import UslugaDetail from './usluga-detail';
import UslugaUpdate from './usluga-update';
import UslugaDeleteDialog from './usluga-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UslugaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UslugaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UslugaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Usluga} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UslugaDeleteDialog} />
  </>
);

export default Routes;
