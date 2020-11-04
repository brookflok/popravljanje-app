import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Informacije from './informacije';
import InformacijeDetail from './informacije-detail';
import InformacijeUpdate from './informacije-update';
import InformacijeDeleteDialog from './informacije-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InformacijeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InformacijeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InformacijeDetail} />
      <ErrorBoundaryRoute path={match.url} component={Informacije} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={InformacijeDeleteDialog} />
  </>
);

export default Routes;
