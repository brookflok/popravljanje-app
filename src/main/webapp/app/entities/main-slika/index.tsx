import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MainSlika from './main-slika';
import MainSlikaDetail from './main-slika-detail';
import MainSlikaUpdate from './main-slika-update';
import MainSlikaDeleteDialog from './main-slika-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MainSlikaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MainSlikaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MainSlikaDetail} />
      <ErrorBoundaryRoute path={match.url} component={MainSlika} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MainSlikaDeleteDialog} />
  </>
);

export default Routes;
