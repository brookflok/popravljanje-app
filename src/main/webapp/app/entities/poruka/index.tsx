import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Poruka from './poruka';
import PorukaDetail from './poruka-detail';
import PorukaUpdate from './poruka-update';
import PorukaDeleteDialog from './poruka-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PorukaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PorukaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PorukaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Poruka} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PorukaDeleteDialog} />
  </>
);

export default Routes;
