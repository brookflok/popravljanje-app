import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Artikl from './artikl';
import ArtiklDetail from './artikl-detail';
import ArtiklUpdate from './artikl-update';
import ArtiklDeleteDialog from './artikl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ArtiklUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ArtiklUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ArtiklDetail} />
      <ErrorBoundaryRoute path={match.url} component={Artikl} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ArtiklDeleteDialog} />
  </>
);

export default Routes;
