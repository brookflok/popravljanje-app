import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IArtikl } from 'app/shared/model/artikl.model';
import { getEntities as getArtikls } from 'app/entities/artikl/artikl.reducer';
import { getEntity, updateEntity, createEntity, reset } from './galerija.reducer';
import { IGalerija } from 'app/shared/model/galerija.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGalerijaUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GalerijaUpdate = (props: IGalerijaUpdateProps) => {
  const [artiklId, setArtiklId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { galerijaEntity, artikls, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/galerija');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getArtikls();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.datum = convertDateTimeToServer(values.datum);

    if (errors.length === 0) {
      const entity = {
        ...galerijaEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="popravljanjeApp.galerija.home.createOrEditLabel">
            <Translate contentKey="popravljanjeApp.galerija.home.createOrEditLabel">Create or edit a Galerija</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : galerijaEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="galerija-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="galerija-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="imeLabel" for="galerija-ime">
                  <Translate contentKey="popravljanjeApp.galerija.ime">Ime</Translate>
                </Label>
                <AvField id="galerija-ime" type="text" name="ime" />
              </AvGroup>
              <AvGroup>
                <Label id="datumLabel" for="galerija-datum">
                  <Translate contentKey="popravljanjeApp.galerija.datum">Datum</Translate>
                </Label>
                <AvInput
                  id="galerija-datum"
                  type="datetime-local"
                  className="form-control"
                  name="datum"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.galerijaEntity.datum)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="galerija-artikl">
                  <Translate contentKey="popravljanjeApp.galerija.artikl">Artikl</Translate>
                </Label>
                <AvInput id="galerija-artikl" type="select" className="form-control" name="artikl.id">
                  <option value="" key="0" />
                  {artikls
                    ? artikls.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.ime}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/galerija" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  artikls: storeState.artikl.entities,
  galerijaEntity: storeState.galerija.entity,
  loading: storeState.galerija.loading,
  updating: storeState.galerija.updating,
  updateSuccess: storeState.galerija.updateSuccess,
});

const mapDispatchToProps = {
  getArtikls,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GalerijaUpdate);
