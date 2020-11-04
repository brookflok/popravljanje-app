import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './odgovor-na-javno-pitanje.reducer';
import { IOdgovorNaJavnoPitanje } from 'app/shared/model/odgovor-na-javno-pitanje.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOdgovorNaJavnoPitanjeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OdgovorNaJavnoPitanjeDetail = (props: IOdgovorNaJavnoPitanjeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { odgovorNaJavnoPitanjeEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.detail.title">OdgovorNaJavnoPitanje</Translate> [
          <b>{odgovorNaJavnoPitanjeEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="odgovor">
              <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.odgovor">Odgovor</Translate>
            </span>
          </dt>
          <dd>{odgovorNaJavnoPitanjeEntity.odgovor}</dd>
          <dt>
            <span id="datum">
              <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.datum">Datum</Translate>
            </span>
          </dt>
          <dd>
            {odgovorNaJavnoPitanjeEntity.datum ? (
              <TextFormat value={odgovorNaJavnoPitanjeEntity.datum} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="prikaz">
              <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.prikaz">Prikaz</Translate>
            </span>
          </dt>
          <dd>{odgovorNaJavnoPitanjeEntity.prikaz ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.javnoPitanje">Javno Pitanje</Translate>
          </dt>
          <dd>{odgovorNaJavnoPitanjeEntity.javnoPitanje ? odgovorNaJavnoPitanjeEntity.javnoPitanje.id : ''}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.dodatniinfoUser">Dodatniinfo User</Translate>
          </dt>
          <dd>{odgovorNaJavnoPitanjeEntity.dodatniinfoUser ? odgovorNaJavnoPitanjeEntity.dodatniinfoUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/odgovor-na-javno-pitanje" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/odgovor-na-javno-pitanje/${odgovorNaJavnoPitanjeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ odgovorNaJavnoPitanje }: IRootState) => ({
  odgovorNaJavnoPitanjeEntity: odgovorNaJavnoPitanje.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OdgovorNaJavnoPitanjeDetail);
