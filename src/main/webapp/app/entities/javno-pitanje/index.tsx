import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import JavnoPitanje from './javno-pitanje';
import JavnoPitanjeDetail from './javno-pitanje-detail';
import JavnoPitanjeUpdate from './javno-pitanje-update';
import JavnoPitanjeDeleteDialog from './javno-pitanje-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={JavnoPitanjeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={JavnoPitanjeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={JavnoPitanjeDetail} />
      <ErrorBoundaryRoute path={match.url} component={JavnoPitanje} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={JavnoPitanjeDeleteDialog} />
  </>
);

export default Routes;
