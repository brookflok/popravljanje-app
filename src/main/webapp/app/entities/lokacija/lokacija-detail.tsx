import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './lokacija.reducer';
import { ILokacija } from 'app/shared/model/lokacija.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILokacijaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LokacijaDetail = (props: ILokacijaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { lokacijaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.lokacija.detail.title">Lokacija</Translate> [<b>{lokacijaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="adresa">
              <Translate contentKey="popravljanjeApp.lokacija.adresa">Adresa</Translate>
            </span>
          </dt>
          <dd>{lokacijaEntity.adresa}</dd>
          <dt>
            <span id="postanskiBroj">
              <Translate contentKey="popravljanjeApp.lokacija.postanskiBroj">Postanski Broj</Translate>
            </span>
          </dt>
          <dd>{lokacijaEntity.postanskiBroj}</dd>
          <dt>
            <span id="grad">
              <Translate contentKey="popravljanjeApp.lokacija.grad">Grad</Translate>
            </span>
          </dt>
          <dd>{lokacijaEntity.grad}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.lokacija.kanton">Kanton</Translate>
          </dt>
          <dd>{lokacijaEntity.kanton ? lokacijaEntity.kanton.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/lokacija" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lokacija/${lokacijaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ lokacija }: IRootState) => ({
  lokacijaEntity: lokacija.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LokacijaDetail);
