import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './potreba.reducer';
import { IPotreba } from 'app/shared/model/potreba.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPotrebaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PotrebaDetail = (props: IPotrebaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { potrebaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.potreba.detail.title">Potreba</Translate> [<b>{potrebaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="cijenaMin">
              <Translate contentKey="popravljanjeApp.potreba.cijenaMin">Cijena Min</Translate>
            </span>
          </dt>
          <dd>{potrebaEntity.cijenaMin}</dd>
          <dt>
            <span id="cijenaMax">
              <Translate contentKey="popravljanjeApp.potreba.cijenaMax">Cijena Max</Translate>
            </span>
          </dt>
          <dd>{potrebaEntity.cijenaMax}</dd>
        </dl>
        <Button tag={Link} to="/potreba" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/potreba/${potrebaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ potreba }: IRootState) => ({
  potrebaEntity: potreba.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PotrebaDetail);
