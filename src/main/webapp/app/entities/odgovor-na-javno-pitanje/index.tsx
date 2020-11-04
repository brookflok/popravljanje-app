import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OdgovorNaJavnoPitanje from './odgovor-na-javno-pitanje';
import OdgovorNaJavnoPitanjeDetail from './odgovor-na-javno-pitanje-detail';
import OdgovorNaJavnoPitanjeUpdate from './odgovor-na-javno-pitanje-update';
import OdgovorNaJavnoPitanjeDeleteDialog from './odgovor-na-javno-pitanje-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OdgovorNaJavnoPitanjeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OdgovorNaJavnoPitanjeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OdgovorNaJavnoPitanjeDetail} />
      <ErrorBoundaryRoute path={match.url} component={OdgovorNaJavnoPitanje} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OdgovorNaJavnoPitanjeDeleteDialog} />
  </>
);

export default Routes;
