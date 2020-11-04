import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Galerija from './galerija';
import GalerijaDetail from './galerija-detail';
import GalerijaUpdate from './galerija-update';
import GalerijaDeleteDialog from './galerija-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GalerijaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GalerijaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GalerijaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Galerija} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GalerijaDeleteDialog} />
  </>
);

export default Routes;
