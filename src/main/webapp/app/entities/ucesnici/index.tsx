import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Ucesnici from './ucesnici';
import UcesniciDetail from './ucesnici-detail';
import UcesniciUpdate from './ucesnici-update';
import UcesniciDeleteDialog from './ucesnici-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UcesniciUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UcesniciUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UcesniciDetail} />
      <ErrorBoundaryRoute path={match.url} component={Ucesnici} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UcesniciDeleteDialog} />
  </>
);

export default Routes;
