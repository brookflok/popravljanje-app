import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Slika from './slika';
import SlikaDetail from './slika-detail';
import SlikaUpdate from './slika-update';
import SlikaDeleteDialog from './slika-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SlikaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SlikaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SlikaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Slika} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SlikaDeleteDialog} />
  </>
);

export default Routes;
