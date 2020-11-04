import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Potreba from './potreba';
import PotrebaDetail from './potreba-detail';
import PotrebaUpdate from './potreba-update';
import PotrebaDeleteDialog from './potreba-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PotrebaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PotrebaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PotrebaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Potreba} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PotrebaDeleteDialog} />
  </>
);

export default Routes;
