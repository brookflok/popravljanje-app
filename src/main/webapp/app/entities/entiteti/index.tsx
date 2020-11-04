import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Entiteti from './entiteti';
import EntitetiDetail from './entiteti-detail';
import EntitetiUpdate from './entiteti-update';
import EntitetiDeleteDialog from './entiteti-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EntitetiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EntitetiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EntitetiDetail} />
      <ErrorBoundaryRoute path={match.url} component={Entiteti} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EntitetiDeleteDialog} />
  </>
);

export default Routes;
