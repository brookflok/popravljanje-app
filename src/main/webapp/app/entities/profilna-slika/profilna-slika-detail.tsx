import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './profilna-slika.reducer';
import { IProfilnaSlika } from 'app/shared/model/profilna-slika.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProfilnaSlikaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProfilnaSlikaDetail = (props: IProfilnaSlikaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { profilnaSlikaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.profilnaSlika.detail.title">ProfilnaSlika</Translate> [<b>{profilnaSlikaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="ime">
              <Translate contentKey="popravljanjeApp.profilnaSlika.ime">Ime</Translate>
            </span>
          </dt>
          <dd>{profilnaSlikaEntity.ime}</dd>
          <dt>
            <span id="datum">
              <Translate contentKey="popravljanjeApp.profilnaSlika.datum">Datum</Translate>
            </span>
          </dt>
          <dd>
            {profilnaSlikaEntity.datum ? <TextFormat value={profilnaSlikaEntity.datum} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/profilna-slika" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/profilna-slika/${profilnaSlikaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ profilnaSlika }: IRootState) => ({
  profilnaSlikaEntity: profilnaSlika.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProfilnaSlikaDetail);
