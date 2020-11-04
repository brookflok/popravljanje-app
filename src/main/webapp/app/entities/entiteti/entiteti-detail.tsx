import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './entiteti.reducer';
import { IEntiteti } from 'app/shared/model/entiteti.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEntitetiDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EntitetiDetail = (props: IEntitetiDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { entitetiEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.entiteti.detail.title">Entiteti</Translate> [<b>{entitetiEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="imeEntiteta">
              <Translate contentKey="popravljanjeApp.entiteti.imeEntiteta">Ime Entiteta</Translate>
            </span>
          </dt>
          <dd>{entitetiEntity.imeEntiteta}</dd>
        </dl>
        <Button tag={Link} to="/entiteti" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/entiteti/${entitetiEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ entiteti }: IRootState) => ({
  entitetiEntity: entiteti.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EntitetiDetail);
