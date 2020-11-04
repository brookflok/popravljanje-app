import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './informacije.reducer';
import { IInformacije } from 'app/shared/model/informacije.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInformacijeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const InformacijeDetail = (props: IInformacijeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { informacijeEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.informacije.detail.title">Informacije</Translate> [<b>{informacijeEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="vrstaOglasa">
              <Translate contentKey="popravljanjeApp.informacije.vrstaOglasa">Vrsta Oglasa</Translate>
            </span>
          </dt>
          <dd>{informacijeEntity.vrstaOglasa}</dd>
          <dt>
            <span id="datumObjave">
              <Translate contentKey="popravljanjeApp.informacije.datumObjave">Datum Objave</Translate>
            </span>
          </dt>
          <dd>
            {informacijeEntity.datumObjave ? (
              <TextFormat value={informacijeEntity.datumObjave} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="brojPregleda">
              <Translate contentKey="popravljanjeApp.informacije.brojPregleda">Broj Pregleda</Translate>
            </span>
          </dt>
          <dd>{informacijeEntity.brojPregleda}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.informacije.artikl">Artikl</Translate>
          </dt>
          <dd>{informacijeEntity.artikl ? informacijeEntity.artikl.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/informacije" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/informacije/${informacijeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ informacije }: IRootState) => ({
  informacijeEntity: informacije.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(InformacijeDetail);
