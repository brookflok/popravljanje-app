import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DodatniInfoUser from './dodatni-info-user';
import DodatniInfoUserDetail from './dodatni-info-user-detail';
import DodatniInfoUserUpdate from './dodatni-info-user-update';
import DodatniInfoUserDeleteDialog from './dodatni-info-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DodatniInfoUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DodatniInfoUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DodatniInfoUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={DodatniInfoUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DodatniInfoUserDeleteDialog} />
  </>
);

export default Routes;
