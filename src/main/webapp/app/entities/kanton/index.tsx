import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Kanton from './kanton';
import KantonDetail from './kanton-detail';
import KantonUpdate from './kanton-update';
import KantonDeleteDialog from './kanton-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={KantonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={KantonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={KantonDetail} />
      <ErrorBoundaryRoute path={match.url} component={Kanton} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={KantonDeleteDialog} />
  </>
);

export default Routes;
